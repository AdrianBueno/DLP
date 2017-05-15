/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.Token;
import visitor.*;

//	ExLogica:Expresion -> left:Expresion  operador:String  right:Expresion
/**
 * 
 * @author AdriánBueno
 *
 */
public class ExLogica extends ExBinaria {

	public ExLogica(Expresion left, String operador, Expresion right) {
		this.left = left;
		this.operador = operador;
		this.right = right;

		searchForPositions(left, right);	// Obtener linea/columna a partir de los hijos
	}

	public ExLogica(Object left, Object operador, Object right) {
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

