/**
 * @generated VGen 1.3.3
 */

package ast;

import ast.declaraciones.DefFuncion;
import ast.sentencias.Sentencia;
/**
 * 
 * @author AdriánBueno
 *
 */
public abstract class AbstractSentencia extends AbstractTraceable implements Sentencia {
	
	public DefFuncion getHisFunction(){
		return this.hisFunction;
	}
	
	public void setHisFunction(DefFuncion definicion){
		this.hisFunction =  definicion;
	}
	
	private DefFuncion hisFunction;
	
}

