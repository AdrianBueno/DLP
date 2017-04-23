/**
 * @generated VGen 1.3.3
 */

package ast.sentencias;

import java.util.*;

import ast.AbstractSentencia;
import ast.expresiones.Expresion;
import visitor.*;

//	SentenciaIf:Sentencia -> condicion:Expresion  sentenciasIf:Sentencia*  sentenciasElse:Sentencia*
/**
 * La sentencia If tiene 2 modalidades, con o sin Else.
 * Si el valor que le llega al constructor tiene un valor Null, en la lista de sentencias Else.
 * existElse() retornará false.
 * Esto podría hacerse con una lista vacía en vez de un valor null, pero he preferido hacerlo así para disntiguir
 * un if sin else a un if con else y sin sentencias dentro.
 * Asi no genero etiquetas de mas en la generación de código.
 * 
 * @author AdriánBueno
 *
 */
public class SenIf extends AbstractSentencia {

	public SenIf(Expresion condicion, List<Sentencia> sentenciasIf, List<Sentencia> sentenciasElse) {
		this.condicion = condicion;
		this.sentenciasIf = sentenciasIf;
		this.sentenciasElse = sentenciasElse;
		if(sentenciasElse == null){
			existElse = false;
			this.sentenciasElse = new ArrayList<Sentencia>();
		}else{
			existElse = true;
			this.sentenciasElse = (List<Sentencia>) sentenciasElse;
		}
		searchForPositions(condicion, sentenciasIf, sentenciasElse);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public SenIf(Object condicion, Object sentenciasIf, Object sentenciasElse) {
		this.condicion = (Expresion) condicion;
		this.sentenciasIf = (List<Sentencia>) sentenciasIf;
		if(sentenciasElse == null){
			existElse = false;
			this.sentenciasElse = new ArrayList<Sentencia>();
		}else{
			existElse = true;
			this.sentenciasElse = (List<Sentencia>) sentenciasElse;
		}
		searchForPositions(condicion, sentenciasIf, sentenciasElse);	// Obtener linea/columna a partir de los hijos
	}
	

	public Expresion getCondicion() {
		return condicion;
	}
	public void setCondicion(Expresion condicion) {
		this.condicion = condicion;
	}

	public List<Sentencia> getSentenciasIf() {
		return sentenciasIf;
	}
	public void setSentenciasIf(List<Sentencia> sentenciasIf) {
		this.sentenciasIf = sentenciasIf;
	}

	public List<Sentencia> getSentenciasElse() {
		return sentenciasElse;
	}
	public void setSentenciasElse(List<Sentencia> sentenciasElse) {
		this.sentenciasElse = sentenciasElse;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	
	public boolean existElse(){
		return existElse;
	}

	private Expresion condicion;
	private List<Sentencia> sentenciasIf;
	private List<Sentencia> sentenciasElse;
	private boolean existElse;
}

