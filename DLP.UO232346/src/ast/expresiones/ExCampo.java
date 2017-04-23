/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.AbstractExpresion;
import visitor.*;

//	ExCampo:Expresion -> left:Expresion  campo:Expresion
/**
 * 
 * @author AdriánBueno
 *
 */
public class ExCampo extends AbstractExpresion {

	public ExCampo(Expresion struct, Variable campo) {
		this.struct = struct;
		this.campo = campo;

		searchForPositions(struct, campo);	// Obtener linea/columna a partir de los hijos
	}

	public ExCampo(Object struct, Object campo) {
		this.struct = (Expresion) struct;
		this.campo = (Variable) campo;

		searchForPositions(struct, campo);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getStruct() {
		return struct;
	}
	public void setStruct(Expresion struct) {
		this.struct = struct;
	}

	public Variable getCampo() {
		return campo;
	}
	public void setCampo(Variable campo) {
		this.campo = campo;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion struct;
	private Variable campo;
}

