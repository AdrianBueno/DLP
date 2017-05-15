/**
 * @generated VGen 1.3.3
 */

package visitor;

import java.util.List;

import ast.AST;
import ast.Programa;
import ast.declaraciones.DefFuncion;
import ast.declaraciones.DefStruct;
import ast.declaraciones.DefVariable;
import ast.expresiones.ExAritmetica;
import ast.expresiones.ExBinaria;
import ast.expresiones.ExCampo;
import ast.expresiones.ExCast;
import ast.expresiones.ExIndice;
import ast.expresiones.ExInvocacion;
import ast.expresiones.ExLogica;
import ast.expresiones.ExNot;
import ast.expresiones.ExRelacional;
import ast.expresiones.Variable;
import ast.expresiones.literales.LitCaracter;
import ast.expresiones.literales.LitEntero;
import ast.expresiones.literales.LitReal;
import ast.sentencias.SenAsignacion;
import ast.sentencias.SenIf;
import ast.sentencias.SenInvocacion;
import ast.sentencias.SenPrint;
import ast.sentencias.SenPrintLn;
import ast.sentencias.SenPrintSp;
import ast.sentencias.SenRead;
import ast.sentencias.SenReturn;
import ast.sentencias.SenWhile;
import ast.tipos.TipoArray;
import ast.tipos.TipoCaracter;
import ast.tipos.TipoEntero;
import ast.tipos.TipoReal;
import ast.tipos.TipoStruct;
import ast.tipos.TipoVoid;

/*
DefaultVisitor. Implementación base del visitor para ser derivada por nuevos visitor.
	No modificar esta clase. Para crear nuevos visitor usar el fichero "_PlantillaParaVisitors.txt".
	DefaultVisitor ofrece una implementación por defecto de cada nodo que se limita a visitar los nodos hijos.
*/
public class DefaultVisitor implements Visitor {

	//	class Programa { List<Declaracion> declaraciones; }
	public Object visit(Programa node, Object param) {
		visitChildren(node.getDeclaraciones(), param);
		return null;
	}

	//	class DefVariable { String nombre;  Tipo tipo; }
	public Object visit(DefVariable node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	//	class DefStruct { String nombre;  List<DefCampo> listaCampos; }
	public Object visit(DefStruct node, Object param) {
		visitChildren(node.getListaCampos(), param);
		return null;
	}

	//	class DefFuncion { String nombre;  List<DefVariable> listaParametros;  Tipo tipo;  List<DefVariable> listaDeclaraciones;  List<Sentencia> listaSentencias; }
	public Object visit(DefFuncion node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		visitChildren(node.getListaParametros(), param);
		visitChildren(node.getListaDeclaraciones(), param);
		visitChildren(node.getListaSentencias(), param);
		return null;
	}
	
	//	class Invocacion { String nombre;  List<Expresion> listaArgumentos; }
	public Object visit(SenInvocacion node, Object param) {
		visitChildren(node.getListaArgumentos(), param);
		return null;
	}

	//	class Asignacion { Expresion left;  Expresion right; }
	public Object visit(SenAsignacion node, Object param) {
		visitChildren(node.getLefts(), param);
		visitChildren(node.getRights(), param);
		//if (node.getLeft() != null)
			//node.getLeft().accept(this, param);
		//if (node.getRight() != null)
			//node.getRight().accept(this, param);
		return null;
	}

	//	class SentenciaPrint { Expresion expresion; }
	public Object visit(SenPrint node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	//	class SentenciaPrintSp { Expresion expresion; }
	public Object visit(SenPrintSp node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	//	class SentenciaPrintLn { Expresion expresion; }
	public Object visit(SenPrintLn node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	//	class SentenciaRead { Expresion expresion; }
	public Object visit(SenRead node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	//	class SentenciaIf { Expresion condicion;  List<Sentencia> sentenciasIf;  List<Sentencia> sentenciasElse; }
	public Object visit(SenIf node, Object param) {
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		visitChildren(node.getSentenciasIf(), param);
		visitChildren(node.getSentenciasElse(), param);
		return null;
	}

	//	class SentenciaWhile { Expresion condicion;  List<Sentencia> sentencias; }
	public Object visit(SenWhile node, Object param) {
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		visitChildren(node.getSentencias(), param);
		return null;
	}

	//	class SentenciaReturn { Expresion retorno; }
	public Object visit(SenReturn node, Object param) {
		if (node.getRetorno() != null)
			node.getRetorno().accept(this, param);
		return null;
	}

	//	class ExBinaria {  }
	public Object visit(ExBinaria node, Object param) {
		return null;
	}

	//	class ExAritmetica { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExAritmetica node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	//	class ExLogica { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExLogica node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	//	class ExRelacional { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExRelacional node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	//	class ExNot { Expresion expresion; }
	public Object visit(ExNot node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	//	class ExInvocacion { String nombre;  List<Expresion> listaArgumentos; }
	public Object visit(ExInvocacion node, Object param) {
		visitChildren(node.getListaArgumentos(), param);
		return null;
	}

	//	class ExIndice { Expresion left;  Expresion indice; }
	public Object visit(ExIndice node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getIndice() != null)
			node.getIndice().accept(this, param);
		return null;
	}

	//	class ExCampo { Expresion left;  Expresion campo; }
	public Object visit(ExCampo node, Object param) {
		if (node.getStruct() != null)
			node.getStruct().accept(this, param);
		if (node.getCampo() != null)
			node.getCampo().accept(this, param);
		return null;
	}

	//	class ExCast { Tipo tipo;  Expresion cast; }
	public Object visit(ExCast node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		if (node.getFrom() != null)
			node.getFrom().accept(this, param);
		return null;
	}

	//	class LitEntero { String valor; }
	public Object visit(LitEntero node, Object param) {
		return null;
	}

	//	class LitReal { String valor; }
	public Object visit(LitReal node, Object param) {
		return null;
	}

	//	class LitCaracter { String valor; }
	public Object visit(LitCaracter node, Object param) {
		return null;
	}

	//	class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		return null;
	}

	//	class TipoEntero {  }
	public Object visit(TipoEntero node, Object param) {
		return null;
	}

	//	class TipoReal {  }
	public Object visit(TipoReal node, Object param) {
		return null;
	}

	//	class TipoCaracter {  }
	public Object visit(TipoCaracter node, Object param) {
		return null;
	}

	//	class TipoVariable { String nombre; }
	public Object visit(TipoStruct node, Object param) {
		return null;
	}

	//	class TipoArray { Tipo tipo;  String tamaño; }
	public Object visit(TipoArray node, Object param) {
		if (node.getTipoBase() != null)
			node.getTipoBase().accept(this, param);
		return null;
	}

	//	class TipoVoid {  }
	public Object visit(TipoVoid node, Object param) {
		return null;
	}
	
	// Método auxiliar -----------------------------
	protected void visitChildren(List<? extends AST> children, Object param) {
		if (children != null)
			for (AST child : children)
				child.accept(this, param);
	}


}
