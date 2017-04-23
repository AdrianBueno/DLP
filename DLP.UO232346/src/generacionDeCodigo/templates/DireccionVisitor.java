package generacionDeCodigo.templates;

import ast.declaraciones.DefVariable;
import ast.expresiones.ExCampo;
import ast.expresiones.ExIndice;
import ast.expresiones.Variable;
import generacionDeCodigo.SourceHelper;
import visitor.DefaultVisitor;
/**
 * En este visitor obtenemos direcciones necesarias en sentencias u expresiones.
 * La dirección es de variables en sus diferentes formas, Globales, locales, parámetros, indices de vectores y campos
 * de Structs.
 * 
 * @author AdriánBueno
 * 
 *
 */
public class DireccionVisitor extends DefaultVisitor {

	private static DireccionVisitor instance = null;
	
	public static DireccionVisitor getInstance(){
		if(instance == null)
			instance = new DireccionVisitor();
		return instance;
	}
	
	private SourceHelper sh;
	private ValorVisitor valorVisitor; //Debe ser inicialización Lazy
	
	private DireccionVisitor(){
		sh = SourceHelper.getInstance();
	}

	/**
	 * Al visitar el nodo variable para obtener su dirección, debemos diferenciar como es el accceso a esta dirección
	 * según el tipo de varaible, para las locales debemos meter el BP en la pila, y añadir la dirección de la variable
	 * que, en la fase de gestión de memoria ya se han encargado de que sea la adecuada tanto si es una local o un 
	 * parámetro.
	 * Las globales simplemente la dirección será la absoluta.
	 * Si es un campo la dirección será relativa a su posición dentro del Struct, aunque al final es igual que la local
	 * lo he separado para que se vea con mas claridad.
	 */
	@Override
	public Object visit(Variable node, Object param) {
	
		if(node.getDeclaracion().getAmbito() == DefVariable.LOCAL){
			sh.code("PUSHa BP");
			sh.code("PUSHi " + node.getDeclaracion().getAddress());
			sh.code("ADDi");
		}
		if(node.getDeclaracion().getAmbito() == DefVariable.GLOBAL){
			sh.code("PUSHa " + node.getDeclaracion().getAddress());
		}
		if(node.getDeclaracion().getAmbito() == DefVariable.CAMPO){
			sh.code("PUSHa " + node.getDeclaracion().getAddress());
		}
		
		return param;
	}

	/**
	 * El acceso a un índice necesitará la dirección en 3 casos:
	 * 1. Para hacer despues un Load desde el VisitorValor.
	 * 2. Si es la parte izquierda de una asignación.
	 * 3. Si su padre es un acceso campo: array[i].campo
	 * La dirrección la sacamos a partir de la dirección de la parte izquierda del acceso, mas la multiplicación
	 * de los bytes del tipo base del array por el valor del índice y sumando esto a la dirección de la parte izquierda.
	 * 
	 */
	@Override
	public Object visit(ExIndice node, Object param) {
		node.getLeft().accept(this, param);
		int bytesBase = node.getTipo().getBytes(); //Esto tiene que ser el tipo base del array
		sh.code("PUSHi " + bytesBase);
		node.getIndice().accept(getValor(), param);
		sh.code("MULi");
		sh.code("ADDi");
		return param;
	}
	
	/**
	 * El acceso a campo necesiatará la dirección en:
	 * 1. Para hacer después un Load desde el VisitorValor
	 * 2. Si es la parte izquierda de una asignación.
	 * 3. Si su padre es un acceso a array: struct.campo[i]
	 */
	@Override
	public Object visit(ExCampo node, Object param) {
		node.getStruct().accept(this, param);
		sh.code("PUSHi " + node.getCampo().getDeclaracion().getAddress());
		sh.code("ADDi");
		return param;
	}

	/*
	 * Utilizamos inicialización Lazy, porque si lo instanciamos este y el de valor en el constructor habría un
	 * bucle infinito.
	 */
	public ValorVisitor getValor(){
		if(valorVisitor == null)
			valorVisitor = ValorVisitor.getInstance();
		return valorVisitor;
	}
	
	
	
	
	
	

}
