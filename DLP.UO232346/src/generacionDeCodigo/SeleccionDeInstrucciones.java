package generacionDeCodigo;

import ast.Programa;
import ast.declaraciones.DefFuncion;
import ast.declaraciones.DefStruct;
import ast.declaraciones.DefVariable;
import ast.expresiones.Expresion;
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
import ast.tipos.TipoVoid;
import generacionDeCodigo.templates.DireccionVisitor;
import generacionDeCodigo.templates.ValorVisitor;
import visitor.DefaultVisitor;
import visitor.Visitor;

/**
 * Este es el visitor principal para generar el código, trabaja con 2 visitors mas, para valores y para direcciones,
 * estos 2 se usan en los nodos expresión, y son llamados desde este visitors en sus nodos sentencia.
 * @author AdriánBueno
 * @version 1
 *
 */
public class SeleccionDeInstrucciones extends DefaultVisitor {
	
	private SourceHelper sh;
	private String sourceFile;
	/*
	 * Estas variables se usan para que las sentencias de las funciones
	 * sepan datos sobre su función sin tener que calcularlos otra vez.
	 * Especialemente para que la sentencia Return no tenga que volver a hacer lo que se hace
	 * en la definición de la función para generar el Enter y el ret por defecto.
	 */
	private int localDecBytes = 0;
	private int localParamBytes = 0;
	private int localRetBytes = 0;
	private boolean existMain = false;

	
	public SeleccionDeInstrucciones(String sourceFile) {
		this.sourceFile = sourceFile;
		sh = SourceHelper.getInstance();
		valorVisitor = ValorVisitor.getInstance();
		direccionVisitor = DireccionVisitor.getInstance();
	}
	
	@Override
	public Object visit(Programa node, Object param) {
		sh.meta("#source \"" + sourceFile + "\"");
		sh.code("CALL main");
		sh.code("HALT");
		super.visit(node, param);
		if(!existMain) //Si no existe ninguna función, el programa debe seguir siendo válido.
			sh.codeLabel("main");
		return param;
	}


	/**
	 * No es necesario visitar los hijos de esta definicion
	 */
	@Override
	public Object visit(DefVariable node, Object param) {
		//sh.meta("#VAR "+ node.getNombre() + ":" + node.getTipo().getNombreTipo());
		return param;
	}

	/**
	 * No es necesario visitar los hijos de esta definicion 
	 */
	@Override
	public Object visit(DefStruct node, Object param) {
		return param;
	}

	/**
	 * La definición de una función es el punto de entrada a todo el código.
	 * Para no tener que recalcular en la sentencia de retorno el tamaño de las variables locales, parámetro y retorno
	 * calculamos estos valores y los guardamos aquí.
	 * También se crea una etiqueta que marca el inicio de la función.
	 * En caso de que la última sentencia de la función no sea un nodo de tipo SenReturn se deberá generar el código
	 * de retorno aquí.
	 */
	@Override
	public Object visit(DefFuncion node, Object param) {
		sh.meta("#FUNC " + node.getNombre());
		localParamBytes = 0; //Reiniciamos los valores de ambito de la funcion
		localDecBytes = 0;
		localRetBytes = node.getTipo().getBytes();
		sh.codeLabel(node.getNombre());
		if(node.getNombre().equals("main"))
			existMain = true;
		for(DefVariable defVar : node.getListaParametros()){
			sh.meta("#PARAM " + defVar.getNombre() + ":" + defVar.getTipo().getNombreTipo());
			localParamBytes += defVar.getTipo().getBytes();
		}
		for(DefVariable defVar : node.getListaDeclaraciones()){
			sh.meta("#LOCAL " + defVar.getNombre() + ":" + defVar.getTipo().getNombreTipo());
			localDecBytes += defVar.getTipo().getBytes();
		}
		sh.code("ENTER " + localDecBytes);
		super.visit(node, param);
		if(node.getTipo() instanceof TipoVoid){ //Por defecto
			sh.meta("#RET " + node.getTipo().getNombreTipo());
			sh.code("RET "+ localRetBytes +", " + localDecBytes + ", " + localParamBytes);
		}
		//if(node.getListaSentencias().size() == 0)
		//	sh.code("RET "+ localRetBytes +", " + localDecBytes + ", " + localParamBytes);
		//else if(!(node.getListaSentencias().get(node.getListaSentencias().size()-1) instanceof SenReturn)) 
		//	sh.code("RET "+ localRetBytes +", " + localDecBytes + ", " + localParamBytes);
		return param;
	}


