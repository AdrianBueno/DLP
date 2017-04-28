//### This file created by BYACC 1.8(/Java extension  1.14)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 5 "sintac.y"
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
//#line 31 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:Object
String   yytext;//user variable to return contextual strings
Object yyval; //used to return semantic vals from action routines
Object yylval;//the 'lval' (result) I got from yylex()
Object valstk[] = new Object[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Object();
  yylval=new Object();
  valptr=-1;
}
final void val_push(Object val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Object[] newstack = new Object[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Object val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Object val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VAR=257;
public final static short INT=258;
public final static short FLOAT=259;
public final static short CHAR=260;
public final static short STRUCT=261;
public final static short PRINT=262;
public final static short PRINTSP=263;
public final static short PRINTLN=264;
public final static short READ=265;
public final static short CAST=266;
public final static short IF=267;
public final static short ELSE=268;
public final static short WHILE=269;
public final static short RETURN=270;
public final static short LIT_INT=271;
public final static short LIT_FLOAT=272;
public final static short LIT_CHAR=273;
public final static short IDENT=274;
public final static short IGUAL=275;
public final static short MAYORIGUAL=276;
public final static short MENORIGUAL=277;
public final static short DISTINTO=278;
public final static short AND=279;
public final static short OR=280;
public final static short NOT=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    3,    2,    2,    2,    6,    6,
    4,    7,    7,   10,   10,   11,    9,    9,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   12,   12,   12,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   14,   14,   15,   15,    8,    8,    5,    5,
    5,    5,    5,
};
final static short yylen[] = {                            2,
    1,    0,    2,    0,    2,    5,    6,    9,    4,    5,
    5,    0,    1,    1,    3,    3,    0,    2,    4,    5,
    3,    3,    3,    2,    3,    7,    7,   11,    3,    2,
    3,    3,    3,    3,    3,    3,    2,    3,    3,    3,
    3,    3,    3,    3,    4,    3,    4,    7,    1,    1,
    1,    1,    0,    1,    1,    3,    0,    2,    1,    4,
    1,    1,    1,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    3,    0,    0,    0,    0,
    0,    0,    0,    0,   14,   61,   62,   63,   59,    0,
    0,    0,    0,    0,    0,    0,    0,    6,    0,    0,
    0,   16,    0,    0,   15,    0,    0,    0,    7,   58,
    4,   60,    9,    0,    0,   10,    0,    5,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   50,   51,
   52,    0,    0,    0,    8,   18,    0,    0,    0,    0,
    0,   24,    0,    0,    0,    0,    0,   30,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   21,
   22,   23,   25,    0,    0,    0,   29,    0,    0,    0,
   46,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   44,   11,    0,    0,    0,
    0,    0,    0,   19,   45,   47,    0,   17,   17,   20,
    0,    0,    0,    0,   48,    0,   26,    0,   17,    0,
   28,
};
final static short yydgoto[] = {                          1,
    2,    6,   45,   48,   21,   23,   13,   34,   49,   14,
   15,   66,   67,  109,  110,
};
final static short yysindex[] = {                         0,
    0, -241, -270, -251,  -11,    0,  -24,  -93, -243,  -86,
 -239,  -22,   -4,   -6,    0,    0,    0,    0,    0, -231,
  -18,  -15, -123,  -86,  -13, -243,  -51,    0,  -86,  -10,
   -9,    0,  -86,  -79,    0,  -86,   -8,  -86,    0,    0,
    0,    0,    0,   -3, -208,    0, -221,    0,  -40,    1,
   59,   59,   36,   59,   -2,   17,   20,   47,    0,    0,
    0,   21,   59,   59,    0,    0,  487,  -86,   22,  496,
  520,    0,  571,  593,  -86,   59,   59,    0,  742,   59,
  859,  296,   59,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   59,   59,   59,   59, -210,    7,   59,    0,
    0,    0,    0,    5,  318,  348,    0,  838,   27,   26,
    0,  -36,  -36,  -36,  -36,  859,  859,  763,  -36,  -36,
  -28,  -28,  -45,  -45,  769,    0,    0,   28,   31,  -50,
  -49,   13,   59,    0,    0,    0,   59,    0,    0,    0,
  838,  370,  -27,  -14,    0, -193,    0,  -46,    0,   -1,
    0,
};
final static short yyrindex[] = {                         0,
    0,   78,    0,    0,    0,    0,    0,    0,   38,    0,
    0,    0,    0,   39,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -42,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   12,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  795,    0,    0,    0,    0,    0,    0,   71,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   41,
  102,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   41,    0,
    0,    0,    0,    0,    0,    0,    0,  -17,    0,   42,
    0,  403,  416,  425,  438,  325,  464,    0,  451,  460,
  381,  390,   97,  124,    0,    0,    0,    0,    0,    0,
    0,  832,    0,    0,    0,    0,    0,    0,    0,    0,
  -16,    0,    0,    0,    0,   25,    0,    0,    0,    0,
    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,    0,  -21,    0,    0,    0, -117,    0,
   58,    0,  879,   -7,    0,
};
final static int YYTABLESIZE=1137;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         64,
   97,   31,   32,    7,   20,   94,   92,   37,   93,   97,
   95,   40,   64,   94,   42,    3,   44,   97,   95,    4,
  143,  144,    8,   55,   56,   64,   55,   56,    9,   11,
   12,  150,    5,   10,   22,   24,   25,   26,   64,   27,
   28,   36,   29,   41,   33,   96,   98,   38,   47,   39,
   43,   17,   50,  104,   96,   46,   76,   75,   68,   77,
   80,   99,   96,  126,   27,  127,  129,  132,  136,  133,
  137,  140,  138,  139,  148,   64,  149,    1,   12,   13,
   57,   53,   54,   35,   65,    0,   64,    0,    0,    0,
    0,  128,    0,    0,   72,    0,    0,  146,   64,    0,
    0,    0,    0,    0,    0,   78,    0,    0,    0,    0,
  147,   49,   49,   49,   49,   49,   49,   49,    0,    0,
    0,    0,    0,  151,    0,    0,    0,    0,    0,   49,
   49,   49,   49,    0,    0,    0,   17,   32,   32,   32,
   32,   32,   37,   32,    0,   37,    0,    0,    0,   27,
   30,    0,    0,    0,    0,   32,   32,   32,   32,    0,
   37,   49,   37,   49,   34,   34,   34,   34,   34,    0,
   34,   16,   17,   18,    0,    0,    0,    0,    0,    0,
    0,    0,   34,   34,   34,   34,    0,   19,    0,   32,
    0,    0,    0,    0,   37,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   34,    0,    0,    0,
    0,   51,   52,   53,   54,   55,   56,    0,   57,   58,
   59,   60,   61,   62,   51,   52,   53,   54,   55,   56,
   63,   57,   58,   59,   60,   61,   62,   51,   52,   53,
   54,   55,   56,   63,   57,   58,   59,   60,   61,   62,
   51,   52,   53,   54,   55,   56,   63,   57,   58,   59,
   60,   61,   62,   17,   17,   17,   17,   17,   17,   63,
   17,   17,   17,   17,   17,   17,   27,   27,   27,   27,
   27,   27,   17,   27,   27,   27,   27,   27,   27,    0,
    0,   55,    0,    0,    0,   27,   59,   60,   61,   69,
    0,    0,   55,    0,    0,    0,   63,   59,   60,   61,
   69,    0,    0,    0,   55,    0,    0,   63,    0,   59,
   60,   61,   69,    0,    0,    0,  111,   94,   92,   63,
   93,   97,   95,    0,    0,   49,   49,   49,   49,   49,
   49,    0,    0,    0,    0,   91,    0,   90,  130,   94,
   92,    0,   93,   97,   95,   35,    0,    0,   35,    0,
    0,   32,   32,   32,   32,   32,   32,   91,    0,   90,
   37,   37,    0,   35,    0,   35,   96,    0,  131,   94,
   92,    0,   93,   97,   95,    0,    0,    0,   34,   34,
   34,   34,   34,   34,    0,    0,    0,   91,   96,   90,
  145,   94,   92,    0,   93,   97,   95,   35,    0,    0,
    0,   31,    0,   31,   31,   31,    0,    0,    0,   91,
   33,   90,   33,   33,   33,    0,    0,    0,   96,   31,
   31,   31,   31,   40,    0,    0,   40,    0,   33,   33,
   33,   33,    0,    0,    0,    0,   41,    0,    0,   41,
   96,   40,   40,   40,   40,   42,    0,    0,   42,    0,
    0,    0,    0,   31,   41,   41,   41,   41,   43,    0,
    0,   43,   33,   42,   42,   42,   42,    0,    0,    0,
    0,   39,    0,    0,   39,   40,   43,   43,   43,   43,
   38,    0,    0,   38,   36,    0,    0,   36,   41,   39,
   39,   39,   39,    0,    0,    0,    0,   42,   38,   38,
   38,   38,   36,    0,   36,    0,    0,    0,   94,   92,
   43,   93,   97,   95,    0,    0,    0,   94,   92,    0,
   93,   97,   95,   39,    0,    0,   91,   89,   90,    0,
    0,    0,   38,    0,  100,   91,   36,   90,    0,    0,
    0,   94,   92,    0,   93,   97,   95,    0,    0,    0,
   83,   84,   85,   86,   87,   88,    0,   96,  101,   91,
    0,   90,    0,    0,    0,    0,   96,    0,    0,    0,
    0,    0,   83,   84,   85,   86,   87,   88,    0,    0,
    0,    0,    0,   35,   35,    0,    0,    0,    0,    0,
   96,    0,   94,   92,    0,   93,   97,   95,    0,    0,
    0,    0,   83,   84,   85,   86,   87,   88,    0,  102,
   91,    0,   90,    0,   94,   92,    0,   93,   97,   95,
    0,    0,    0,    0,   83,   84,   85,   86,   87,   88,
    0,  103,   91,    0,   90,   31,   31,   31,   31,   31,
   31,   96,    0,    0,   33,   33,   33,   33,   33,   33,
    0,    0,    0,    0,    0,    0,    0,   40,   40,   40,
   40,   40,   40,   96,    0,    0,    0,    0,    0,    0,
   41,   41,   41,   41,   41,   41,    0,    0,    0,   42,
   42,   42,   42,   42,   42,    0,    0,    0,    0,    0,
    0,    0,   43,   43,   43,   43,   43,   43,    0,    0,
    0,    0,    0,    0,    0,   39,   39,   39,   39,   39,
   39,    0,    0,    0,   38,   38,   38,   38,   38,   38,
    0,    0,   36,   36,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   83,   84,   85,   86,   87,   88,    0,    0,    0,
   83,   84,   85,   86,   87,   88,    0,    0,    0,    0,
    0,    0,    0,   94,   92,    0,   93,   97,   95,    0,
    0,    0,    0,    0,   83,   84,   85,   86,   87,   88,
  107,   91,    0,   90,   94,   92,    0,   93,   97,   95,
   94,   92,    0,   93,   97,   95,    0,    0,    0,    0,
    0,  134,   91,    0,   90,    0,    0,    0,   91,    0,
   90,    0,   96,    0,    0,    0,   49,   49,    0,   49,
   49,   49,    0,    0,    0,   83,   84,   85,   86,   87,
   88,    0,    0,   96,   49,   49,   49,    0,    0,   96,
    0,  135,    0,    0,    0,    0,    0,   83,   84,   85,
   86,   87,   88,   47,   47,    0,   47,   47,   47,   94,
   92,    0,   93,   97,   95,   49,    0,    0,    0,    0,
    0,   47,   47,   47,    0,    0,    0,   91,    0,   90,
   94,   92,    0,   93,   97,   95,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   91,    0,
   90,    0,   47,    0,    0,    0,    0,    0,   96,   70,
   71,   73,   74,    0,    0,    0,   79,    0,    0,    0,
    0,   81,   82,    0,    0,    0,    0,    0,    0,   96,
    0,    0,    0,    0,  105,  106,    0,    0,  108,    0,
    0,  112,  113,  114,  115,  116,  117,  118,  119,  120,
  121,  122,  123,  124,  125,    0,    0,  108,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  141,    0,    0,    0,  142,   83,   84,   85,   86,
   87,   88,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   83,   84,   85,
   86,   87,   88,   83,   84,   85,   86,   87,   88,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   49,
   49,   49,   49,   49,   49,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   47,   47,   47,   47,
   47,   47,   83,   84,   85,   86,   87,   88,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   83,   84,   85,   86,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   46,  125,   24,  274,   91,   42,   43,   29,   45,   46,
   47,   33,   40,   42,   36,  257,   38,   46,   47,  261,
  138,  139,  274,   41,   41,   40,   44,   44,   40,  123,
  274,  149,  274,   58,  274,   58,   41,   44,   40,  271,
   59,   93,   58,  123,   58,   91,   68,   58,  257,   59,
   59,   40,  274,   75,   91,   59,   40,   60,   58,   40,
   40,   40,   91,  274,   40,   59,   62,   41,   41,   44,
   40,   59,  123,  123,  268,   40,  123,    0,   41,   41,
  123,   41,   41,   26,  125,   -1,   40,   -1,   -1,   -1,
   -1,   99,   -1,   -1,   59,   -1,   -1,  125,   40,   -1,
   -1,   -1,   -1,   -1,   -1,   59,   -1,   -1,   -1,   -1,
  125,   41,   42,   43,   44,   45,   46,   47,   -1,   -1,
   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,   -1,   59,
   60,   61,   62,   -1,   -1,   -1,  125,   41,   42,   43,
   44,   45,   41,   47,   -1,   44,   -1,   -1,   -1,  125,
  274,   -1,   -1,   -1,   -1,   59,   60,   61,   62,   -1,
   59,   91,   61,   93,   41,   42,   43,   44,   45,   -1,
   47,  258,  259,  260,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   59,   60,   61,   62,   -1,  274,   -1,   93,
   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,
   -1,  262,  263,  264,  265,  266,  267,   -1,  269,  270,
  271,  272,  273,  274,  262,  263,  264,  265,  266,  267,
  281,  269,  270,  271,  272,  273,  274,  262,  263,  264,
  265,  266,  267,  281,  269,  270,  271,  272,  273,  274,
  262,  263,  264,  265,  266,  267,  281,  269,  270,  271,
  272,  273,  274,  262,  263,  264,  265,  266,  267,  281,
  269,  270,  271,  272,  273,  274,  262,  263,  264,  265,
  266,  267,  281,  269,  270,  271,  272,  273,  274,   -1,
   -1,  266,   -1,   -1,   -1,  281,  271,  272,  273,  274,
   -1,   -1,  266,   -1,   -1,   -1,  281,  271,  272,  273,
  274,   -1,   -1,   -1,  266,   -1,   -1,  281,   -1,  271,
  272,  273,  274,   -1,   -1,   -1,   41,   42,   43,  281,
   45,   46,   47,   -1,   -1,  275,  276,  277,  278,  279,
  280,   -1,   -1,   -1,   -1,   60,   -1,   62,   41,   42,
   43,   -1,   45,   46,   47,   41,   -1,   -1,   44,   -1,
   -1,  275,  276,  277,  278,  279,  280,   60,   -1,   62,
  279,  280,   -1,   59,   -1,   61,   91,   -1,   41,   42,
   43,   -1,   45,   46,   47,   -1,   -1,   -1,  275,  276,
  277,  278,  279,  280,   -1,   -1,   -1,   60,   91,   62,
   41,   42,   43,   -1,   45,   46,   47,   93,   -1,   -1,
   -1,   41,   -1,   43,   44,   45,   -1,   -1,   -1,   60,
   41,   62,   43,   44,   45,   -1,   -1,   -1,   91,   59,
   60,   61,   62,   41,   -1,   -1,   44,   -1,   59,   60,
   61,   62,   -1,   -1,   -1,   -1,   41,   -1,   -1,   44,
   91,   59,   60,   61,   62,   41,   -1,   -1,   44,   -1,
   -1,   -1,   -1,   93,   59,   60,   61,   62,   41,   -1,
   -1,   44,   93,   59,   60,   61,   62,   -1,   -1,   -1,
   -1,   41,   -1,   -1,   44,   93,   59,   60,   61,   62,
   41,   -1,   -1,   44,   41,   -1,   -1,   44,   93,   59,
   60,   61,   62,   -1,   -1,   -1,   -1,   93,   59,   60,
   61,   62,   59,   -1,   61,   -1,   -1,   -1,   42,   43,
   93,   45,   46,   47,   -1,   -1,   -1,   42,   43,   -1,
   45,   46,   47,   93,   -1,   -1,   60,   61,   62,   -1,
   -1,   -1,   93,   -1,   59,   60,   93,   62,   -1,   -1,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,
  275,  276,  277,  278,  279,  280,   -1,   91,   59,   60,
   -1,   62,   -1,   -1,   -1,   -1,   91,   -1,   -1,   -1,
   -1,   -1,  275,  276,  277,  278,  279,  280,   -1,   -1,
   -1,   -1,   -1,  279,  280,   -1,   -1,   -1,   -1,   -1,
   91,   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,   -1,  275,  276,  277,  278,  279,  280,   -1,   59,
   60,   -1,   62,   -1,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   -1,   -1,  275,  276,  277,  278,  279,  280,
   -1,   59,   60,   -1,   62,  275,  276,  277,  278,  279,
  280,   91,   -1,   -1,  275,  276,  277,  278,  279,  280,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  275,  276,  277,
  278,  279,  280,   91,   -1,   -1,   -1,   -1,   -1,   -1,
  275,  276,  277,  278,  279,  280,   -1,   -1,   -1,  275,
  276,  277,  278,  279,  280,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  275,  276,  277,  278,  279,  280,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  275,  276,  277,  278,  279,
  280,   -1,   -1,   -1,  275,  276,  277,  278,  279,  280,
   -1,   -1,  279,  280,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  275,  276,  277,  278,  279,  280,   -1,   -1,   -1,
  275,  276,  277,  278,  279,  280,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,   -1,   -1,  275,  276,  277,  278,  279,  280,
   59,   60,   -1,   62,   42,   43,   -1,   45,   46,   47,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   59,   60,   -1,   62,   -1,   -1,   -1,   60,   -1,
   62,   -1,   91,   -1,   -1,   -1,   42,   43,   -1,   45,
   46,   47,   -1,   -1,   -1,  275,  276,  277,  278,  279,
  280,   -1,   -1,   91,   60,   61,   62,   -1,   -1,   91,
   -1,   93,   -1,   -1,   -1,   -1,   -1,  275,  276,  277,
  278,  279,  280,   42,   43,   -1,   45,   46,   47,   42,
   43,   -1,   45,   46,   47,   91,   -1,   -1,   -1,   -1,
   -1,   60,   61,   62,   -1,   -1,   -1,   60,   -1,   62,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   60,   -1,
   62,   -1,   91,   -1,   -1,   -1,   -1,   -1,   91,   51,
   52,   53,   54,   -1,   -1,   -1,   58,   -1,   -1,   -1,
   -1,   63,   64,   -1,   -1,   -1,   -1,   -1,   -1,   91,
   -1,   -1,   -1,   -1,   76,   77,   -1,   -1,   80,   -1,
   -1,   83,   84,   85,   86,   87,   88,   89,   90,   91,
   92,   93,   94,   95,   96,   -1,   -1,   99,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  133,   -1,   -1,   -1,  137,  275,  276,  277,  278,
  279,  280,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  275,  276,  277,
  278,  279,  280,  275,  276,  277,  278,  279,  280,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  275,
  276,  277,  278,  279,  280,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  275,  276,  277,  278,
  279,  280,  275,  276,  277,  278,  279,  280,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  275,  276,  277,  278,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"VAR","INT","FLOAT","CHAR","STRUCT",
"PRINT","PRINTSP","PRINTLN","READ","CAST","IF","ELSE","WHILE","RETURN",
"LIT_INT","LIT_FLOAT","LIT_CHAR","IDENT","IGUAL","MAYORIGUAL","MENORIGUAL",
"DISTINTO","AND","OR","NOT",
};
final static String yyrule[] = {
"$accept : programa",
"programa : l_dec_globales",
"l_dec_globales :",
"l_dec_globales : l_dec_globales dec_global",
"l_dec_locales :",
"l_dec_locales : l_dec_locales dec_local",
"dec_global : VAR IDENT ':' tipo ';'",
"dec_global : STRUCT IDENT '{' l_dec_struct '}' ';'",
"dec_global : IDENT '(' signatura ')' tipo_retorno '{' l_dec_locales l_sentencias '}'",
"l_dec_struct : IDENT ':' tipo ';'",
"l_dec_struct : l_dec_struct IDENT ':' tipo ';'",
"dec_local : VAR IDENT ':' tipo ';'",
"signatura :",
"signatura : l_parametros",
"l_parametros : parametro",
"l_parametros : l_parametros ',' parametro",
"parametro : IDENT ':' tipo",
"l_sentencias :",
"l_sentencias : l_sentencias sentencia",
"sentencia : expresion '=' expresion ';'",
"sentencia : IDENT '(' argumentos ')' ';'",
"sentencia : PRINT expresion ';'",
"sentencia : PRINTSP expresion ';'",
"sentencia : PRINTLN expresion ';'",
"sentencia : PRINTLN ';'",
"sentencia : READ expresion ';'",
"sentencia : WHILE '(' expresion ')' '{' l_sentencias '}'",
"sentencia : IF '(' expresion ')' '{' l_sentencias '}'",
"sentencia : IF '(' expresion ')' '{' l_sentencias '}' ELSE '{' l_sentencias '}'",
"sentencia : RETURN expresion ';'",
"sentencia : RETURN ';'",
"expresion : expresion '+' expresion",
"expresion : expresion '*' expresion",
"expresion : expresion '-' expresion",
"expresion : expresion '/' expresion",
"expresion : expresion AND expresion",
"expresion : expresion OR expresion",
"expresion : NOT expresion",
"expresion : expresion '<' expresion",
"expresion : expresion '>' expresion",
"expresion : expresion IGUAL expresion",
"expresion : expresion MAYORIGUAL expresion",
"expresion : expresion MENORIGUAL expresion",
"expresion : expresion DISTINTO expresion",
"expresion : expresion '.' IDENT",
"expresion : expresion '[' expresion ']'",
"expresion : '(' expresion ')'",
"expresion : IDENT '(' argumentos ')'",
"expresion : CAST '<' tipo '>' '(' expresion ')'",
"expresion : IDENT",
"expresion : LIT_INT",
"expresion : LIT_FLOAT",
"expresion : LIT_CHAR",
"argumentos :",
"argumentos : l_argumentos",
"l_argumentos : expresion",
"l_argumentos : l_argumentos ',' expresion",
"tipo_retorno :",
"tipo_retorno : ':' tipo",
"tipo : IDENT",
"tipo : '[' LIT_INT ']' tipo",
"tipo : INT",
"tipo : FLOAT",
"tipo : CHAR",
};

