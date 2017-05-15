/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import ast.expresiones.Expresion;
import visitor.Visitor;

//	SentenciaPrintLn:Sentencia -> expresion:Expresion
/**
 * PrintLn admite expresiones nulas, en ese caso solo deberá imprimir el salto de linea.
 * Se le debe hacer un .setPositions() desde el sintáctico en ese caso.
 * @author AdriánBueno
 *
 */
public class SenPrintLn extends SenPrint {

	public SenPrintLn(Expresion expresion) {
		this.expresion = expresion;
		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public SenPrintLn(Object expresion) {
		if(expresion != null)
			this.expresion = (Expresion)expresion;
		else
			this.expresion = null;
		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

