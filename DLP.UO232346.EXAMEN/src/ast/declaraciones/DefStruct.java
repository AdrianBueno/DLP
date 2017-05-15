/**
 * @generated VGen 1.3.3
 */

package ast.declaraciones;

import java.util.List;

import ast.AbstractDeclaracion;
import ast.Token;
import visitor.Visitor;

//	DefStruct:Declaracion -> nombre:String  listaCampos:DefCampo*

public class DefStruct extends AbstractDeclaracion {

	public DefStruct(String nombre, List<DefVariable> listaCampos) {
		this.nombre = nombre;
		this.listaCampos = listaCampos;
		setAmbito(GLOBAL);
		//setTipo(new TipoStruct(nombre));
		//calcularBytes();
		searchForPositions(listaCampos);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefStruct(Object nombre, Object listaCampos) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.listaCampos = (List<DefVariable>) listaCampos;
		setAmbito(GLOBAL);
		//setTipo(new TipoStruct(nombre));
		//calcularBytes();
		searchForPositions(nombre, listaCampos);	// Obtener linea/columna a partir de los hijos
	}
	
	public int getBytes(){
		int suma = 0;
		for(DefVariable campo : listaCampos)
			suma += campo.getTipo().getBytes();
		return suma;
	}


	public List<DefVariable> getListaCampos() {
		return listaCampos;
	}
	public void setListaCampos(List<DefVariable> listaCampos) {
		this.listaCampos = listaCampos;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private List<DefVariable> listaCampos;
	
}

