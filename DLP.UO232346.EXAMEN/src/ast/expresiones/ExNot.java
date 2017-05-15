/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.AbstractExpresion;
import visitor.*;

//	ExNot:Expresion -> expresion:Expresion
/**
 * 
 * @author AdriánBueno
 *
 */
public class ExNot extends AbstractExpresion {

	public ExNot(Expresion expresion) {
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public ExNot(Object expresion) {
		this.expresion = (Expresion) expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresion() {
		return expresion;
	}
	public void setExpresion(Expresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion expresion;
}

