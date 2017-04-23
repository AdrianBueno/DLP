// @author Raúl Izquierdo

/* No es necesario modificar esta sección ------------------ */
%{
package sintactico;

import java.io.*;
import java.util.*;
import ast.*;
import ast.declaraciones.*;
import ast.expresiones.*;
import ast.expresiones.literales.*;
import ast.tipos.*;
import ast.sentencias.*;
import main.*;

@SuppressWarnings(value = { "unchecked" })
%}

%token	VAR, INT, FLOAT, CHAR, STRUCT, PRINT, PRINTSP, PRINTLN, READ, CAST, IF, ELSE, WHILE, RETURN, LIT_INT, LIT_FLOAT, LIT_CHAR, IDENT, IGUAL, MAYORIGUAL, MENORIGUAL, DISTINTO, AND, OR, NOT

/* Precedencias aquí --------------------------------------- */
%nonassoc ELSE
%right '='
%left AND OR NOT
%left '>' '<' IGUAL MAYORIGUAL MENORIGUAL DISTINTO
%left '+' '-'
%left '*' '/'
%left '[' ']' '.' 
%nonassoc '(' ')'

%%

/* Añadir las reglas en esta sección ----------------------- */

/* DECLARACIONES */
programa		: l_dec_globales						{ raiz = new Programa((List<Declaracion>)$1); }
				;

l_dec_globales	:										{ List<Declaracion> lista = new ArrayList<Declaracion>();  $$ = lista; }
				|  l_dec_globales dec_global			{ List<Declaracion> lista = (List<Declaracion>)$1; lista.add((Declaracion)$2); $$ = lista; }
				;
				
l_dec_locales	:										{ List<Declaracion> lista = new ArrayList<Declaracion>();  $$ = lista; }
				|  l_dec_locales dec_local				{ List<Declaracion> lista = (List<Declaracion>)$1; lista.add((Declaracion)$2); $$ = lista; }
				;


		
dec_global		: VAR IDENT ':' tipo ';'													{ $$ = new DefVariable($2, $4); }
				| STRUCT IDENT '{' l_dec_struct '}'  ';'									{ $$ = new DefStruct($2, (List<DefVariable>)$4); }
				| IDENT '(' signatura ')' tipo_retorno '{' l_dec_locales l_sentencias '}'	{ $$ = new DefFuncion($1, (List<DefVariable>)$3, $5, (List<DefVariable>)$7, (List<Sentencia>)$8); }
				;
				
l_dec_struct	: IDENT ':' tipo ';'														{ List<DefVariable> campos = new ArrayList<DefVariable>(); campos.add(new DefVariable($1,$3)); $$ = campos;  }
				| l_dec_struct IDENT ':' tipo ';'											{ List<DefVariable> campos = (List<DefVariable>)$1; campos.add(new DefVariable($2,$4)); $$ = campos; } 
				;

dec_local		: VAR IDENT ':' tipo ';'													{ $$ = new DefVariable($2, $4); }
				;

signatura		:																			{ List<DefVariable> params = new ArrayList<DefVariable>();  $$ = params; }
				| l_parametros																{ $$ = $1; }
				;
		
l_parametros	: parametro																	{ List<DefVariable> params = new ArrayList<DefVariable>(); params.add((DefVariable)$1); $$ = params; }
				| l_parametros ',' parametro												{ List<DefVariable> params = (List<DefVariable>)$1; params.add((DefVariable)$3); $$ = params; }
				;
		
parametro		: IDENT ':' tipo														{ $$ = new DefVariable($1, $3); }
				;		
		
/* FIN DECLARACIONES */

/* SENTENCIAS */

l_sentencias	:																			{ List<Sentencia> sents = new ArrayList<Sentencia>(); $$ = sents;  } 
				| l_sentencias sentencia 													{ List<Sentencia> sents = (List<Sentencia>)$1; sents.add((Sentencia)$2); $$ = sents; }
				;
		
sentencia		: expresion '=' expresion ';'												{ $$ = new SenAsignacion($1, $3); }
				| IDENT '(' argumentos ')' ';'												{ $$ = new SenInvocacion($1, (List<Expresion>)$3); }
				| PRINT expresion ';'														{ $$ = new SenPrint($2); }
				| PRINTSP expresion ';'														{ $$ = new SenPrintSp($2); }
				| PRINTLN expresion ';'														{ $$ = new SenPrintLn($2); }
				| PRINTLN ';'																{ $$ = new SenPrintLn(null).setPositions($1); }
				| READ expresion ';'														{ $$ = new SenRead($2); }
				| WHILE '(' expresion ')' '{' l_sentencias '}'								{ $$ = new SenWhile($3, (List<Expresion>)$6); }
				| IF '(' expresion ')' '{' l_sentencias '}'									{ $$ = new SenIf($3,(List<Sentencia>)$6,null); }
				| IF '(' expresion ')' '{' l_sentencias '}' ELSE '{' l_sentencias '}'		{ $$ = new SenIf($3,(List<Sentencia>)$6,(List<Sentencia>)$10); }
				| RETURN expresion ';'														{ $$ = new SenReturn($2);  }
				| RETURN ';'																{ $$ = new SenReturn(null).setPositions($1); }
				;

		
