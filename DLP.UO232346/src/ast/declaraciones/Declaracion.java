/**
 * @generated VGen 1.3.3
 */

package ast.declaraciones;

import ast.AST;
import ast.tipos.Tipo;

public interface Declaracion extends AST {
	
	public Tipo getTipo();
	public void setTipo(Tipo tipo);
	public int getAddress();
	public void setAddress(int address);
	public int getAmbito();
	public void setAmbito(int ambito);

}

