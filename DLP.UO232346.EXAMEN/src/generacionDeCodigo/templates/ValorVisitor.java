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
 * Se usa en combinación con el DirecciónVisitor para obtener primero la dirección y luego hacer un LOAD
 * @author Adrián Bueno
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
	private DireccionVisitor direccionVisitor; //Debe ser inicialización Lazy
	
	private ValorVisitor(){
		sh = SourceHelper.getInstance();
	}
	
	
	/**
	 * Al visitar un acceso a índice obtenemos la dirección de memoria donde comienza el array, luego, multiplicamos 
	 * el número de bytes del tipo base por el valor del índice, y esto se lo sumamos a la dirección base del array
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
	 * Al visitar un acceso a Campo, obtenemos al dirección donde comienza la variable de tipo Struct, luego, a partir
	 * de la declaración (Fase de tipos) del campo obtenemos su dirección relativa dentro del Struct.
	 * Simplemente se la sumamos a la dirección base de la variable y hacemos un Load para cargar el valor del campo.
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
	 * En una expresión binaria siempre necesitamos el valor de ambas partes y coder la operación correspondiente
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
	 * En una expresión binaria siempre necesitamos el valor de ambas partes y coder la operación correspondiente
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
	 * En una expresión binaria siempre necesitamos el valor de ambas partes y coder la operación correspondiente
	 * a partir del operador guardado en el nodo. Sin conversiones explícitas.
	 * La generación de la isntrucción relacional es delicada, el nodo debe de ser tipo entero, para poder formar parte
	 * de otra expresión lógica, pero a la vez debe distinguir el tipo real del entero para generar la instrucción
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
	 * La negación simplemente debemos tener el valor de la expresión y añadir una instrucción NOT
	 */
	@Override
	public Object visit(ExNot node, Object param) {
		node.getExpresion().accept(this, param);
		sh.code(inst.get("!"));
		return param;
	}

	/**
	 * En una expresión de invocacion debemos generar instrucciones para tener en la pila el valor de todos los 
	 * argumentos y despues realizar una llamada a la función.
	 * Los parámetros son por valor.
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
	 * Luego generamos la instrucción a partir de los subfijos de cada tipo.
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
	 * Aquí debemos diferenciar entre el cara
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
	 * Obtemeos primero la dirección y luego cargamos el valor con Load
	 */
	@Override
	public Object visit(Variable node, Object param) {
		node.accept(getDireccion(), param);
		sh.code("LOAD" + sh.getSubfijo(node.getTipo()));
		return param;
	}
	
	/*
	 * Utilizamos inicialización Lazy, porque si lo instanciamos este y el de dirección en el constructor habría un
	 * bucle infinito.
	 */
	public DireccionVisitor getDireccion(){
		if(direccionVisitor == null)
			direccionVisitor = DireccionVisitor.getInstance();
		return direccionVisitor;
	}
	

	
}
