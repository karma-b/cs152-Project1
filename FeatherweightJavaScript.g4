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
IDS       : [a-zA-Z_] [a-zA-Z_0-9]*;

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
    | IF '(' expr ')' block ELSE block                  # ifThenElse
    | IF '(' expr ')' block                             # ifThen
    | WHILE '(' expr ')' block                          # whileLoop
    | PRINT '(' expr ')' SEPARATOR                      # printStatement
    | SEPARATOR                                         # emptyStatement
    ;

<<<<<<< HEAD
expr: expr op=(GE | LE | EQ | GT | LT) expr             # equality
    | expr op=(ADD | SUB)                               # addSub
    | expr op=(MUL | DIV | MOD)                         # multDivMod
    | expr FUNCTION'(' (',' IDS)* ')' block             # funcDeclare
    | expr FUNCTION'(' (',' FUNCTION)* ')'              # funcCall
    | expr IDS '=' IDS                                  # assignVarIDS
    | expr IDS '=' BOOL                                 # assignVarBool
    | expr IDS '=' NULL                                 # assignVarNull
    | expr IDS '=' INT                                  # assignVarInt
    | expr VAR IDS                                      # varRef
    | expr VAR IDS '=' IDS                              # assignIDS
    | expr VAR IDS '=' BOOL                             # assignBool
    | expr VAR IDS '=' NULL                             # assignNull
    | expr VAR IDS '=' INT                              # assignInt
    | INT                                               # in
    | BOOL                                              # bool
=======
expr: expr op=(GT | LT | GE | LE | EQ) expr             # equalityComp
    | expr op=(ADD | SUB) expr                          #AddSub
    | expr op=( '*' | '/' | '%' ) expr                  # MulDivMod
    | INT                                               # int
    | BOOL                                              # boolean
>>>>>>> 2cb7899d150805dd3b1c829a40edd4f0f33d97f2
    | NULL                                              # null
    | '(' expr ')'                                      # parens
    

    ;

block: '{' stat* '}'                                    # fullBlock
     | stat                                             # simpBlock
     ;


