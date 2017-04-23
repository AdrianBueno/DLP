package semantico.visitor;

import ast.AbstractTipo;
import ast.Programa;
import ast.Traceable;
import ast.declaraciones.Declaracion;
import ast.declaraciones.DefFuncion;
import ast.declaraciones.DefStruct;
import ast.declaraciones.DefVariable;
import ast.expresiones.ExAritmetica;
import ast.expresiones.ExCampo;
import ast.expresiones.ExCast;
import ast.expresiones.ExIndice;
import ast.expresiones.ExInvocacion;
import ast.expresiones.ExLogica;
import ast.expresiones.ExNot;
import ast.expresiones.ExRelacional;
import ast.expresiones.Variable;
import ast.expresiones.literales.LitCaracter;
import ast.expresiones.literales.LitEntero;
import ast.expresiones.literales.LitReal;
import ast.sentencias.SenAsignacion;
import ast.sentencias.SenIf;
import ast.sentencias.SenInvocacion;
import ast.sentencias.SenPrint;
import ast.sentencias.SenPrintLn;
import ast.sentencias.SenPrintSp;
import ast.sentencias.SenRead;
import ast.sentencias.SenReturn;
import ast.sentencias.SenWhile;
import ast.sentencias.Sentencia;
import ast.tipos.Tipo;
import ast.tipos.TipoArray;
import ast.tipos.TipoCaracter;
import ast.tipos.TipoEntero;
import ast.tipos.TipoReal;
import ast.tipos.TipoStruct;
import ast.tipos.TipoVoid;
import main.GestorErrores;
import visitor.DefaultVisitor;

public class ComprobacionDeTipos extends DefaultVisitor {

	public ComprobacionDeTipos(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}
	
	@Override
	public Object visit(Programa node, Object param) {
		return super.visit(node, param);
	}
	
	@Override
	public Object visit(DefVariable node, Object param) {
		return super.visit(node, param);
	}
	
	@Override
	public Object visit(DefStruct node, Object param) {
		return super.visit(node, param);
	}
	
	/**
	 * 
	 */
	@Override
	public Object visit(DefFuncion node, Object param) {
		node.getTipo().accept(this, null);
		predicado(tipoRetorno(node.getTipo()),"Error, el tipo de retorno debe ser Simple: " + node.getNombre(),node); //Retorno primitivo
		for (Declaracion def : node.getListaParametros()) {
			def.accept(this, null);
			predicado(tipoPrimitivo(def.getTipo()),"Error, hay par�metro/s de tipo no Simple: " + node.getNombre(),node); //Parametros primitivos
		}
		for (Declaracion def : node.getListaDeclaraciones()) {
			def.accept(this, null);
		}
		for (Sentencia sen : node.getListaSentencias()) {
			sen.setHisFunction(node);	// Regla Sem�ntica
			sen.accept(this, null);
		}		
		
		return null;
	}
	
	@Override
	public Object visit(SenAsignacion node, Object param) {
		Object ob = super.visit(node, param);
		predicado(node.getLeft().isModificable(),"Error, la parte izquierda de la asignaci�n no es modificable ",node); //Left modificable
		predicado(tipoPrimitivo(node.getLeft().getTipo()),"Error, la parte izquierda debe ser tipo Simple ",node); //Left Simple
		predicado(mismoTipo(node.getLeft().getTipo(), node.getRight().getTipo()),"Error, Las 2 partes deben tener mismo tipo ",node); //Mismo tipo
		return ob;
	}
	
	@Override
	public Object visit(SenInvocacion node, Object param) {
		DefFuncion invocacion = (DefFuncion)node.getInvocacion();
		int numParams = invocacion.getListaParametros().size();
		int numArgs = node.getListaArgumentos().size();
		predicado(numParams == numArgs, "Error, no exite funci�n: " +node.getNombre() +", que tome: "+ node.getListaArgumentos().size() +" argumentos.",node); // Argumentos == Parametros
		if(numParams == numArgs)
			for(int i = 0; i < node.getListaArgumentos().size(); i++){
				node.getListaArgumentos().get(i).accept(this, null);
				Tipo tipoArg = node.getListaArgumentos().get(i).getTipo();
				Tipo tipoPar = invocacion.getListaParametros().get(i).getTipo();
				predicado(mismoTipo(tipoArg, tipoPar),"Error, el tipo de los argumentos no coincide con el de los par�metros de: " + invocacion.getNombre(),node); // Argumentos_i.tipo == Parametros_i.tipo				
			}
		return param;
	}
	
