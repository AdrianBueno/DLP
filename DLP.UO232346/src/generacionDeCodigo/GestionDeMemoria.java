package generacionDeCodigo;

import ast.AbstractDeclaracion;
import ast.Programa;
import ast.declaraciones.Declaracion;
import ast.declaraciones.DefFuncion;
import ast.declaraciones.DefStruct;
import ast.declaraciones.DefVariable;
import visitor.DefaultVisitor;

/** 
 * Clase encargada de asignar direcciones a las variables 
 */
public class GestionDeMemoria extends DefaultVisitor {
	
	private SourceHelper sh;
	
	public GestionDeMemoria(){
		sh = SourceHelper.getInstance();
	}
	
	@Override
	public Object visit(Programa node, Object param) {

		int sumaTamañoVariables = 0;

		for (Declaracion dec : node.getDeclaraciones()){
			dec.setAmbito(AbstractDeclaracion.GLOBAL);
			sumaTamañoVariables = (int)dec.accept(this, sumaTamañoVariables);
		}
		return sumaTamañoVariables;
	}

	@Override
	public Object visit(DefVariable node, Object param) {
		int ra = (int)param;
		node.setAddress(ra);
		sh.directivaDireccion(node);
		ra = ra + node.getTipo().getBytes();
		return ra;
	}

	@Override
	public Object visit(DefStruct node, Object param) {
		sh.meta("#TYPE " + node.getNombre()+":" + "{");
		int relativeStructAddr = 0;
		for(DefVariable defVar : node.getListaCampos()){
			sh.meta(defVar.getNombre() + ":" + defVar.getTipo().getNombreTipo());
			defVar.setAmbito(DefVariable.CAMPO);
			relativeStructAddr = (int)defVar.accept(this, relativeStructAddr);
		}
		sh.meta("}");
		return param;
	}

	@Override
	public Object visit(DefFuncion node, Object param) {
		sh.memoryLabel("Parametros de Función: " + node.getNombre());
		int relativeParamAddr = 4; //BP + 4
		for(int i = node.getListaParametros().size()-1; i >= 0; i--){
			node.getListaParametros().get(i).setAmbito(DefVariable.LOCAL);
			relativeParamAddr = (int)node.getListaParametros().get(i).accept(this, relativeParamAddr);
		}
		sh.memoryLabel("Locales de Función: " + node.getNombre());
		int relativeLocalAddr = 0;
		for(int i = node.getListaDeclaraciones().size()-1; i >= 0; i--)
			relativeLocalAddr -= node.getListaDeclaraciones().get(i).getTipo().getBytes();
		for(int i = node.getListaDeclaraciones().size()-1; i >= 0; i--){	
			node.getListaDeclaraciones().get(i).setAmbito(DefVariable.LOCAL);
			relativeLocalAddr = (int)node.getListaDeclaraciones().get(i).accept(this, relativeLocalAddr);
		}
		return param;
	}

}
