/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import ast.AbstractSentencia;
import ast.expresiones.Expresion;
import visitor.*;

//	SentenciaPrint:Sentencia -> expresion:Expresion
/**
 * 
 * @author AdriánBueno
 *
 */
public class SenPrint extends AbstractSentencia {

	public SenPrint(Expresion expresion) {
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public SenPrint(Object expresion) {
		this.expresion = (Expresion) expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}
	public SenPrint(){}

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

	protected Expresion expresion;
}