	@Override
	public Object visit(SenPrint node, Object param) {
		super.visit(node, param);
		predicado(tipoPrimitivo(node.getExpresion().getTipo()),"Error, la expresi�n debe ser tipo Simple",node); //Print primitivo
		return null;
	}
	/**
	 * El nodo PrintLn puede tener una expresi�n nula.
	 */
	@Override
	public Object visit(SenPrintLn node, Object param) {
		super.visit(node, param);
		if(node.getExpresion() != null)
			predicado(tipoPrimitivo(node.getExpresion().getTipo()),"Error, la expresi�n debe ser tipo Simple",node); //Print primitivo
		return null;
	}
	
	@Override
	public Object visit(SenPrintSp node, Object param) {
		super.visit(node, param);
		predicado(tipoPrimitivo(node.getExpresion().getTipo()),"Error, la expresi�n debe ser tipo Simple",node); //Print primitivo
		return null;
	}
	
	@Override
	public Object visit(SenRead node, Object param) {
		super.visit(node, param);
		predicado(node.getExpresion().isModificable(),"Error, la expresi�n de read debe ser modificable",node); //Read modificable
		predicado(tipoPrimitivo(node.getExpresion().getTipo()),"Error, la expresi�n de read debe ser tipo Simple",node); //Read primitivo
		return null;
	}
	// Las sentencias if y while, al tener que setear la definici�n es una regla sem�ntica
	// que se ejecuta antes de ir al hijo a diferencia de las dem�s.
	@Override
	public Object visit(SenIf node, Object param) {
		node.getCondicion().accept(this, null); //Entramos a condici�n para que asigne el tipo.
		predicado(tipoNumerico(node.getCondicion().getTipo()),"Error, la condici�n debe ser de tipo Num�rico",node); //Condicion Entera
		for (Sentencia sen : node.getSentenciasIf()) {
			sen.setHisFunction(node.getHisFunction()); //Regla Sem�ntica
			sen.accept(this, null);
		}
		for (Sentencia sen : node.getSentenciasElse()) {
			sen.setHisFunction(node.getHisFunction()); //Regla Sem�ntica
			sen.accept(this, null);
		}
		return null;
	}
	
	@Override
	public Object visit(SenWhile node, Object param) {
		node.getCondicion().accept(this, null); //Entramos a condici�n para que asigne el tipo.
		predicado(tipoNumerico(node.getCondicion().getTipo()),"Error, la condici�n debe ser de tipo Num�rico",node); //Condicion Entera
		for (Sentencia sen : node.getSentencias()) {
			sen.setHisFunction(node.getHisFunction());//Regla Sem�ntica
			sen.accept(this, null);
		}
		return null;
	}
	
	@Override
	public Object visit(SenReturn node, Object param) {
		if (node.getRetorno() != null) { // Consultar esto mejor.
			node.getRetorno().accept(this, null);
			predicado(mismoTipo(node.getHisFunction().getTipo(), node.getRetorno().getTipo()),"Error, la funci�n define un tipo de retorno distinto al dado.",node); // retorno.tipo == definicion.tipo
		} else //Tipo Void
			predicado(mismoTipo(node.getHisFunction().getTipo(), AbstractTipo.getTipoVoid()),"Error, la funci�n requiere devolver un valor, pero se devuelve Void.",node);
		return null;
	}
	
	@Override
	public Object visit(ExAritmetica node, Object param) {
		super.visit(node, param);	
		predicado(tipoNumerico(node.getLeft().getTipo()) && tipoNumerico(node.getRight().getTipo()),"Error, la expresi�n aritm�tica tiene tipos no numericos",node); // INT || FLOAT
		predicado(mismoTipo(node.getRight().getTipo(),node.getLeft().getTipo()), "Error, �mbas partes de la expresi�n aritm�tica deben ser del mismo tipo", node);
		node.setTipo(getTipoMayor(node.getLeft().getTipo(), node.getRight().getTipo())); // Elegimos el tipo mayor
		node.setModificable(false);
		return null;
	}
	
	@Override
	public Object visit(ExLogica node, Object param) {
		super.visit(node, param);
		predicado(tipoEntero(node.getLeft().getTipo()) && tipoEntero(node.getRight().getTipo()),"Error, la expresi�n logica tiene tipos no enteros",node); // Condici�n entera
		node.setTipo(AbstractTipo.getTipoEntero()); //Debe ser tipo entero al ser una expresi�n l�gica.
		node.setModificable(false);
		return null;
	}
	
