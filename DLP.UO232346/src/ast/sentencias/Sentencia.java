/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import ast.AST;
import ast.declaraciones.DefFuncion;
/**
 * 
 * @author AdriánBueno
 *
 */
public interface Sentencia extends AST {
	
	/**
	 * Este método se encarga de obtener una referencia a la función donde se encuentra la Sentencia.
	 * @return DefFuncion Función dónde se encuentra la sentencia.
	 */
	public DefFuncion getHisFunction();
	/**
	 * Este método se encarga de fijar la referencia de la función donde se encuentra la sentencia.
	 * (Debería ser fijada en la fase de identificación.)
	 * @param definicion DefFuncion nodo padre o antepasado en caso del while e if de la sentencia.
	 */
	public void setHisFunction(DefFuncion definicion);

}

