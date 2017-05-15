/**
 * @generated VGen 1.3.3
 */

package ast;

import ast.declaraciones.DefStruct;
import ast.tipos.Tipo;
import ast.tipos.TipoCaracter;
import ast.tipos.TipoEntero;
import ast.tipos.TipoReal;
import ast.tipos.TipoVoid;
/**
 * 
 * @author AdriánBueno
 *
 */
public abstract class AbstractTipo extends AbstractTraceable implements Tipo {

	private static TipoEntero entero;
	private static TipoReal real;
	private static TipoCaracter caracter;
	private static TipoVoid tipoVoid;
	
	public static TipoEntero getTipoEntero() {
		if(entero == null)
			entero = new TipoEntero();
		return entero;
	}

	public static TipoReal getTipoReal() {
		if(real == null)
			real = new TipoReal();
		return real;
	}

	public static TipoCaracter getTipoCaracter() {
		if(caracter == null)
			caracter = new TipoCaracter();
		return caracter;
	}
	
	public static TipoVoid getTipoVoid(){
		if(tipoVoid == null)
			tipoVoid = new TipoVoid();
		return tipoVoid;
	}
	

	@Override
	public DefStruct getDefinicion() {
		return this.definicion;
	}

	@Override
	public void setDefinicion(DefStruct struct) {
		this.definicion = struct;
	}

	public int getBytes(){
		return this.bytes;
	}
	
	public void setBytes(int bytes){
		this.bytes = bytes;
	}
	
	public String getNombreTipo() {
		return this.nombreTipo;
	}	

	public void setNombreTipo(String nombre) {
		this.nombreTipo = nombre;
	}

	protected int bytes;
	protected String nombreTipo;
	protected DefStruct definicion; //Definición del tipo si la hay
	
}
