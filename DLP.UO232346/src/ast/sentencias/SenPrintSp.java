/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import ast.expresiones.Expresion;
import visitor.Visitor;

//	SentenciaPrintSp:Sentencia -> expresion:Expresion
/**
 * 
 * @author AdriánBueno
 *
 */
public class SenPrintSp extends SenPrint {

	public SenPrintSp(Expresion expresion) {
		super(expresion);
	}

	public SenPrintSp(Object expresion) {
		super(expresion);
	}



	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

