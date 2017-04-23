/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AST;
import ast.declaraciones.DefStruct;
/**
 * 
 * @author AdriánBueno
 *
 */
public interface Tipo extends AST {
	
	public int getBytes();
	public void setBytes(int bytes);
	
	/* Nombre del tipo, esencialmente para el tipo Struct
	 * */
	public String getNombreTipo();
	public void setNombreTipo(String nombre);
	
	/*
	 * Definición del tipo, en caso de Tipo variable su struct
	 * */
	public DefStruct getDefinicion();
	public void setDefinicion(DefStruct struct);

}

