/**
 * @generated VGen 1.3.3
 */

package ast.tipos;

import ast.AbstractTipo;
import ast.Token;
import visitor.*;

//	TipoArray:Tipo -> tipo:Tipo  tama�o:String
/**
 * 
 * @author Adri�nBueno
 *
 */
public class TipoArray extends AbstractTipo {

	public TipoArray(Tipo tipo, String tama�o) {
		this.tipoBase = tipo;
		String temp = tama�o;
		this.arraySize = Integer.parseInt(temp);
		this.nombreTipo = this.arraySize+" * " + this.tipoBase.getNombreTipo();
		this.bytes = this.tipoBase.getBytes() * this.arraySize;
		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public TipoArray(Object tipo, Object tama�o) {
		this.tipoBase = (Tipo) tipo;
		String temp = (tama�o instanceof Token) ? ((Token)tama�o).getLexeme() : (String) tama�o;
		this.arraySize = Integer.parseInt(temp);
		this.bytes = this.tipoBase.getBytes() * this.arraySize;
		this.nombreTipo = this.arraySize+" * " + this.tipoBase.getNombreTipo();
		searchForPositions(tipo, tama�o);	// Obtener linea/columna a partir de los hijos
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

