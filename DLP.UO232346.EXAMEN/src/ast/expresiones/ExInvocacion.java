/**
 * @generated VGen 1.3.3
 */

package ast.expresiones;

import java.util.List;

import ast.AbstractExpresion;
import ast.Token;
import ast.declaraciones.DefFuncion;
import visitor.Visitor;

//	ExInvocacion:Expresion -> nombre:String  listaArgumentos:Expresion*
/**
 * 
 * @author AdriánBueno
 *
 */
public class ExInvocacion extends AbstractExpresion {

	public ExInvocacion(String nombre, List<Expresion> listaArgumentos) {
		this.nombre = nombre;
		this.listaArgumentos = listaArgumentos;

		searchForPositions(listaArgumentos);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public ExInvocacion(Object nombre, Object listaArgumentos) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.listaArgumentos = (List<Expresion>) listaArgumentos;

		searchForPositions(nombre, listaArgumentos);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public DefFuncion getInvocacion() {
		return invocacion;
	}

	public void setInvocacion(DefFuncion invocacion) {
		this.invocacion = invocacion;
	}

	public List<Expresion> getListaArgumentos() {
		return listaArgumentos;
	}
	public void setListaArgumentos(List<Expresion> listaArgumentos) {
		this.listaArgumentos = listaArgumentos;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String nombre;
	private DefFuncion invocacion;
	private List<Expresion> listaArgumentos;
}

