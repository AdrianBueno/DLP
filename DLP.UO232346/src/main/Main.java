package main;

import generacionDeCodigo.*;

import java.io.*;

import semantico.*;
import sintactico.*;
import visitor.*;
import ast.*;


/**
 * Clase que inicia el compilador e invoca a todas sus fases.
 * 
 * No es necesario modificar este fichero. En su lugar hay que modificar:
 * - Para An�lisis Sint�ctico: 'sintactico/sintac.y' y 'sintactico/lexico.l'
 * - Para An�lisis Sem�ntico: 'semantico/Identificacion.java' y 'semantico/ComprobacionDeTipos.java'
 * - Para Generaci�n de C�digo: 'generacionDeCodigo/GestionDeMemoria.java' y 'generacionDeCodigo/SeleccionDeInstrucciones.java'
 *
 * @author Ra�l Izquierdo, Adri�n Bueno
 * @version 2
 * 
 * Cambio en la salida de generaci�n de c�digo. Ahora el resultado de la gesti�n de memoria saldr� en un fichero
 * en  output/memoria.txt y la selecci�n de instrucciones en output/codigo.txt.
 * 
 */
public class Main {
	public static final String programa = "input/prueba.txt";
	//public static final String programa = "input/Test Generaci�n de C�digo 4.txt";	// Entrada a usar durante el desarrollo
	//public static final String programa = "input/input1.txt";
	//public static final String programa = "input/ejemplo.txt";
	//public static final String programa = "input/Hipoteca.txt";
	public static void main(String[] args) throws Exception {
		GestorErrores gestor = new GestorErrores();

		AST raiz = compile(programa, gestor); // Poner args[0] en vez de "programa" en la versi�n final
		if (!gestor.hayErrores())
			System.out.println("El programa se ha compilado correctamente.");

		ASTPrinter.toHtml(programa, raiz, "Traza arbol"); // Utilidad generada por VGen (opcional)
	}

	/**
	 * M�todo que coordina todas las fases del compilador
	 */
	public static AST compile(String sourceName, GestorErrores gestor) throws Exception {
		// 1. Fases de An�lisis L�xico y Sint�ctico
		Yylex lexico = new Yylex(new FileReader(sourceName), gestor);
		Parser sint�ctico = new Parser(lexico, gestor, false);
		sint�ctico.parse();

		AST raiz = sint�ctico.getAST();
		if (raiz == null) // Hay errores o el AST no se ha implementado a�n
			return null;
		// 2. Fase de An�lisis Sem�ntico
		AnalisisSemantico sem�ntico = new AnalisisSemantico(gestor);
		sem�ntico.analiza(raiz);
		if (gestor.hayErrores())
			return raiz;
		// 3. Fase de Generaci�n de C�digo
		File sourceFile = new File(sourceName);
		
		Writer memory = new FileWriter(new File("output/", "memoria.txt"));
		Writer code = new FileWriter(new File("output/", "codigo.txt"));
		
		GeneracionDeCodigo generador = new GeneracionDeCodigo();
		generador.genera("../input/"+sourceFile.getName(), raiz, memory, code);
		memory.close();
		code.close();

		return raiz;
	}
}
