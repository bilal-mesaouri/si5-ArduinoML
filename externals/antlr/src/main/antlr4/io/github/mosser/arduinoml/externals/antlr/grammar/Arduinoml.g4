grammar Arduinoml;

/******************
** Parser rules **
******************/

root            :   declaration bricks states EOF;

declaration     :   'application' name=IDENTIFIER;

bricks          :   (sensor|actuator|serial)+;
   sensor       :   'sensor' location ;
   actuator     :   'actuator' location ;
   serial       :   'serial' baudRate=INTEGER;
   location     :   id=IDENTIFIER ':' port=PORT_NUMBER;

states          :   state+;
    state       :   initial? name=IDENTIFIER '{'  (action | serialAction)+ transition* '}';
    action      :   receiver=IDENTIFIER '<=' value=SIGNAL;
    serialAction :   'serial' '<=' message=STRING;
    transition  :   serialCondition '=>' next=IDENTIFIER | signalCondition '=>' next=IDENTIFIER;
    initial     :   '->';

serialCondition :   'serial' '<=' value=STRING;
signalCondition :   trigger=IDENTIFIER 'is' value=SIGNAL;

/*****************
** Lexer rules **
*****************/

PORT_NUMBER     :   [1-9] | '10' | '11' | '12';
INTEGER         :   [0-9]+;
IDENTIFIER      :   LOWERCASE (LOWERCASE|UPPERCASE|DIGITS)*;
SIGNAL          :   'HIGH' | 'LOW';
STRING          :   '"' .*? '"';

/*************
** Helpers **
*************/

fragment LOWERCASE  : [a-z];
fragment UPPERCASE  : [A-Z];
fragment DIGITS     : [0-9];

NEWLINE            : ('\r'? '\n' | '\r')+      -> skip;
WS                 : ((' ' | '\t')+)           -> skip;
COMMENT            : '#' ~( '\r' | '\n' )*     -> skip;