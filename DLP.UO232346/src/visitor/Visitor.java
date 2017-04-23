/**
 * @generated VGen 1.3.3
 */

package visitor;

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

public interface Visitor {
	public Object visit(Programa node, Object param);
	public Object visit(DefVariable node, Object param);
	public Object visit(DefStruct node, Object param);
	public Object visit(DefFuncion node, Object param);
	public Object visit(SenInvocacion node, Object param);
	public Object visit(SenAsignacion node, Object param);
	public Object visit(SenPrint node, Object param);
	public Object visit(SenPrintSp node, Object param);
	public Object visit(SenPrintLn node, Object param);
	public Object visit(SenRead node, Object param);
	public Object visit(SenIf node, Object param);
	public Object visit(SenWhile node, Object param);
	public Object visit(SenReturn node, Object param);
	public Object visit(ExBinaria node, Object param);
	public Object visit(ExAritmetica node, Object param);
	public Object visit(ExLogica node, Object param);
	public Object visit(ExRelacional node, Object param);
	public Object visit(ExNot node, Object param);
	public Object visit(ExInvocacion node, Object param);
	public Object visit(ExIndice node, Object param);
	public Object visit(ExCampo node, Object param);
	public Object visit(ExCast node, Object param);
	public Object visit(LitEntero node, Object param);
	public Object visit(LitReal node, Object param);
	public Object visit(LitCaracter node, Object param);
	public Object visit(Variable node, Object param);
	public Object visit(TipoEntero node, Object param);
	public Object visit(TipoReal node, Object param);
	public Object visit(TipoCaracter node, Object param);
	public Object visit(TipoStruct node, Object param);
	public Object visit(TipoArray node, Object param);
	public Object visit(TipoVoid node, Object param);
}
