/**
 * @generated VGen 1.3.3
 */

package ast.expresiones.literales;

import ast.AbstractLiteral;
import ast.Token;
import visitor.*;

//	LitReal:Expresion, Literal -> valor:String
/**
 * 
 * @author AdriánBueno
 *
 */
public class LitReal extends AbstractLiteral {

	public LitReal(String valor) {
		this.valor = valor;
	}

	public LitReal(Object valor) {
		this.valor = (valor instanceof Token) ? ((Token)valor).getLexeme() : (String) valor;

		searchForPositions(valor);	// Obtener linea/columna a partir de los hijos
	}
	
	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

