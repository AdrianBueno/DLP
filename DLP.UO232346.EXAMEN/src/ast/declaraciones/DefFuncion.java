/**
 * @generated VGen 1.3.3
 */

package ast.declaraciones;

import java.util.*;

import ast.AbstractDeclaracion;
import ast.Token;
import ast.sentencias.Sentencia;
import ast.tipos.Tipo;
import visitor.*;

//	DefFuncion:Declaracion -> nombre:String  listaParametros:DefVariable*  tipo:Tipo  listaDeclaraciones:DefVariable*  listaSentencias:Sentencia*

public class DefFuncion extends AbstractDeclaracion {

	public DefFuncion(String nombre, List<DefVariable> listaParametros, Tipo tipo, List<DefVariable> listaDeclaraciones, List<Sentencia> listaSentencias) {
		this.nombre = nombre;
		this.listaParametros = listaParametros;
		setTipo(tipo);
		setAmbito(GLOBAL);
		this.listaDeclaraciones = listaDeclaraciones;
		this.listaSentencias = listaSentencias;
		searchForPositions(listaParametros, tipo, listaDeclaraciones, listaSentencias);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefFuncion(Object nombre, Object listaParametros, Object tipo, Object listaDeclaraciones, Object listaSentencias) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.listaParametros = (List<DefVariable>) listaParametros;
		setTipo((Tipo) tipo);
		setAmbito(GLOBAL);
		this.listaDeclaraciones = (List<DefVariable>) listaDeclaraciones;
		this.listaSentencias = (List<Sentencia>) listaSentencias;
		searchForPositions(nombre, listaParametros, tipo, listaDeclaraciones, listaSentencias);	// Obtener linea/columna a partir de los hijos
	}

	public List<DefVariable> getListaParametros() {
		return listaParametros;
	}
	public void setListaParametros(List<DefVariable> listaParametros) {
		this.listaParametros = listaParametros;
	}

	public List<DefVariable> getListaDeclaraciones() {
		return listaDeclaraciones;
	}
	public void setListaDeclaraciones(List<DefVariable> listaDeclaraciones) {
		this.listaDeclaraciones = listaDeclaraciones;
	}

	public List<Sentencia> getListaSentencias() {
		return listaSentencias;
	}
	public void setListaSentencias(List<Sentencia> listaSentencias) {
		this.listaSentencias = listaSentencias;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private List<DefVariable> listaParametros;
	private List<DefVariable> listaDeclaraciones;
	private List<Sentencia> listaSentencias;
}

