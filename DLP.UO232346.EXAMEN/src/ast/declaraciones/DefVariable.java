/**
 * @generated VGen 1.3.3
 */

package ast.declaraciones;

import ast.AbstractDeclaracion;
import ast.Token;
import ast.tipos.Tipo;
import visitor.*;

//	DefVariable:Declaracion -> nombre:String  tipo:Tipo

public class DefVariable extends AbstractDeclaracion {
	
	

	public DefVariable(String nombre, Tipo tipo) {
		this.nombre = nombre;
		setTipo(tipo);
		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public DefVariable(Object nombre, Object tipo) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		setTipo((Tipo)tipo);

		searchForPositions(nombre, tipo);	// Obtener linea/columna a partir de los hijos
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	

	
	
}


