/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AbstractTipo;
import visitor.*;

//	TipoReal:Tipo -> 
/**
 * 
 * @author AdriánBueno
 *
 */
public class TipoReal extends AbstractTipo {

	
	public TipoReal() {
		this.bytes = 4;
		this.nombreTipo = "float";
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

