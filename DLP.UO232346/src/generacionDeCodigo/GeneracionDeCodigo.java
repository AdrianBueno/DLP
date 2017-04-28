package generacionDeCodigo;

import java.io.*;

import ast.*;

/**
 * Esta clase coordina las dos fases principales de la Generaci�n de C�digo:
 * 1- Gesti�n de Memoria (asignaci�n de direcciones)
 * 2- Selecci�n de Instrucciones
 * 
 * No es necesario modificar esta clase. En su lugar hay que modificar las clases
 * que son llamadas desde aqu�: "GestionDeMemoria.java" y "SeleccionDeInstrucciones.java".
 *   
 * @author Ra�l Izquierdo, Adri�n Bueno
 * @version 2
 * 
 * La gesti�n del SourceHelper se hace desde aqu�, de esta forma abstraemos las 2 subfases de la impresi�n del fichero.
 *
 */
public class GeneracionDeCodigo {

	public void genera(String sourceFile, AST raiz, Writer memory, Writer code) {
		SourceHelper sh = SourceHelper.getInstance();
		sh.setWriterMemory(new PrintWriter(memory));
		sh.setWriterCode(new PrintWriter(code));
		
		
		GestionDeMemoria gestion = new GestionDeMemoria();
		raiz.accept(gestion, null);
		SeleccionDeInstrucciones selecciona = new SeleccionDeInstrucciones(sourceFile);
		raiz.accept(selecciona, null);
	}

}
