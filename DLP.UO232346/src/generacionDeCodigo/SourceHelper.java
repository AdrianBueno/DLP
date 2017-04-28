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
 * Con esta clase externalizo los métodos que sacan a fichero el código y la memoria. De esta forma es más cómodo
 * trabajar con los 3 visitors, sin tener que pasarle el Writer a cada uno.
 * @author Adrián Bueno
 *
 */
public class SourceHelper {
	
	
	private static SourceHelper instance = null;
	
	public static SourceHelper getInstance(){
		if(instance == null)
			instance = new SourceHelper();
		return instance;
	}
	
	private PrintWriter writerMemory;
	private PrintWriter writerCode;
	private String formatSpace = "			";
	
	
	public Writer getWriterMemory() {
		return writerMemory;
	}

	public void setWriterMemory(PrintWriter writer) {
		this.writerMemory = writer;
	}
	
	public PrintWriter getWriterCode() {
		return writerCode;
	}

	public void setWriterCode(PrintWriter writerCode) {
		this.writerCode = writerCode;
	}

	public void meta(String metadata){
		writerCode.println(metadata);
	}

	public void code(String instruccion) {
		writerCode.println(instruccion);
	}
	
	public void directivaDireccion(DefVariable var){
		if(var.getAmbito() == DefVariable.GLOBAL){
			writerMemory.println(var.getNombre() + " Tipo: " + var.getTipo().getNombreTipo() + "Absolute addr: " + var.getAddress());
			writerCode.println("#GLOBAL " + var.getNombre() + ":" + var.getTipo().getNombreTipo());
		}else{
			writerMemory.println(formatSpace+var.getNombre() + " Tipo: " + var.getTipo().getNombreTipo() + "Relative addr: " + var.getAddress());
			writerCode.println();
		}
	}
	
	public void codeLabel(String label) {
		writerCode.println(label+":");
	}
	public void memoryLabel(String label) {
		writerMemory.println(label+":");
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
