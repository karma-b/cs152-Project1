grammar FeatherweightJavaScript;


@header { package edu.sjsu.fwjs.parser; }

// Reserved words
IF        : 'if' ;
ELSE      : 'else' ;
WHILE     : 'while' ;
FUNCTION  : 'function' ;
VAR       : 'var' ;
PRINT     : 'print' ;

// Literals
INT       : [1-9][0-9]* | '0' ;
BOOL      : 'true' | 'false' ;
NULL      : 'null' ;

//identifiers
IDS       : [a-zA-Z_][a-zA-Z_0-9]*;

// Symbols
MUL       : '*' ;
DIV       : '/' ;
SEPARATOR : ';' ;
ADD       : '+' ;
SUB       : '-' ;
MOD       : '%' ;
GE        : '>=' ;
LE        : '<=' ;
EQ        : '==' ;
GT        : '>' ;
LT        : '<' ;



// Whitespace and comments
NEWLINE   : '\r'? '\n' -> skip ;
LINE_COMMENT  : '//' ~[\n\r]* -> skip ;
WS            : [ \t]+ -> skip ; // ignore whitespace
BLOCK_COMMENT : '/*' .*? '*/' -> skip ;


// ***Parsing rules ***

/** The start rule */
prog: stat+ ;

stat: expr SEPARATOR                                    # bareExpr
    | IF '(' expr ')' block (ELSE block)?               # ifThenElse
    | WHILE '(' expr ')' block                          # whileLoop
    | PRINT '(' expr ')' SEPARATOR                      # printStatement
    | SEPARATOR                                         # emptyStatement
    ;

expr: expr '(' (expr (',' expr )* )? ')'                # funcCall
    | expr op=(MUL | DIV | MOD) expr                    # multDivMod
    | expr op=(ADD | SUB) expr                          # addSub
    | expr op=(GE | LE | EQ | GT | LT) expr             # equality
    | FUNCTION '(' (IDS (',' IDS)* )? ')' block         # funcDeclare
    | IDS '=' expr                                      # assignVarIDS
    | VAR IDS '=' expr                                  # varAssign
    | IDS                                               # varRef
    | INT                                               # in
    | BOOL                                              # bool
    | NULL                                              # null
    | '(' expr ')'                                      # parens
    ;

block: '{' stat* '}'                                    # fullBlock
     | stat                                             # simpBlock
     ;