package semantico;

import java.util.*;
/*
 * Implementaci�n de una tabla Hash con contextos.
 * Permite:
 * - Insertar s�mbolos (put) en el contexto actual.
 * - Buscar tanto en el contexto actual (getFromTop) como en todos los contextos (getFromAny).
 * - Crear y destruir contextos mediante las operaciones set y reset.
 * 
 * La forma habitual de instanciarla ser�:
 * 	ContextMap<String, DefinicionVariable> variables = new ContextMap<String, DefinicionVariable>();
 * 
 */
public class ContextMap<S, D> {

	public ContextMap() {
		set();
	}

	public void put(S nombre, D def) {
		contextos.peek().put(nombre, def);
	}

	public D getFromTop(S nombre) {
		return contextos.peek().get(nombre);
	}

	//De abajo hac�a arriba!
	public D getFromAny(S nombre) {
		for (int i = contextos.size() - 1; i >= 0; i--) {
			Map<S, D> contexto = contextos.get(i);
			D def = contexto.get(nombre);
			if (def != null)
				return def;
		}
		return null;
	}

	public void set() {
		contextos.push(new HashMap<S, D>());
	}
	public void set(Map<S,D> map){
		contextos.push(map);
	}

	public void reset() {
		contextos.pop();
	}
	public Stack<Map<S, D>> getContextos(){
		return this.contextos;
	}

	private Stack<Map<S, D>> contextos = new Stack<Map<S, D>>();
}
