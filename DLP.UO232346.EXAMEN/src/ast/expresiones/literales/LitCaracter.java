/**
 * @generated VGen 1.3.3
 */

package ast.expresiones.literales;

import ast.AbstractLiteral;
import ast.Token;
import visitor.*;

//	LitCaracter:Expresion, Literal -> valor:String
/**
 * 
 * @author AdriánBueno
 *
 */
public class LitCaracter extends AbstractLiteral {

	public LitCaracter(String valor) {
		this.valor = valor;
	}

	public LitCaracter(Object valor) {
		this.valor = (valor instanceof Token) ? ((Token)valor).getLexeme() : (String) valor;
		searchForPositions(valor);	// Obtener linea/columna a partir de los hijos
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
}

