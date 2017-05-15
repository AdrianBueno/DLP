/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import java.util.List;

import ast.AbstractSentencia;
import ast.Token;
import ast.declaraciones.DefFuncion;
import ast.expresiones.Expresion;
import visitor.Visitor;

//	Invocacion:Sentencia, Expresion -> nombre:String  listaArgumentos:Expresion*
/**
 * 
 * @author AdriánBueno
 *
 */
public class SenInvocacion extends AbstractSentencia {

	public SenInvocacion(String nombre, List<Expresion> listaArgumentos) {
		this.nombre = nombre;
		this.listaArgumentos = listaArgumentos;

		searchForPositions(listaArgumentos);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public SenInvocacion(Object nombre, Object listaArgumentos) {
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

