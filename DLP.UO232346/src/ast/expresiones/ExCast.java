/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.AbstractExpresion;
import ast.tipos.Tipo;
import visitor.*;

//	ExCast:Expresion -> tipo:Tipo  cast:Expresion
/**
 * 
 * @author AdriánBueno
 *
 */
public class ExCast extends AbstractExpresion {

	public ExCast(Tipo to, Expresion from) {
		this.to = to;
		this.from = from;

		searchForPositions(to, from);	// Obtener linea/columna a partir de los hijos
	}

	public ExCast(Object to, Object from) {
		this.to = (Tipo) to;
		this.from = (Expresion) from;

		searchForPositions(to, from);	// Obtener linea/columna a partir de los hijos
	}

	public Tipo getTo() {
		return to;
	}
	public void setTo(Tipo to) {
		this.to = to;
	}

	public Expresion getFrom() {
		return from;
	}
	public void setfrom(Expresion cast) {
		this.from = cast;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Tipo to;
	private Expresion from;
}