	/**
	 * Los parámetros se pasan por valor.
	 */
	@Override
	public Object visit(SenInvocacion node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		for(Expresion expr : node.getListaArgumentos())
			expr.accept(valorVisitor, null);
		sh.code("CALL " + node.getNombre());
		if(node.getInvocacion().getTipo().getBytes() > 0)
			sh.code("POP");
		return param;
	}

	@Override
	public Object visit(SenAsignacion node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		node.getLeft().accept(direccionVisitor, null);
		node.getRight().accept(valorVisitor, null);
		sh.code("STORE" + sh.getSubfijo(node.getRight().getTipo()));
		return param;
	}
	
	@Override
	public Object visit(SenPrint node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		node.getExpresion().accept(valorVisitor, null);
		sh.code("OUT" + sh.getSubfijo(node.getExpresion().getTipo()));
		return param;
	}

	@Override
	public Object visit(SenPrintSp node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		node.getExpresion().accept(valorVisitor, null);
		sh.code("OUT" + sh.getSubfijo(node.getExpresion().getTipo()));
		sh.code("PUSHb 32");
		sh.code("OUTb");
		return param;
	}

	/**
	 * El nodo SenPrintLn, imprimirá un salto de linea, además también imprimirá una expresión antes del salto de linea
	 * si la expresión existe.
	 */
	@Override
	public Object visit(SenPrintLn node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		if(node.getExpresion() != null){
			node.getExpresion().accept(valorVisitor, null);
			sh.code("OUT" + sh.getSubfijo(node.getExpresion().getTipo()));
		}
		sh.code("PUSHb 10");
		sh.code("OUTb");
		return param;
	}

	@Override
	public Object visit(SenRead node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		node.getExpresion().accept(direccionVisitor, null);
		sh.code("IN" + sh.getSubfijo(node.getExpresion().getTipo()));
		sh.code("STORE" + sh.getSubfijo(node.getExpresion().getTipo()));
		return param;
	}

	@Override
	public Object visit(SenIf node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		node.getCondicion().accept(valorVisitor, param);
		int i = newLabel();
		/*
		 * Si existe un else, vamos al else, sino al final del if.
		 */
		if(node.existElse())
			sh.code("JZ else"+ i);
		else
			sh.code("JZ finIf"+ i);
		
		for(Sentencia sen : node.getSentenciasIf()) //Recorremos sentencias del If.
			sen.accept(this, param);
		/*
		 * Si existe un else, aquí vamos al final del If (despues del else.) porque acabamos el código del if.
		 * Si no existe, cremaos aquí la etiqueta finelse.
		 */
		if(node.existElse())
			sh.code("JMP finIf"+ i);
		else
			sh.codeLabel("finIf"+ i);
		/*
		 * Si existe un else, creamos la etiqueta else, recorremos sus sentencias y creamos la etiqueta finIf al final.
		 */
		if(node.existElse()){
			sh.codeLabel("else"+i);
			for(Sentencia sen : node.getSentenciasElse())
				sen.accept(this, param);
			sh.codeLabel("finIf"+i);
		}
		return param;
	}

	@Override
	public Object visit(SenWhile node, Object param) {
		sh.meta("#line " + node.getStart().getLine());
		int i = newLabel();
		sh.codeLabel("while"+ i);
		node.getCondicion().accept(valorVisitor, param);
		sh.code("JZ finWhile"+ i);
		for(Sentencia sen : node.getSentencias())
			sen.accept(this, param);
		sh.code("JMP while"+ i);
		sh.codeLabel("finWhile"+i);
		return param;
	}

	@Override
	public Object visit(SenReturn node, Object param) {
		sh.meta("#line " + node.getStart().getLine()); //Si no hay expresion, hace falta setPositions en el sintactico
		if(node.getRetorno() != null){
			node.getRetorno().accept(valorVisitor,param);
		}
		sh.meta("#RET " + node.getHisFunction().getTipo().getNombreTipo());
		sh.code("RET " + localRetBytes + ", "+ localDecBytes + ", "+ localParamBytes );
		return param;
	}


	/*
	 * De aquí sacamos el contador para añadir a las etiquetas y que no se repitan
	 */
	private int newLabel(){
		line++;
		return line;
	}

	private Visitor valorVisitor;
	private Visitor direccionVisitor;
	public static int line = -1;
	
}
