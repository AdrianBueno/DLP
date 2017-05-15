package semantico.visitor;

import ast.Programa;
import ast.Traceable;
import ast.declaraciones.Declaracion;
import ast.declaraciones.DefFuncion;
import ast.declaraciones.DefStruct;
import ast.declaraciones.DefVariable;
import ast.expresiones.ExCampo;
import ast.expresiones.ExInvocacion;
import ast.expresiones.Variable;
import ast.sentencias.SenInvocacion;
import ast.tipos.TipoStruct;
import main.GestorErrores;
import semantico.ContextMap;
import visitor.DefaultVisitor;

/**
 * @author Adri�nBueno
 *
 */
public class Identificacion extends DefaultVisitor {
	
	public static final String ALREADY_DEFINED = "Identificador ya en uso.";
	public static final String NOT_YET_DEFINED = "Identificador a�n no definido.";
	
	private ContextMap<String,DefVariable> variables;
	private ContextMap<String,DefFuncion> funciones;
	private ContextMap<String,DefStruct> structs;
	private GestorErrores gestorErrores;
	
		
	public Identificacion(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}

	@Override
	public Object visit(Programa node, Object param){
		variables  = new ContextMap<String,DefVariable>();
		funciones = new ContextMap<String,DefFuncion>();
		structs = new ContextMap<String,DefStruct>();
		super.visit(node, param);
		return param;
	}
		
	/**
	 * En la definci�n de una variable, comprobamos que el nombre se encuentra disponible, de ser as� a�adimos el nodo
	 * a la estructura d�nde guardamos las varaibles
	 * al salir de la funci�n, el visitor pasar� por el nodo Tipo asociado a la variable. 
	 *
	 */
	@Override
	public Object visit(DefVariable node, Object param){
		Declaracion declaracion = variables.getFromTop(node.getNombre());
		predicado(declaracion == null, ALREADY_DEFINED + " Variable: " + node.getNombre(), node);
		variables.put(node.getNombre(), node);
		super.visit(node, param);
		return param;
	}
	
	/**
	 * En la definici�n de un tipo, comprobamos que el nombre se encuentra disponisble y de ser as� a�adimos el nodo
	 * a la estructura d�nde guardamos las Structs.
	 * Ahora debemos comprobar que no haya campos con nombres repetidos. Para ello creamos un �mbito nuevo en la
	 * estructura de variables, recorremos los campos como DefVariable y los visitamos. El visitor se dirigir�
	 * a la funci�n visit que visita DefVariable.
	 */
	@Override
	public Object visit(DefStruct node, Object param){
		DefStruct struct = structs.getFromTop(node.getNombre());
		predicado(struct == null, ALREADY_DEFINED + " Struct: " + node.getNombre(), node);
		structs.put(node.getNombre(), node);
		variables.set();
		visitChildren(node.getListaCampos(), param);
		variables.reset();
		return param;
	}

	/**
	 * En la definici�n de una funci�n, comprobamos que el nombre se encuentre disponible y de ser as� a�adimos el nodo
	 * a la estructura de funciones.
	 * Para determinar si los par�metros y las declaraciones tienen nombres v�lidos es necesario abrir un nuevo �mbito
	 * para ello hacemos un .set a la estructura variables y luego un reset, por el medio se llama al m�todo visit
	 * que recorre todos los hijos, incluyendo el tipo de retorno, par�metros, declaraciones y sentencias.
	 * Para cada lista de hijos es necesario mantener el nuevo �mbito de variables de la funci�n.
	 */
	@Override
	public Object visit(DefFuncion node, Object param){
		Declaracion declaracion = funciones.getFromTop(node.getNombre());
		predicado(declaracion == null, ALREADY_DEFINED + " Funcion: " + node.getNombre(), node);
		funciones.put(node.getNombre(), node);
		variables.set();
		super.visit(node, param);
		variables.reset();
		return param;
	}

	
	@Override
	public Object visit(SenInvocacion node, Object param){
		DefFuncion declaracion = funciones.getFromTop(node.getNombre());
		predicado(declaracion != null, NOT_YET_DEFINED + " Invocacion: " + node.getNombre(), node);
		node.setInvocacion(declaracion);
		return super.visit(node, param);
	}

	@Override
	public Object visit(ExInvocacion node, Object param) {
		DefFuncion declaracion = funciones.getFromTop(node.getNombre());
		predicado(declaracion != null, NOT_YET_DEFINED + " Invocacion: " + node.getNombre(), node);
		node.setInvocacion(declaracion);
		return super.visit(node, param);
	}
	
	@Override
	public Object visit(Variable node, Object param){
		DefVariable declaracion = variables.getFromAny(node.getNombre());
		predicado(declaracion != null, NOT_YET_DEFINED + " Variable: " + node.getNombre(), node);
		node.setDeclaracion(declaracion);
		return param;
	}


	@Override
	public Object visit(ExCampo node, Object param){
		return node.getStruct().accept(this, param);
		/*
		 * No podemos recorrer el campo aqu�, pues es una DefVariable, como no tenemos el tipo de la struct
		 * ni el �mbito, el visitor de DefVariable no podr� asignar la declaraci�n al campo.
		 * Esto lo hacemos en la fase de Comprobaci�n de tipos.
		 * 
		 * Sin embargo debemos sobrescrivirlo para que el padre no recorra los 2 hijos.
		 * */
	}
	
	/**
	 * Cuando se declara una variable de tipo struct, debemos visitar el tipo de la definici�n, de esta forma
	 * podemos comprobar si existe el tipo Struct que se ha declarado.
	 */
	@Override
	public Object visit(TipoStruct node, Object param){
		DefStruct def =  structs.getFromTop(node.getNombreTipo());
		predicado(def != null, NOT_YET_DEFINED + " TipoStruct: " + node.getNombreTipo(), node);
		node.setDefinicion(def);
		return super.visit(node, param);
	}
	

	
	/**
	 * M�todo auxiliar opcional para ayudar a implementar los predicados de la Gram�tica Atribuida.
	 * 
	 * Ejemplo de uso:
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", expr.getStart());
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", null);
	 * 
	 * NOTA: El m�todo getStart() indica la linea/columna del fichero fuente de donde se ley� el nodo.
	 * Si se usa VGen dicho m�todo ser� generado en todos los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion Debe cumplirse para que no se produzca un error
	 * @param mensajeError Se imprime si no se cumple la condici�n
	 * @param posicionError Fila y columna del fichero donde se ha producido el error. Es opcional (acepta null)
	 */
	private boolean predicado(boolean condicion, String mensajeError, Traceable traceable) {
		if (!condicion)
			gestorErrores.error("Identificaci�n", mensajeError, traceable.getStart());
		return condicion;
	}

	
}
