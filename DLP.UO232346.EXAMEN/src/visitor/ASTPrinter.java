/**
 * @generated VGen 1.3.3
 */

package visitor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import ast.AST;
import ast.Position;
import ast.Programa;
import ast.Traceable;
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

/**
 * ASTPrinter. Utilidad que ayuda a validar un arbol AST:
 * 	-	Muestra la estructura del árbol en HTML.
 * 	-	Destaca los hijos/propiedades a null.
 * 	-	Muestra a qué texto apuntan las posiciones de cada nodo (linea/columna)
 * 		ayudando a decidir cual de ellas usar en los errores y generación de código.
 * 
 * Esta clase se genera con VGen. El uso de esta clase es opcional (puede eliminarse del proyecto). 
 * 
 */
public class ASTPrinter extends DefaultVisitor {

	/**
	 * toHtml. Muestra la estructura del AST indicando qué hay en las posiciones (linea y columna) de cada nodo.
	 * 
	 * @param sourceFile	El fichero del cual se ha obtenido el AST
	 * @param raiz				El AST creado a partir de sourceFile
	 * @param filename		Nombre del fichero HMTL a crear con la traza del AST
	 */

	public static void toHtml(String sourceFile, AST raiz, String filename) {
		toHtml(sourceFile, raiz, filename, 4);
	}
	
	public static void toHtml(AST raiz, String filename) {
		toHtml(null, raiz, filename);
	}

