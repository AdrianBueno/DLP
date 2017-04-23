/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AbstractTipo;
import visitor.*;

//	TipoCaracter:Tipo -> 
/**
 * 
 * @author AdriánBueno
 *
 */
public class TipoCaracter extends AbstractTipo {

	public TipoCaracter() {
		this.bytes = 1;
		this.nombreTipo = "char";
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

}

