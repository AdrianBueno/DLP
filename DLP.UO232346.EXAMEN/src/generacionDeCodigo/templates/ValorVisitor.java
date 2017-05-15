package generacionDeCodigo.templates;

import java.util.HashMap;
import java.util.Map;

import ast.expresiones.ExAritmetica;
import ast.expresiones.ExCampo;
import ast.expresiones.ExCast;
import ast.expresiones.ExIndice;
import ast.expresiones.ExInvocacion;
import ast.expresiones.ExLogica;
import ast.expresiones.ExNot;
import ast.expresiones.ExRelacional;
import ast.expresiones.Expresion;
import ast.expresiones.Variable;
import ast.expresiones.literales.LitCaracter;
import ast.expresiones.literales.LitEntero;
import ast.expresiones.literales.LitReal;
import generacionDeCodigo.SourceHelper;
import visitor.DefaultVisitor;
/**
 * En este visitor obtenemos el valor de variables y expresiones en general.
 * Se usa en combinaci�n con el Direcci�nVisitor para obtener primero la direcci�n y luego hacer un LOAD
 * @author Adri�n Bueno
 *
 */
public class ValorVisitor extends DefaultVisitor {

	private static ValorVisitor instance = null;
	private static Map<String, String> inst = new HashMap<String, String>();
	static {
		inst.put("+", "ADD");
		inst.put("*", "MUL");
		inst.put("-", "SUB");
		inst.put("/", "DIV");
		inst.put("&&", "AND");
		inst.put("||", "OR");
		inst.put(">", "GT");
		inst.put("<", "LT");
		inst.put(">=", "GE");
		inst.put("<=", "LE");
		inst.put("==", "EQ");
		inst.put("!=", "NE");
		inst.put("!", "NOT");
	}
	
	
	public static ValorVisitor getInstance(){
		if(instance == null)
			instance = new ValorVisitor();
		return instance;
	}
	
	
	private SourceHelper sh;
	private DireccionVisitor direccionVisitor; //Debe ser inicializaci�n Lazy
	
	private ValorVisitor(){
		sh = SourceHelper.getInstance();
	}
	
	
	/**
	 * Al visitar un acceso a �ndice obtenemos la direcci�n de memoria donde comienza el array, luego, multiplicamos 
	 * el n�mero de bytes del tipo base por el valor del �ndice, y esto se lo sumamos a la direcci�n base del array
	 * Para finalmente hacer cargar el valor correspondiente 
	 */
	@Override
	public Object visit(ExIndice node, Object param) {
		node.getLeft().accept(getDireccion(), param);
		int bytesBase = node.getTipo().getBytes(); //Esto tiene que ser el tipo base del array
		sh.code("PUSHi " + bytesBase);
		node.getIndice().accept(this, param);
		sh.code("MULi");
		sh.code("ADDi");
		sh.code("LOAD"+sh.getSubfijo(node.getTipo()));
		return param;
	}

	/**
	 * Al visitar un acceso a Campo, obtenemos al direcci�n donde comienza la variable de tipo Struct, luego, a partir
	 * de la declaraci�n (Fase de tipos) del campo obtenemos su direcci�n relativa dentro del Struct.
	 * Simplemente se la sumamos a la direcci�n base de la variable y hacemos un Load para cargar el valor del campo.
	 */
	@Override
	public Object visit(ExCampo node, Object param) {
		node.getStruct().accept(getDireccion(), param);
		sh.code("PUSHi " + node.getCampo().getDeclaracion().getAddress());
		sh.code("ADDi");
		sh.code("LOAD"+sh.getSubfijo(node.getCampo().getTipo()));
		return param;
	}
	
	/**
	 * En una expresi�n binaria siempre necesitamos el valor de ambas partes y coder la operaci�n correspondiente
	 * a partir del operador guardado en el nodo.
	 */
	@Override
	public Object visit(ExAritmetica node, Object param) {
		node.getLeft().accept(this, param);
		node.getRight().accept(this, param);
		sh.code(inst.get(node.getOperador()) + sh.getSubfijo(node.getTipo()));
		return param;
	}

