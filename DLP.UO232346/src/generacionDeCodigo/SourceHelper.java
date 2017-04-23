package generacionDeCodigo;

import java.io.PrintWriter;
import java.io.Writer;

import ast.declaraciones.DefVariable;
import ast.tipos.Tipo;
import ast.tipos.TipoArray;
import ast.tipos.TipoCaracter;
import ast.tipos.TipoEntero;
import ast.tipos.TipoReal;
import ast.tipos.TipoStruct;

/**
 * Con esta clase externalizo los m�todos que sacan a fichero el c�digo y la memoria. De esta forma es m�s c�modo
 * trabajar con los 3 visitors, sin tener que pasarle el Writer a cada uno.
 * @author Adri�n Bueno
 *
 */
public class SourceHelper {
	
	
	private static SourceHelper instance = null;
	
	public static SourceHelper getInstance(){
		if(instance == null)
			instance = new SourceHelper();
		return instance;
	}
	
	private PrintWriter writer;
	private String formatSpace = "			";
	
	
	public Writer getWriter() {
		return writer;
	}

	public void setWriter(PrintWriter writer) {
		this.writer = writer;
	}

	public void meta(String metadata){
		writer.println(metadata);
	}

	public void code(String instruccion) {
		writer.println(instruccion);
	}
	
	public void memory(DefVariable var){
		if(var.getAmbito() == DefVariable.GLOBAL)
			writer.println(var.getNombre() + " Tipo: " + var.getTipo().getNombreTipo() + "Absolute addr: " + var.getAddress());
		else
			writer.println(formatSpace+var.getNombre() + " Tipo: " + var.getTipo().getNombreTipo() + "Relative addr: " + var.getAddress());
	}
	public void label(String label) {
		writer.println(label+":");
	}
	
		
	public String getSubfijo(Tipo tipo){
		if(tipo instanceof TipoEntero)
			return "i";
		if(tipo instanceof TipoReal)
			return "f";
		if(tipo instanceof TipoCaracter)
			return "b";
		if(tipo instanceof TipoArray)
			return "r";
		if(tipo instanceof TipoStruct)
			return "r";
		return "";
		
	}

	
}
