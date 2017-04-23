/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.AbstractExpresion;
import visitor.*;

//	ExBinaria:Expresion -> 
/**
 * De esta clase heredan las expresiones binarias.
 * No aparece como nodo.
 * @author AdrianBueno
 *
 */
public  class ExBinaria extends AbstractExpresion {
	

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	
	public Expresion getLeft() {
		return left;
	}
	public void setLeft(Expresion left) {
		this.left = left;
	}

	public String getOperador() {
		return operador;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}

	public Expresion getRight() {
		return right;
	}
	public void setRight(Expresion right) {
		this.right = right;
	}

	protected Expresion left;
	protected String operador;
	protected Expresion right;

}

