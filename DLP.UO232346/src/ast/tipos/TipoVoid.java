/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AbstractTipo;
import visitor.*;

//	TipoVoid:Tipo -> 
/**
 * 
 * @author AdriánBueno
 *
 */
public class TipoVoid extends AbstractTipo {

	
	public TipoVoid() {
		this.bytes = 0;
		this.nombreTipo = "Void";
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

