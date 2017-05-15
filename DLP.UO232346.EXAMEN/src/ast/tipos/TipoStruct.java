/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AbstractTipo;
import ast.Token;
import visitor.Visitor;

//	TipoStruct:Tipo -> nombre:String
/**
 * 
 * @author AdriánBueno
 *
 */
public class TipoStruct extends AbstractTipo {

	public TipoStruct(String nombre) {
		this.nombreTipo = nombre;
		//Los bytes los setea DefStruct
	}

	public TipoStruct(Object nombre) {
		this.nombreTipo = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;

		searchForPositions(nombre);	// Obtener linea/columna a partir de los hijos
	}
	

	@Override
	public int getBytes() {
		if(this.definicion != null)
			return definicion.getBytes();
		else
			return 0;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
}

