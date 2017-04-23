/**
 * @generated VGen 1.3.3
 */

package ast;

import ast.expresiones.Expresion;
import ast.tipos.Tipo;
/**
 * 
 * @author AdriánBueno
 *
 */
public abstract class AbstractExpresion extends AbstractTraceable implements Expresion {
	
	public boolean isModificable(){
		return modificable;
	}
	
	public void setModificable(boolean modificable){
		this.modificable = modificable;
	}
	
	public Tipo getTipo(){
		return this.tipo;
	}
	
	public void setTipo(Tipo tipo){
		this.tipo = tipo;
	}
	
	protected boolean modificable;
	protected Tipo tipo;
}