	/**
	 * En una expresi�n binaria siempre necesitamos el valor de ambas partes y coder la operaci�n correspondiente
	 * a partir del operador guardado en el nodo.
	 */
	@Override
	public Object visit(ExLogica node, Object param) {
			
		if(node.getOperador().equals("^")){
			node.getLeft().accept(this, param); //a
			sh.code("NOT"); //a Negada
			node.getRight().accept(this, param); //b
			sh.code("AND"); //AND !a b
			node.getLeft().accept(this, param); // a
			node.getRight().accept(this, param); // b
			sh.code("NOT"); //b negada
			sh.code("AND"); //AND a !b
			sh.code("OR"); // AND !a b OR AND a !b
		}else{
			node.getLeft().accept(this, param);
			node.getRight().accept(this, param);
			sh.code(inst.get(node.getOperador()));
		}
		
		
		
		return param;
	}

	/**
	 * En una expresi�n binaria siempre necesitamos el valor de ambas partes y coder la operaci�n correspondiente
	 * a partir del operador guardado en el nodo. Sin conversiones expl�citas.
	 * La generaci�n de la isntrucci�n relacional es delicada, el nodo debe de ser tipo entero, para poder formar parte
	 * de otra expresi�n l�gica, pero a la vez debe distinguir el tipo real del entero para generar la instrucci�n
	 * adecuada, por eso opto por usar el tipo de uno de sus hijos.
	 * Los tipos de lo hijos son iguales.
	 * 
	 */
	@Override
	public Object visit(ExRelacional node, Object param) {
		node.getLeft().accept(this, param);
		node.getRight().accept(this, param);
		sh.code(inst.get(node.getOperador()) + sh.getSubfijo(node.getLeft().getTipo()));
		return param;
	}

	/**
	 * La negaci�n simplemente debemos tener el valor de la expresi�n y a�adir una instrucci�n NOT
	 */
	@Override
	public Object visit(ExNot node, Object param) {
		node.getExpresion().accept(this, param);
		sh.code(inst.get("!"));
		return param;
	}

	/**
	 * En una expresi�n de invocacion debemos generar instrucciones para tener en la pila el valor de todos los 
	 * argumentos y despues realizar una llamada a la funci�n.
	 * Los par�metros son por valor.
	 */
	@Override
	public Object visit(ExInvocacion node, Object param) {
		for(Expresion expr : node.getListaArgumentos())
			expr.accept(this, param);
		sh.code("CALL " + node.getNombre());
		return param;
	}
	
	/**
	 * En un cast necesitamos el valor de la parte derecha del cast.
	 * Luego generamos la instrucci�n a partir de los subfijos de cada tipo.
	 */
	@Override
	public Object visit(ExCast node, Object param) {
		node.getFrom().accept(this, param);
		sh.code(sh.getSubfijo(node.getFrom().getTipo()).toUpperCase() + 2 + sh.getSubfijo(node.getTo()).toUpperCase());
		return param;
	}

	/**
	 * Literales, hacemos el push de tipo correspondiente con el valor del nodo.
	 */
	@Override
	public Object visit(LitEntero node, Object param) {
		sh.code("PUSHi " + node.getValor());
		return null;
	}

	/**
	 * Literales, hacemos el push de tipo correspondiente con el valor del nodo.
	 */
	@Override
	public Object visit(LitReal node, Object param) {
		sh.code("PUSHf " + node.getValor());
		return null;
	}

	/**
	 * Literales, hacemos el push de tipo correspondiente con el valor del nodo.
	 * Aqu� debemos diferenciar entre el cara
	 */
	@Override
	public Object visit(LitCaracter node, Object param) {
		if(node.getValor().equals("'\\n'") || node.getValor().equals("'\\r'"))
			sh.code("PUSHb " + 10);
		else
			sh.code("PUSHb " + (int)node.getValor().charAt(1));
		return null;
	}

	/**
	 * Obtemeos primero la direcci�n y luego cargamos el valor con Load
	 */
	@Override
	public Object visit(Variable node, Object param) {
		node.accept(getDireccion(), param);
		sh.code("LOAD" + sh.getSubfijo(node.getTipo()));
		return param;
	}
	
	/*
	 * Utilizamos inicializaci�n Lazy, porque si lo instanciamos este y el de direcci�n en el constructor habr�a un
	 * bucle infinito.
	 */
	public DireccionVisitor getDireccion(){
		if(direccionVisitor == null)
			direccionVisitor = DireccionVisitor.getInstance();
		return direccionVisitor;
	}
	

	
}
