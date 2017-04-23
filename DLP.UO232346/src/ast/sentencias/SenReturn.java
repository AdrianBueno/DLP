/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import ast.AbstractSentencia;
import ast.expresiones.Expresion;
import visitor.*;

//	SentenciaReturn:Sentencia -> retorno:Expresion
/**
 * SenReturn puede tener una expresión nula, en este caso se le hace un .setPositions desde el sintáctico.
 * @author AdriánBueno
 *
 */
public class SenReturn extends AbstractSentencia {

	public SenReturn(Expresion retorno) {
		this.retorno = retorno;

		searchForPositions(retorno);	// Obtener linea/columna a partir de los hijos
	}

	public SenReturn(Object retorno) {
		this.retorno = (Expresion) retorno;

		searchForPositions(retorno);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getRetorno() {
		return retorno;
	}
	public void setRetorno(Expresion retorno) {
		this.retorno = retorno;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion retorno;
}