//#line 147 "sintac.y"
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
//#line 573 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 38 "sintac.y"
{ raiz = new Programa((List<Declaracion>)val_peek(0)); }
break;
case 2:
//#line 41 "sintac.y"
{ List<Declaracion> lista = new ArrayList<Declaracion>();  yyval = lista; }
break;
case 3:
//#line 42 "sintac.y"
{ List<Declaracion> lista = (List<Declaracion>)val_peek(1); lista.add((Declaracion)val_peek(0)); yyval = lista; }
break;
case 4:
//#line 45 "sintac.y"
{ List<Declaracion> lista = new ArrayList<Declaracion>();  yyval = lista; }
break;
case 5:
//#line 46 "sintac.y"
{ List<Declaracion> lista = (List<Declaracion>)val_peek(1); lista.add((Declaracion)val_peek(0)); yyval = lista; }
break;
case 6:
//#line 51 "sintac.y"
{ yyval = new DefVariable(val_peek(3), val_peek(1)); }
break;
case 7:
//#line 52 "sintac.y"
{ yyval = new DefStruct(val_peek(4), (List<DefVariable>)val_peek(2)); }
break;
case 8:
//#line 53 "sintac.y"
{ yyval = new DefFuncion(val_peek(8), (List<DefVariable>)val_peek(6), val_peek(4), (List<DefVariable>)val_peek(2), (List<Sentencia>)val_peek(1)); }
break;
case 9:
//#line 56 "sintac.y"
{ List<DefVariable> campos = new ArrayList<DefVariable>(); campos.add(new DefVariable(val_peek(3),val_peek(1))); yyval = campos;  }
break;
case 10:
//#line 57 "sintac.y"
{ List<DefVariable> campos = (List<DefVariable>)val_peek(4); campos.add(new DefVariable(val_peek(3),val_peek(1))); yyval = campos; }
break;
case 11:
//#line 60 "sintac.y"
{ yyval = new DefVariable(val_peek(3), val_peek(1)); }
break;
case 12:
//#line 63 "sintac.y"
{ List<DefVariable> params = new ArrayList<DefVariable>();  yyval = params; }
break;
case 13:
//#line 64 "sintac.y"
{ yyval = val_peek(0); }
break;
case 14:
//#line 67 "sintac.y"
{ List<DefVariable> params = new ArrayList<DefVariable>(); params.add((DefVariable)val_peek(0)); yyval = params; }
break;
case 15:
//#line 68 "sintac.y"
{ List<DefVariable> params = (List<DefVariable>)val_peek(2); params.add((DefVariable)val_peek(0)); yyval = params; }
break;
case 16:
//#line 71 "sintac.y"
{ yyval = new DefVariable(val_peek(2), val_peek(0)); }
break;
case 17:
//#line 78 "sintac.y"
{ List<Sentencia> sents = new ArrayList<Sentencia>(); yyval = sents;  }
break;
case 18:
//#line 79 "sintac.y"
{ List<Sentencia> sents = (List<Sentencia>)val_peek(1); sents.add((Sentencia)val_peek(0)); yyval = sents; }
break;
case 19:
//#line 82 "sintac.y"
{ yyval = new SenAsignacion(val_peek(3), val_peek(1)); }
break;
case 20:
//#line 83 "sintac.y"
{ yyval = new SenInvocacion(val_peek(4), (List<Expresion>)val_peek(2)); }
break;
case 21:
//#line 84 "sintac.y"
{ yyval = new SenPrint(val_peek(1)); }
break;
case 22:
//#line 85 "sintac.y"
{ yyval = new SenPrintSp(val_peek(1)); }
break;
case 23:
//#line 86 "sintac.y"
{ yyval = new SenPrintLn(val_peek(1)); }
break;
case 24:
//#line 87 "sintac.y"
{ yyval = new SenPrintLn(null).setPositions(val_peek(1)); }
break;
case 25:
//#line 88 "sintac.y"
{ yyval = new SenRead(val_peek(1)); }
break;
case 26:
//#line 89 "sintac.y"
{ yyval = new SenWhile(val_peek(4), (List<Expresion>)val_peek(1)); }
break;
case 27:
//#line 90 "sintac.y"
{ yyval = new SenIf(val_peek(4),(List<Sentencia>)val_peek(1),null); }
break;
case 28:
//#line 91 "sintac.y"
{ yyval = new SenIf(val_peek(8),(List<Sentencia>)val_peek(5),(List<Sentencia>)val_peek(1)); }
break;
case 29:
//#line 92 "sintac.y"
{ yyval = new SenReturn(val_peek(1));  }
break;
case 30:
//#line 93 "sintac.y"
{ yyval = new SenReturn(null).setPositions(val_peek(1)); }
break;
case 31:
//#line 97 "sintac.y"
{ yyval = new ExAritmetica(val_peek(2),"+",val_peek(0)); }
break;
case 32:
//#line 98 "sintac.y"
{ yyval = new ExAritmetica(val_peek(2),"*",val_peek(0)); }
break;
case 33:
//#line 99 "sintac.y"
{ yyval = new ExAritmetica(val_peek(2),"-",val_peek(0)); }
break;
case 34:
//#line 100 "sintac.y"
{ yyval = new ExAritmetica(val_peek(2),"/",val_peek(0)); }
break;
case 35:
//#line 101 "sintac.y"
{ yyval = new ExLogica(val_peek(2),"&&",val_peek(0)); }
break;
case 36:
//#line 102 "sintac.y"
{ yyval = new ExLogica(val_peek(2),"||",val_peek(0)); }
break;
case 37:
//#line 103 "sintac.y"
{ yyval = new ExNot(val_peek(0)); }
break;
case 38:
//#line 104 "sintac.y"
{ yyval = new ExRelacional(val_peek(2),"<",val_peek(0)); }
break;
case 39:
//#line 105 "sintac.y"
{ yyval = new ExRelacional(val_peek(2),">",val_peek(0)); }
break;
case 40:
//#line 106 "sintac.y"
{ yyval = new ExRelacional(val_peek(2),"==",val_peek(0)); }
break;
case 41:
//#line 107 "sintac.y"
{ yyval = new ExRelacional(val_peek(2),">=",val_peek(0)); }
break;
case 42:
//#line 108 "sintac.y"
{ yyval = new ExRelacional(val_peek(2),"<=",val_peek(0)); }
break;
case 43:
//#line 109 "sintac.y"
{ yyval = new ExRelacional(val_peek(2),"!=",val_peek(0)); }
break;
case 44:
//#line 110 "sintac.y"
{ yyval = new ExCampo(val_peek(2),new Variable(val_peek(0))); }
break;
case 45:
//#line 111 "sintac.y"
{ yyval = new ExIndice(val_peek(3), val_peek(1)); }
break;
case 46:
//#line 112 "sintac.y"
{ yyval = val_peek(1); }
break;
case 47:
//#line 113 "sintac.y"
{ yyval = new ExInvocacion(val_peek(3),(List<Expresion>)val_peek(1)); }
break;
case 48:
//#line 114 "sintac.y"
{ yyval = new ExCast(val_peek(4),val_peek(1)); }
break;
case 49:
//#line 115 "sintac.y"
{ yyval = new Variable(val_peek(0)); }
break;
case 50:
//#line 116 "sintac.y"
{ yyval = new LitEntero(val_peek(0)); }
break;
case 51:
//#line 117 "sintac.y"
{ yyval = new LitReal(val_peek(0)); }
break;
case 52:
//#line 118 "sintac.y"
{ yyval = new LitCaracter(val_peek(0)); }
break;
case 53:
//#line 123 "sintac.y"
{ yyval = new ArrayList<Expresion>(); }
break;
case 54:
//#line 124 "sintac.y"
{ yyval = val_peek(0); }
break;
case 55:
//#line 126 "sintac.y"
{ List<Expresion> exprs = new ArrayList<Expresion>(); exprs.add((Expresion)val_peek(0)); yyval = exprs; }
break;
case 56:
//#line 127 "sintac.y"
{ List<Expresion> exprs = (List<Expresion>)val_peek(2); exprs.add((Expresion)val_peek(0)); yyval = exprs; }
break;
case 57:
//#line 133 "sintac.y"
{ yyval = new TipoVoid().setPositions(val_peek(-1)); }
break;
case 58:
//#line 134 "sintac.y"
{ yyval = val_peek(0); }
break;
case 59:
//#line 136 "sintac.y"
{ yyval = new TipoStruct(val_peek(0)); }
break;
case 60:
//#line 137 "sintac.y"
{ yyval = new TipoArray(val_peek(0),val_peek(2)); }
break;
case 61:
//#line 138 "sintac.y"
{ yyval = new TipoEntero().setPositions(val_peek(0)); }
break;
case 62:
//#line 139 "sintac.y"
{ yyval = new TipoReal().setPositions(val_peek(0)); }
break;
case 63:
//#line 140 "sintac.y"
{ yyval = new TipoCaracter().setPositions(val_peek(0)); }
break;
//#line 973 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
