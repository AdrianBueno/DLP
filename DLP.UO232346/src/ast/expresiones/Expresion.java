/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.AST;
import ast.tipos.Tipo;
/**
 * 
 * @author Adri�nBueno
 *
 */
public interface Expresion extends AST {
	/**
	 * Este m�todo proporciona el tipo de la expresi�n
	 * @return Tipo
	 */
	public Tipo getTipo();
	/**
	 * Este m�todo fija el tipo de la expresi�n
	 * @param tipo Tipo
	 */
	public void setTipo(Tipo tipo);
	/**
	 * Este m�todo obtiene un valor booleano que indica la modificabilidad de la expresion.
	 * (solo las variables [Acceso y campos] deberian ser True)
	 * @return Boolean
	 */
	public boolean isModificable();
	/**
	 * Este m�todo fija la modificabilidad de la expresion.
	 * @param modificable Boolean
	 */
	public void setModificable(boolean modificable);
}

