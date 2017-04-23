/**
 * @generated VGen 1.3.3
 */

package ast;

import ast.declaraciones.Declaracion;
import ast.tipos.Tipo;
/**
 * 
 * @author AdriánBueno
 *
 */
public abstract class AbstractDeclaracion extends AbstractTraceable implements Declaracion {
	
	public static final int GLOBAL = 0;
	public static final int LOCAL = 1;
	public static final int CAMPO = 2;
	
	protected String nombre;
	protected Tipo tipo;
	protected int address;
	protected int ambito;
	
	public String getNombre(){
		return nombre;
	}
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	public Tipo getTipo(){
		return tipo;
	}
	public void setTipo(Tipo tipo){
		this.tipo = tipo;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public int getAmbito() {
		return ambito;
	}
	public void setAmbito(int ambito) {
		this.ambito = ambito;
	}

}

