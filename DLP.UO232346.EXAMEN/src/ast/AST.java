/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;
/**
 * 
 * @author Adri�nBueno
 *
 */
public interface AST extends Traceable {
	public Object accept(Visitor visitor, Object param);
}

