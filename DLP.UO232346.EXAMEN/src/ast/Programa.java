/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;

import ast.declaraciones.Declaracion;
import visitor.*;

//	Programa -> declaraciones:Declaracion*
/**
 * 
 * @author AdriánBueno
 *
 */
public class Programa extends AbstractTraceable implements AST {

	public Programa(List<Declaracion> declaraciones) {
		this.declaraciones = declaraciones;

		searchForPositions(declaraciones);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public Programa(Object declaraciones) {
		this.declaraciones = (List<Declaracion>) declaraciones;

		searchForPositions(declaraciones);	// Obtener linea/columna a partir de los hijos
	}

	public List<Declaracion> getDeclaraciones() {
		return declaraciones;
	}
	public void setDeclaraciones(List<Declaracion> declaraciones) {
		this.declaraciones = declaraciones;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private List<Declaracion> declaraciones;
}