	@Override
	public Object visit(ExRelacional node, Object param) {
		super.visit(node, param);
		predicado(tipoNumerico(node.getLeft().getTipo()),"Error, la parte izquierda de la expresi�n relacional tiene tipos no numericos",node); // INT || FLOAT
		predicado(tipoNumerico(node.getRight().getTipo()),"Error, la parte derecha de la expresi�n relacional tiene tipos no numericos",node); // INT || FLOAT
		predicado(mismoTipo(node.getRight().getTipo(),node.getLeft().getTipo()), "Error, �mbas partes de la expresi�n realacional deben ser del mismo tipo", node);
		//node.setTipo(getTipoMayor(node.getLeft().getTipo(), node.getRight().getTipo())); //Debe ser del tipo mayor, si no en generaci�n de c�digo generar� mal la instrucci�n.
		node.setTipo(AbstractTipo.getTipoEntero());
		node.setModificable(false);
		return null;
	}
	
	@Override
	public Object visit(ExNot node, Object param) {
		super.visit(node, param);
		predicado(tipoEntero(node.getExpresion().getTipo()),"Error, la expresi�n Not tiene tipos no enteros",node); // Condici�n entera
		node.setTipo(AbstractTipo.getTipoEntero()); //Tipo Entero
		node.setModificable(false);
		return null;
	}
	
	@Override
	public Object visit(ExInvocacion node, Object param) {
		DefFuncion invocacion = (DefFuncion)node.getInvocacion();
		int numParams = invocacion.getListaParametros().size();
		int numArgs = node.getListaArgumentos().size();
		boolean mismoNumArgs = false;
		mismoNumArgs = predicado(numParams == numArgs, "Error, no exite funci�n: " +node.getNombre() +", que tome: "+ node.getListaArgumentos().size() +" argumentos.",node); // Argumentos == Parametros
		if(mismoNumArgs)
			for(int i = 0; i < node.getListaArgumentos().size(); i++){
				node.getListaArgumentos().get(i).accept(this, null);
				Tipo tipoArg = node.getListaArgumentos().get(i).getTipo();
				Tipo tipoPar = invocacion.getListaParametros().get(i).getTipo();
				predicado(mismoTipo(tipoArg, tipoPar),"Error, el tipo de los argumentos no coincide con el de los par�metros de: " + invocacion.getNombre(),node); // Argumentos_i.tipo == Parametros_i.tipo				
			}
		predicado(tipoPrimitivo(invocacion.getTipo()), "La expresion de invocaci�n no devuelve un tipo Simple",node);
		node.setModificable(false);
		node.setTipo(invocacion.getTipo());
		return null;
	}
	
	/* La expresi�n ExIndice, es la expresi�n para acceder a un �ndice de una variable
	 * TipoArray, tras comprobar que sea de este t�po y el �ndice entero, se debe 
	 * establecer como tipo de expresi�n el tipobase del tipoArray.
	 *  
	 */
	@Override
	public Object visit(ExIndice node, Object param) {
		super.visit(node, param);
		boolean isArray = false;
		isArray = predicado(node.getLeft().getTipo() instanceof TipoArray,"Error, el acceso con �ndice debe ser sobre TipoArray",node); // Tipo Array
		predicado(tipoEntero(node.getIndice().getTipo()),"Error, el acceso con �ndice debe tener un �ndice entero",node); // Indice Entero
		TipoArray tipo = null;
		if(isArray){ // Tras el acceso el tipo de la expresi�n es el tipo Base del Array
			tipo = (TipoArray)node.getLeft().getTipo();
			node.setTipo(tipo.getTipoBase());
			node.setModificable(node.getLeft().isModificable());
		}
		return null;
	}
	
	/**
	 * Al acceder a un campo, primero debemos visitar la parte izquierda de la expresi�n, para que esta tenga el tipo
	 * definido, con el tipo obtenemos la definici�n del tipo, que ser� null si no es TipoStruct.
	 */
	@Override
	public Object visit(ExCampo node, Object param) {
		node.getStruct().accept(this, param);
		DefStruct defStruct = node.getStruct().getTipo().getDefinicion();
		Variable campo = node.getCampo();
		predicado(node.getStruct().isModificable(),"Error, el struct debe ser modificable",node);
		predicado(campo.isModificable(),"Error, el campo debe ser modificable",node);
		predicado(defStruct != null,"Error, el acceso a campo debe ser sobre tipo Struct",node); // Tipo Variable
		if(defStruct != null)
			for(DefVariable defVar : defStruct.getListaCampos())
				if(defVar.getNombre().equals(campo.getNombre()))
					campo.setDeclaracion(defVar);
		predicado(campo.getDeclaracion() != null,"Error, el campo no est� definido para la struct",node);
		campo.accept(this, param);
		node.setTipo(node.getCampo().getTipo());
		node.setModificable(node.getCampo().isModificable());
		return param;
	}
	