expresion		: expresion '+' expresion													{ $$ = new ExAritmetica($1,"+",$3); }
				| expresion '*' expresion													{ $$ = new ExAritmetica($1,"*",$3); }
				| expresion '-' expresion													{ $$ = new ExAritmetica($1,"-",$3); }
				| expresion '/' expresion													{ $$ = new ExAritmetica($1,"/",$3); }
				| expresion AND expresion													{ $$ = new ExLogica($1,"&&",$3); }
				| expresion OR expresion													{ $$ = new ExLogica($1,"||",$3); }
				| NOT expresion																{ $$ = new ExNot($2); }
				| expresion '<' expresion													{ $$ = new ExRelacional($1,"<",$3); }
				| expresion '>' expresion													{ $$ = new ExRelacional($1,">",$3); }
				| expresion IGUAL expresion													{ $$ = new ExRelacional($1,"==",$3); }
				| expresion MAYORIGUAL expresion											{ $$ = new ExRelacional($1,">=",$3); }
				| expresion MENORIGUAL expresion											{ $$ = new ExRelacional($1,"<=",$3); }
				| expresion DISTINTO expresion												{ $$ = new ExRelacional($1,"!=",$3); }
				| expresion '.' IDENT														{ $$ = new ExCampo($1,new Variable($3)); }
				| expresion '[' expresion ']'												{ $$ = new ExIndice($1, $3); }
				| '(' expresion ')'															{ $$ = $2; }
				| IDENT '(' argumentos ')'													{ $$ = new ExInvocacion($1,(List<Expresion>)$3); }
				| CAST '<' tipo '>' '(' expresion ')'										{ $$ = new ExCast($3,$6); }
				| IDENT																		{ $$ = new Variable($1); }
				| LIT_INT																	{ $$ = new LitEntero($1); }	
				| LIT_FLOAT																	{ $$ = new LitReal($1); }
				| LIT_CHAR																	{ $$ = new LitCaracter($1); }
				;
		

		
argumentos		:																			{ $$ = new ArrayList<Expresion>(); }
				| l_argumentos  															{ $$ = $1; }
				;
l_argumentos	: expresion																	{ List<Expresion> exprs = new ArrayList<Expresion>(); exprs.add((Expresion)$1); $$ = exprs; }
				| l_argumentos ',' expresion												{ List<Expresion> exprs = (List<Expresion>)$1; exprs.add((Expresion)$3); $$ = exprs; }
				;
	


/* TERMINALES*/
tipo_retorno	:																			{ $$ = new TipoVoid().setPositions($1); }
				| ':' tipo																	{ $$ = $2; }

tipo			: IDENT																		{ $$ = new TipoStruct($1); }
				| '[' LIT_INT ']' tipo														{ $$ = new TipoArray($4,$2); }
				| INT																		{ $$ = new TipoEntero().setPositions($1); }
				| FLOAT																		{ $$ = new TipoReal().setPositions($1); }
				| CHAR																		{ $$ = new TipoCaracter().setPositions($1); }
				;




%%
/* No es necesario modificar esta sección ------------------ */
public Parser(Yylex lex, GestorErrores gestor, boolean debug) {
	this(debug);
	this.lex = lex;
	this.lex.setParser(this);
	this.gestor = gestor;
}

// Métodos de acceso para el main -------------
public int parse() { return yyparse(); }
public AST getAST() { return raiz; }

// Funciones requeridas por Yacc --------------
void yyerror(String msg) {
	Token lastToken = (Token) yylval;
	gestor.error("Sintáctico", "Token = " + lastToken.getToken() + ", lexema = \"" + lastToken.getLexeme() + "\". " + msg, lastToken.getStart());
}
public void setYylval(Object yylval) {
        this.yylval = yylval;
}

int yylex() {
	try {
		int token = lex.yylex();
		yylval = new Token(token, lex.lexeme(), lex.line(), lex.column());
		return token;
	} catch (IOException e) {
		return -1;
	}
}

private Yylex lex;
private GestorErrores gestor;
private AST raiz;
