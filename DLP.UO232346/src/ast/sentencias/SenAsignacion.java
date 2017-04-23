/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import ast.AbstractSentencia;
import ast.expresiones.Expresion;
import visitor.*;

//	Asignacion:Sentencia -> left:Expresion  right:Expresion

/**
 * 
 * @author AdriánBueno
 *
 */
public class SenAsignacion extends AbstractSentencia {

	public SenAsignacion(Expresion left, Expresion right) {
		this.left = left;
		this.right = right;

		searchForPositions(left, right);	// Obtener linea/columna a partir de los hijos
	}

	public SenAsignacion(Object left, Object right) {
		this.left = (Expresion) left;
		this.right = (Expresion) right;
		searchForPositions(left, right);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getLeft() {
		return left;
	}
	public void setLeft(Expresion left) {
		this.left = left;
	}

	public Expresion getRight() {
		return right;
	}
	public void setRight(Expresion right) {
		this.right = right;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion left;
	private Expresion right;
}

