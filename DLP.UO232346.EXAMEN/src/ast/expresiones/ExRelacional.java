/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.Token;
import visitor.*;

//	ExRelacional:Expresion -> left:Expresion  operador:String  right:Expresion
/**
 * 
 * @author AdriánBueno
 *
 */
public class ExRelacional extends ExBinaria {

	public ExRelacional(Expresion left, String operador, Expresion right) {
		this.left = left;
		this.operador = operador;
		this.right = right;

		searchForPositions(left, right);	// Obtener linea/columna a partir de los hijos
	}

	public ExRelacional(Object left, Object operador, Object right) {
		this.left = (Expresion) left;
		this.operador = (operador instanceof Token) ? ((Token)operador).getLexeme() : (String) operador;
		this.right = (Expresion) right;

		searchForPositions(left, operador, right);	// Obtener linea/columna a partir de los hijos
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

