/**
 * @generated VGen 1.3.3
 */

package ast;

import ast.expresiones.literales.Literal;
/**
 * 
 * @author AdriánBueno
 *
 */
public abstract class AbstractLiteral extends AbstractExpresion implements Literal {
	
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	protected String valor;

}

