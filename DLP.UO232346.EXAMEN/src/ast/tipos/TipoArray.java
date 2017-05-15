/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AbstractTipo;
import ast.Token;
import visitor.*;

//	TipoArray:Tipo -> tipo:Tipo  tamaño:String
/**
 * 
 * @author AdriánBueno
 *
 */
public class TipoArray extends AbstractTipo {

	public TipoArray(Tipo tipo, String tamaño) {
		this.tipoBase = tipo;
		String temp = tamaño;
		this.arraySize = Integer.parseInt(temp);
		this.nombreTipo = this.arraySize+" * " + this.tipoBase.getNombreTipo();
		this.bytes = this.tipoBase.getBytes() * this.arraySize;
		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public TipoArray(Object tipo, Object tamaño) {
		this.tipoBase = (Tipo) tipo;
		String temp = (tamaño instanceof Token) ? ((Token)tamaño).getLexeme() : (String) tamaño;
		this.arraySize = Integer.parseInt(temp);
		this.bytes = this.tipoBase.getBytes() * this.arraySize;
		this.nombreTipo = this.arraySize+" * " + this.tipoBase.getNombreTipo();
		searchForPositions(tipo, tamaño);	// Obtener linea/columna a partir de los hijos
	}

	public Tipo getTipoBase() {
		return tipoBase;
	}
	public void setTipoBase(Tipo tipo) {
		this.tipoBase = tipo;
	}
	

	@Override
	public int getBytes() {
		return this.tipoBase.getBytes() * this.arraySize;
	}

	public int getArraySize() {
		return arraySize;
	}
	public void setArraySize(int arraySize) {
		this.arraySize = arraySize;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Tipo tipoBase;
	private int arraySize;
}

