package generacionDeCodigo;

import java.io.*;

import ast.*;

/**
 * Esta clase coordina las dos fases principales de la Generación de Código:
 * 1- Gestión de Memoria (asignación de direcciones)
 * 2- Selección de Instrucciones
 * 
 * No es necesario modificar esta clase. En su lugar hay que modificar las clases
 * que son llamadas desde aquí: "GestionDeMemoria.java" y "SeleccionDeInstrucciones.java".
 *   
 * @author  Adrián Bueno
 * @version 2
 * 
 * La gestión del SourceHelper se hace desde aquí, de esta forma abstraemos las 2 subfases de la impresión del fichero.
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
