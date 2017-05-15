/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import ast.AST;
import ast.declaraciones.DefFuncion;
/**
 * 
 * @author Adri�nBueno
 *
 */
public interface Sentencia extends AST {
	
	/**
	 * Este m�todo se encarga de obtener una referencia a la funci�n donde se encuentra la Sentencia.
	 * @return DefFuncion Funci�n d�nde se encuentra la sentencia.
	 */
	public DefFuncion getHisFunction();
	/**
	 * Este m�todo se encarga de fijar la referencia de la funci�n donde se encuentra la sentencia.
	 * (Deber�a ser fijada en la fase de identificaci�n.)
	 * @param definicion DefFuncion nodo padre o antepasado en caso del while e if de la sentencia.
	 */
	public void setHisFunction(DefFuncion definicion);

}

