/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AbstractTipo;
import visitor.*;

//	TipoEntero:Tipo -> 
/**
 * 
 * @author AdriánBueno
 *
 */
public class TipoEntero extends AbstractTipo {

	
	
	public TipoEntero() {
		this.bytes = 2;
		this.nombreTipo = "int";
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

