/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.AST;
import ast.tipos.Tipo;
/**
 * 
 * @author AdriánBueno
 *
 */
public interface Expresion extends AST {
	/**
	 * Este método proporciona el tipo de la expresión
	 * @return Tipo
	 */
	public Tipo getTipo();
	/**
	 * Este método fija el tipo de la expresión
	 * @param tipo Tipo
	 */
	public void setTipo(Tipo tipo);
	/**
	 * Este método obtiene un valor booleano que indica la modificabilidad de la expresion.
	 * (solo las variables [Acceso y campos] deberian ser True)
	 * @return Boolean
	 */
	public boolean isModificable();
	/**
	 * Este método fija la modificabilidad de la expresion.
	 * @param modificable Boolean
	 */
	public void setModificable(boolean modificable);
}