	// tabWidth deberían ser los espacios correspondientes a un tabulador en eclipse.
	// Normalmente no será necesario especificarlo. Usar mejor los dos métodos anteriores.
	public static void toHtml(String sourceFile, AST raiz, String filename, int tabWidth) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(filename.endsWith(".html") ? filename : filename + ".html"));
			generateHeader(writer);
			writer.println("[ASTPrinter] -------------------------------- line:col  line:col");
			if (raiz != null) {
				ASTPrinter tracer = new ASTPrinter(writer, loadLines(sourceFile, tabWidth));
				raiz.accept(tracer, new Integer(0));
			} else
				writer.println("raiz == null");
			writer.println(ls + ls + "[ASTPrinter] --------------------------------");
			generateFooter(writer);
			writer.close();
			System.out.println(ls + "ASTPrinter: Fichero '" + filename + ".html' generado con éxito. Abra el fichero para validar el árbol AST generado.");
		} catch (IOException e) {
			System.out.println(ls + "ASTPrinter: No se ha podido crear el fichero " + filename);
			e.printStackTrace();
		}
	}

	private static void generateHeader(PrintWriter writer) {
		writer.println("<html>\r\n" +
				"<head>\r\n" +
				"<style type=\"text/css\">\r\n" +
				".value { font-weight: bold; }\r\n" +
				".dots { color: #888888; }\r\n" +
				".type { color: #BBBBBB; }\r\n" +
				".pos { color: #CCCCCC; }\r\n" +
				".sourceText { color: #BBBBBB; }\r\n" +
				".posText {\r\n" +
				"	color: #BBBBBB;\r\n" +
				"	text-decoration: underline; font-weight: bold;\r\n" +
				"}\r\n" +
				".null {\r\n" +
				"	color: #FF0000;\r\n" +
				"	font-weight: bold;\r\n" +
				"	font-style: italic;\r\n" +
				"}\r\n" +
			//	 "pre { font-family: Arial, Helvetica, sans-serif; font-size: 11px; }\r\n" +
			//	"pre { font-size: 11px; }\r\n" +
				"</style>\r\n" +
				"</head>\r\n" +
				"\r\n" +
				"<body><pre>");
	}

	private static void generateFooter(PrintWriter writer) {
		writer.println("</pre>\r\n" +
				"</body>\r\n" +
				"</html>");
	}

	private ASTPrinter(PrintWriter writer, List<String> sourceLines) {
		this.writer = writer;
		this.sourceLines = sourceLines;
	}

	// ----------------------------------------------

	//	class Programa { List<Declaracion> declaraciones; }
	public Object visit(Programa node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "Programa", node, false);

		visit(indent + 1, "declaraciones", "List<Declaracion>",node.getDeclaraciones());
		return null;
	}

	//	class DefVariable { String nombre;  Tipo tipo; }
	public Object visit(DefVariable node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "DefVariable", node, false);

		print(indent + 1, "nombre", "String", node.getNombre());
		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		return null;
	}

	//	class DefStruct { String nombre;  List<DefCampo> listaCampos; }
	public Object visit(DefStruct node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "DefStruct", node, false);

		print(indent + 1, "nombre", "String", node.getNombre());
		visit(indent + 1, "listaCampos", "List<DefCampo>",node.getListaCampos());
		return null;
	}

	//	class DefFuncion { String nombre;  List<DefVariable> listaParametros;  Tipo tipo;  List<DefVariable> listaDeclaraciones;  List<Sentencia> listaSentencias; }
	public Object visit(DefFuncion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "DefFuncion", node, false);

		print(indent + 1, "nombre", "String", node.getNombre());
		visit(indent + 1, "listaParametros", "List<DefVariable>",node.getListaParametros());
		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		visit(indent + 1, "listaDeclaraciones", "List<DefVariable>",node.getListaDeclaraciones());
		visit(indent + 1, "listaSentencias", "List<Sentencia>",node.getListaSentencias());
		return null;
	}

	//	class Invocacion { String nombre;  List<Expresion> listaArgumentos; }
	public Object visit(SenInvocacion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "Invocacion", node, false);

		print(indent + 1, "nombre", "String", node.getNombre());
		visit(indent + 1, "listaArgumentos", "List<Expresion>",node.getListaArgumentos());
		return null;
	}

	//	class Asignacion { Expresion left;  Expresion right; }
	public Object visit(SenAsignacion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "Asignacion", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class SentenciaPrint { Expresion expresion; }
	public Object visit(SenPrint node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaPrint", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaPrintSp { Expresion expresion; }
	public Object visit(SenPrintSp node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaPrintSp", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaPrintLn { Expresion expresion; }
	public Object visit(SenPrintLn node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaPrintLn", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaRead { Expresion expresion; }
	public Object visit(SenRead node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaRead", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class SentenciaIf { Expresion condicion;  List<Sentencia> sentenciasIf;  List<Sentencia> sentenciasElse; }
	public Object visit(SenIf node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaIf", node, false);

		visit(indent + 1, "condicion", "Expresion",node.getCondicion());
		visit(indent + 1, "sentenciasIf", "List<Sentencia>",node.getSentenciasIf());
		visit(indent + 1, "sentenciasElse", "List<Sentencia>",node.getSentenciasElse());
		return null;
	}

	//	class SentenciaWhile { Expresion condicion;  List<Sentencia> sentencias; }
	public Object visit(SenWhile node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaWhile", node, false);

		visit(indent + 1, "condicion", "Expresion",node.getCondicion());
		visit(indent + 1, "sentencias", "List<Sentencia>",node.getSentencias());
		return null;
	}

	//	class SentenciaReturn { Expresion retorno; }
	public Object visit(SenReturn node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "SentenciaReturn", node, false);

		visit(indent + 1, "retorno", "Expresion",node.getRetorno());
		return null;
	}

	//	class ExBinaria {  }
	public Object visit(ExBinaria node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExBinaria", node, true);

		return null;
	}

	//	class ExAritmetica { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExAritmetica node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExAritmetica", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		print(indent + 1, "operador", "String", node.getOperador());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class ExLogica { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExLogica node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExLogica", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		print(indent + 1, "operador", "String", node.getOperador());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class ExRelacional { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExRelacional node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExRelacional", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		print(indent + 1, "operador", "String", node.getOperador());
		visit(indent + 1, "right", "Expresion",node.getRight());
		return null;
	}

	//	class ExNot { Expresion expresion; }
	public Object visit(ExNot node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExNot", node, false);

		visit(indent + 1, "expresion", "Expresion",node.getExpresion());
		return null;
	}

	//	class ExInvocacion { String nombre;  List<Expresion> listaArgumentos; }
	public Object visit(ExInvocacion node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExInvocacion", node, false);

		print(indent + 1, "nombre", "String", node.getNombre());
		visit(indent + 1, "listaArgumentos", "List<Expresion>",node.getListaArgumentos());
		return null;
	}

	//	class ExIndice { Expresion left;  Expresion indice; }
	public Object visit(ExIndice node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExIndice", node, false);

		visit(indent + 1, "left", "Expresion",node.getLeft());
		visit(indent + 1, "indice", "Expresion",node.getIndice());
		return null;
	}

	//	class ExCampo { Expresion left;  Expresion campo; }
	public Object visit(ExCampo node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExCampo", node, false);

		visit(indent + 1, "left", "Expresion",node.getStruct());
		visit(indent + 1, "campo", "Expresion",node.getCampo());
		return null;
	}

	//	class ExCast { Tipo tipo;  Expresion cast; }
	public Object visit(ExCast node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "ExCast", node, false);

		visit(indent + 1, "tipo", "Tipo",node.getTipo());
		visit(indent + 1, "cast", "Expresion",node.getFrom());
		return null;
	}

	//	class LitEntero { String valor; }
	public Object visit(LitEntero node, Object param) {
		int indent = ((Integer)param).intValue();

		printCompact(indent, "LitEntero", node, "valor", node.getValor());
		return null;
	}

	//	class LitReal { String valor; }
	public Object visit(LitReal node, Object param) {
		int indent = ((Integer)param).intValue();

		printCompact(indent, "LitReal", node, "valor", node.getValor());
		return null;
	}

	//	class LitCaracter { String valor; }
	public Object visit(LitCaracter node, Object param) {
		int indent = ((Integer)param).intValue();

		printCompact(indent, "LitCaracter", node, "valor", node.getValor());
		return null;
	}

	//	class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		int indent = ((Integer)param).intValue();

		printCompact(indent, "Variable", node, "nombre", node.getNombre());
		return null;
	}

	//	class TipoEntero {  }
	public Object visit(TipoEntero node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoEntero", node, true);

		return null;
	}

	//	class TipoReal {  }
	public Object visit(TipoReal node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoReal", node, true);

		return null;
	}

	//	class TipoCaracter {  }
	public Object visit(TipoCaracter node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoCaracter", node, true);

		return null;
	}

	//	class TipoVariable { String nombre; }
	public Object visit(TipoStruct node, Object param) {
		int indent = ((Integer)param).intValue();

		printCompact(indent, "TipoVariable", node, "nombre", node.getNombreTipo());
		return null;
	}

	//	class TipoArray { Tipo tipo;  String tamaño; }
	public Object visit(TipoArray node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoArray", node, false);

		visit(indent + 1, "tipo", "Tipo",node.getTipoBase());
		print(indent + 1, "tamaño", "String", node.getArraySize());
		return null;
	}

	//	class TipoVoid {  }
	public Object visit(TipoVoid node, Object param) {
		int indent = ((Integer)param).intValue();

		printName(indent, "TipoVoid", node, true);

		return null;
	}

	// -----------------------------------------------------------------
	// Métodos invocados desde los métodos visit -----------------------

	private void printName(int indent, String name, AST node, boolean empty) {
		String text = ls + tabula(indent) + name + " &rarr;  ";
		text = String.format("%1$-" + 93 + "s", text);
		if (empty)
			text = text.replace(name, valueTag(name));
		writer.print(text + getPosition(node));
	}

	private void print(int indent, String name, String type, Object value) {
		write(indent, formatValue(value) + "  " + typeTag(type));
	}

	@SuppressWarnings("unused")
	private void print(int indent, String attName, String type, List<? extends Object> children) {
		write(indent, attName + "  " + typeTag(type) + " = ");
		if (children != null)
			for (Object child : children)
				write(indent + 1, formatValue(child));
		else
			writer.print(" " + valueTag(null));
	}

	// Versión compacta de una linea para nodos que solo tienen un atributo String
	private void printCompact(int indent, String nodeName, AST node, String attName, Object value) {
		String fullName = nodeName + '.' + attName;
		String text = ls + tabula(indent) + '\"' + value + "\"  " + fullName;
		text = String.format("%1$-" + 88 + "s", text);
		// text = text.replace(value.toString(), valueTag(value));
		text = text.replace(fullName, typeTag(fullName));
		writer.print(text + getPosition(node));
	}

	private void visit(int indent, String attName, String type, List<? extends AST> children) {
		write(indent, attName + "  " + typeTag(type) + " = ");
		if (children != null)
			for (AST child : children)
				child.accept(this, indent + 1);
		else
			writer.print(" " + valueTag(null));
	}

	private void visit(int indent, String attName, String type, AST child) {
		if (child != null)
			child.accept(this, new Integer(indent));
		else
			write(indent, valueTag(null) + "  " + attName + ':' + typeTag(type));
	}

	// -----------------------------------------------------------------
	// Métodos auxiliares privados -------------------------------------

	private void write(int indent, String text) {
		writer.print(ls + tabula(indent) + text);
	}

	private static String tabula(int count) {
		StringBuffer cadena = new StringBuffer("<span class=\"dots\">");
		for (int i = 0; i < count; i++)
			cadena.append(i % 2 == 0 && i > 0 ? "|  " : "·  ");
		return cadena.toString() + "</span>";
	}

	private String typeTag(String type) {
		if (type.equals("String"))
			return "";
		return "<span class=\"type\">" + type.replace("<", "&lt;").replace(">", "&gt;") + "</span>";
	}

	private String valueTag(Object value) {
		if (value == null)
			return "<span class=\"null\">null</span>";
		return "<span class=\"value\">" + value + "</span>";
	}

	private String formatValue(Object value) {
		String text = valueTag(value);
		if (value instanceof String)
			text = "\"" + text + '"';
		return text;
	}


	// -----------------------------------------------------------------
	// Métodos para mostrar las Posiciones -----------------------------

	private String getPosition(Traceable node) {
		String text = node.getStart() + "  " + node.getEnd();
		text = "<span class=\"pos\">" + String.format("%1$-" + 13 + "s", text) + "</span>";
		text = text.replace("null", "<span class=\"null\">null</span>");
		String sourceText = findSourceText(node);
		if (sourceText != null)
			text += sourceText;
		return text;
	}

	private String findSourceText(Traceable node) {
		if (sourceLines == null)
			return null;

		Position start = node.getStart();
		Position end = node.getEnd();
		if (start == null || end == null)
			return null;

		String afterText, text, beforeText;
		if (start.getLine() == end.getLine()) {
			String line = sourceLines.get(start.getLine() - 1);
			afterText = line.substring(0, start.getColumn() - 1);
			text = line.substring(start.getColumn() - 1, end.getColumn());
			beforeText = line.substring(end.getColumn());
		} else {
			String firstLine = sourceLines.get(start.getLine() - 1);
			String lastLine = sourceLines.get(end.getLine() - 1);

			afterText = firstLine.substring(0, start.getColumn() - 1);

			text = firstLine.substring(start.getColumn() - 1);
			text += "</span><span class=\"sourceText\">" + " ... " + "</span><span class=\"posText\">";
			text += lastLine.substring(0, end.getColumn()).replaceAll("^\\s+", "");

			beforeText = lastLine.substring(end.getColumn());
		}
		return "<span class=\"sourceText\">" + afterText.replaceAll("^\\s+", "")
				+ "</span><span class=\"posText\">" + text
				+ "</span><span class=\"sourceText\">" + beforeText + "</span>";
	}

	private static List<String> loadLines(String sourceFile, int tabWidth) {
		if (sourceFile == null)
			return null;
		try {
			String spaces = new String(new char[tabWidth]).replace("\0", " ");
			
			List<String> lines = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(sourceFile));
			String line;
			while ((line = br.readLine()) != null)
				lines.add(line.replace("\t", spaces));
			br.close();
			return lines;
		} catch (FileNotFoundException e) {
			System.out.println("Warning. No se pudo encontrar el fichero fuente '" + sourceFile + "'. No se mostrará informaicón de posición.");
			return null;
		} catch (IOException e) {
			System.out.println("Warning. Error al leer del fichero fuente '" + sourceFile + "'. No se mostrará informaicón de posición.");
			return null;
		}
	}


	private List<String> sourceLines;
	private static String ls = System.getProperty("line.separator");
	private PrintWriter writer;
}