	@Override
	public Object visit(ExCast node, Object param) {
		super.visit(node, param);
		predicado(!mismoTipo(node.getTo(), node.getFrom().getTipo()),"Error, No se puede hacer un cast al mismo tipo",node); // Diferente tipo
		predicado(tipoPrimitivo(node.getTo()),"Error, la parte To del cast debe ser tipo primitiva",node); // Cast To
		predicado(tipoPrimitivo(node.getFrom().getTipo()),"Error, la parte from del cast debe ser tipo primitiva",node); // Cast From
		node.setTipo(node.getTo());
		node.setModificable(false);
		return null;
	}
	
	/*
	 * 			LITERALES y Terminales
	 * Para definir su tipo utilizo una �nica instancia compartida 
	 * que se encuentra en la clase abstracta de la que heredan.
	 * 
	 * */

	@Override
	public Object visit(LitEntero node, Object param) {
		node.setModificable(false);
		node.setTipo(AbstractTipo.getTipoEntero());
		return super.visit(node, param);
	}

	@Override
	public Object visit(LitReal node, Object param) {
		node.setModificable(false);
		node.setTipo(AbstractTipo.getTipoReal());
		return super.visit(node, param);
	}

	@Override
	public Object visit(LitCaracter node, Object param) {
		node.setModificable(false);
		node.setTipo(AbstractTipo.getTipoCaracter());
		return super.visit(node, param);
	}
	
	@Override
	public Object visit(Variable node, Object param) {
		//Reglas sem�nticas
		if(node.getDeclaracion() != null)
			node.setTipo(node.getDeclaracion().getTipo());
		node.setModificable(true);
		return null;
	}
	
	/*
	 * 			TIPOS DE VARIABLE
	 * 
	 * */

	public Object visit(TipoEntero node, Object param) {
		return super.visit(node, param);
	}

	public Object visit(TipoReal node, Object param) {
		return super.visit(node, param);
	}

	public Object visit(TipoCaracter node, Object param) {
		return super.visit(node, param);
	}

	public Object visit(TipoStruct node, Object param) {
		return super.visit(node, param);
	}

	public Object visit(TipoArray node, Object param) {
		predicado(node.getArraySize() > 0, "No se puede declarar un array de 0 elementos",node);
		return super.visit(node, param);
	}

	public Object visit(TipoVoid node, Object param) {
		return super.visit(node, param);
	}

	/**
	 * M�todo auxiliar opcional para ayudar a implementar los predicados de la
	 * Gram�tica Atribuida.
	 * 
	 * Ejemplo de uso (suponiendo implementado el m�todo "esPrimitivo"):
	 * predicado(esPrimitivo(expr.tipo), "La expresi�n debe ser de un tipo
	 * primitivo", expr.getStart()); predicado(esPrimitivo(expr.tipo), "La
	 * expresi�n debe ser de un tipo primitivo", null);
	 * 
	 * NOTA: El m�todo getStart() indica la linea/columna del fichero fuente de
	 * donde se ley� el nodo. Si se usa VGen dicho m�todo ser� generado en todos
	 * los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion
	 *            Debe cumplirse para que no se produzca un error
	 * @param mensajeError
	 *            Se imprime si no se cumple la condici�n
	 * @param posicionError
	 *            Fila y columna del fichero donde se ha producido el error. Es
	 *            opcional (acepta null)
	 */
	private boolean predicado(boolean condicion, String mensajeError, Traceable traceable) {
		if (!condicion)
			gestorErrores.error("Tipos", mensajeError, traceable.getStart());
		return condicion;
	}

	private boolean tipoPrimitivo(Tipo tipo) {
		return tipo instanceof TipoEntero || tipo instanceof TipoReal || tipo instanceof TipoCaracter;
	}
	
	private boolean tipoRetorno(Tipo tipo){
		return tipo instanceof TipoVoid || tipoPrimitivo(tipo);
	}

	private boolean mismoTipo(Tipo tipo1, Tipo tipo2) {
		return tipo1.getNombreTipo().equals(tipo2.getNombreTipo());
	}

	private boolean tipoNumerico(Tipo tipo) {
		return tipo instanceof TipoEntero || tipo instanceof TipoReal;
	}
	
	private boolean tipoEntero(Tipo tipo){
		return tipo instanceof TipoEntero;
	}
	
	private Tipo getTipoMayor(Tipo tipo1, Tipo tipo2){
		return tipo1.getBytes() > tipo2.getBytes() ? tipo1 : tipo2;
	}
	

	private GestorErrores gestorErrores;
}
