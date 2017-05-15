/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import ast.AbstractExpresion;
import ast.Token;
import ast.declaraciones.DefVariable;
import ast.tipos.Tipo;
import visitor.Visitor;

//	Variable:Expresion -> nombre:String
/**
 * 
 * @author AdriánBueno
 *
 */
public class Variable extends AbstractExpresion {

	public Variable(String nombre) {
		setModificable(true);
		this.nombre = nombre;
		
	}

	public Variable(Object nombre) {
		setModificable(true);
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;

		searchForPositions(nombre);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public DefVariable getDeclaracion() {
		return declaracion;
	}

	public void setDeclaracion(DefVariable definicion) {
		this.declaracion = definicion;
	}

	@Override
	public Object accept(Visitor v, Object param) {
		return v.visit(this, param);
	}
	

	@Override
	public void setTipo(Tipo tipo) {
		if(declaracion != null)
			this.tipo = tipo;
		else
			throw new IllegalStateException("No se puede asignar el tipo a esta variable porque no se ha definido su declaración");
	}
	
	



	@Override
	public Tipo getTipo() {
		// TODO Auto-generated method stub
		return super.getTipo();
	}





	private String nombre;
	private DefVariable declaracion;
}

