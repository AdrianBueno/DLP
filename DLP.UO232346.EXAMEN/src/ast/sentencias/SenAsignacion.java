/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import java.util.List;

import visitor.Visitor;
import ast.AbstractSentencia;
import ast.expresiones.Expresion;

//	Asignacion:Sentencia -> left:Expresion  right:Expresion

/**
 * 
 * @author AdriánBueno
 *
 */
public class SenAsignacion extends AbstractSentencia {

	public SenAsignacion(Expresion left, Expresion right) {
		this.left = left;
		this.right = right;

		searchForPositions(left, right);	// Obtener linea/columna a partir de los hijos
	}
	public SenAsignacion(List<Expresion> left, List<Expresion> right) {
		this.lefts = left;
		this.rights = right;

		searchForPositions(left, right);	// Obtener linea/columna a partir de los hijos
	}

	public SenAsignacion(Object left, Object right) {
		//this.left = (Expresion) left;
		//this.right = (Expresion) right;
		this.lefts = (List<Expresion>) left;
		this.rights = (List<Expresion>) right;
		searchForPositions(left, right);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getLeft() {
		return left;
	}
	public void setLeft(Expresion left) {
		this.left = left;
	}

	public Expresion getRight() {
		return right;
	}
	public void setRight(Expresion right) {
		this.right = right;
	}
	

	public List<Expresion> getLefts() {
		return lefts;
	}

	public void setLefts(List<Expresion> lefts) {
		this.lefts = lefts;
	}

	public List<Expresion> getRights() {
		return rights;
	}

	public void setRights(List<Expresion> rights) {
		this.rights = rights;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion left;
	private Expresion right;
	private List<Expresion> lefts;
	private List<Expresion> rights;
}

