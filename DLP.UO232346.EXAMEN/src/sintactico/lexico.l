

/* -- No es necesario modificar esta parte ----------------------------------------------- */
package sintactico;

import java.io.*;
import main.*;
import ast.Position;
import sintactico.Parser;

@SuppressWarnings(value = { "unused" })

%%
%byaccj
%unicode
%line
%column
%public

%{
	private Parser parser;
	public void setParser(Parser parser){
		this.parser = parser;
	}
	public Yylex(Reader in, GestorErrores gestor) {
		this(in);
		this.gestor = gestor;
	}

	public int line() { return yyline + 1; }
	public int column() { return yycolumn + 1; }
	public String lexeme() { return yytext(); }

	private GestorErrores gestor;
%}

LETRA = [a-zA-ZñÑ_]										/*Incluyo la _*/
DIGITO = [0-9]											/* Digito 1 número 0-9*/
LIT_INT = {DIGITO}+										/* 1 o más dígitos son un entero*/
LIT_FLOAT = {LIT_INT} "." {LIT_INT}						/*Reales con una parte entera y otra decimal*/
LIT_CHAR = '.' | '\\n' | '\\r'
IDENT = {LETRA}({LETRA}|{DIGITO})*						/**/
COMENTARIO = (("/*"([^*]|\*+[^*/])*\*+"/")|("//".*))


%%
/* PALABRAS RESERVADAS*/

var						{ return Parser.VAR ; }	
float					{ return Parser.FLOAT ; }
int						{ return Parser.INT ; }
char					{ return Parser.CHAR ; }
struct					{ return Parser.STRUCT ; }

print					{ return Parser.PRINT ; }
printsp					{ return Parser.PRINTSP ; }
println					{ return Parser.PRINTLN ; }
read					{ return Parser.READ ; }
cast					{ return Parser.CAST ; }
	
if						{ return Parser.IF ; }
else					{ return Parser.ELSE ; }
while					{ return Parser.WHILE ; }
return					{ return Parser.RETURN ; }


/* LITERALES */
{LIT_INT}						{ parser.setYylval(new Integer(yytext()));   return Parser.LIT_INT ; }
{LIT_FLOAT}						{ parser.setYylval(yytext()); return Parser.LIT_FLOAT ; }
{LIT_CHAR}						{ parser.setYylval(yytext()); return Parser.LIT_CHAR ; }
{IDENT}							{ parser.setYylval(yytext()); return Parser.IDENT ; }

/* OPERADORES */

			/* Comparación */
"=="							{ return Parser.IGUAL ; }
">="							{ return Parser.MAYORIGUAL ; }
"<="							{ return Parser.MENORIGUAL ; }
"!="							{ return Parser.DISTINTO ; }
			/* Lógicos */
"&&"							{ return Parser.AND ; }
"||"							{ return Parser.OR ; }
"!"								{ return Parser.NOT ; }


/* -- Se aconseja dejar estas reglas al final ----------------------------------------------- */

/* Aritméticos */
[+*\-/<>,;:=()<>\.\[\]{}\^]		{ return yytext().charAt(0); }
/* EXAMEN : Añado operador ^ con el escape*/
{COMENTARIO}					{ }

[ \n\r]		{ }
"\t"		{ yycolumn += 3; } // Para que coincida con la info del editor de Eclipse (opcional). En eclipse: \t == 4 caracteres. En Jflex: \t == 1 carácter.

.			{ gestor.error("Léxico", "Cadena \"" + yytext() +"\" no reconocida.", new Position(line(), column())); }
