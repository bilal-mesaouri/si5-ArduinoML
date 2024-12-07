grammar Arduinoml;

/*****************
** Lexer rules **
*****************/

PORT_NUMBER     :   [1-9] | '10' | '11' | '12';
INTEGER         :   [0-9]+;
IDENTIFIER      :   LOWERCASE (LOWERCASE|UPPERCASE|DIGITS)*;
SIGNAL          :   'HIGH' | 'LOW';
STRING          :   '"' .*? '"';
OPERATOR        :   'AND' | 'OR';
CHARS : [a-zA-Z]+;
// authorize ' in the name of the key

/******************
 ** Parser rules **
 ******************/

root            :   declaration bricks states EOF;  

declaration     :   'application' name=IDENTIFIER;

bricks          :   (pinSensor|actuator|lcdActuator|serial)+;
    pinSensor   :   'PinSensor'   location ;
    serial       :   'serial' baudRate=INTEGER;
    actuator    :   'actuator' location ;
    location    :   id=IDENTIFIER ':' port=PORT_NUMBER;
    lcdActuator :   'lcdActuator' id=IDENTIFIER 'bus' bus=('BUS1' | 'BUS2') 'message' message=CHARS;
    states          :   state+;
    state       :   initial? name=IDENTIFIER '{'  (action|serialAction)+ (transition+)? '}';
    action      :   receiver=IDENTIFIER '<=' (value=SIGNAL|message=CHARS);
    transition  :   cond=condition '=>' next=IDENTIFIER ;
    initial     :   '->';
    serialAction :   'serial' '<=' message=STRING;


condition  :  signalCondition | '('fisrt_condition=condition operator=OPERATOR second_condition=condition')' |temporalCondition| serialCondition;
signalCondition   :   trigger=IDENTIFIER 'is' value=SIGNAL;
temporalCondition :   'after' duration=INTEGER 'ms';
serialCondition :   'serial' '<=' value=STRING;



/*************
** Helpers **
*************/

fragment LOWERCASE  : [a-z];
fragment UPPERCASE  : [A-Z];
fragment DIGITS     : [0-9];

NEWLINE            : ('\r'? '\n' | '\r')+      -> skip;
WS                 : ((' ' | '\t')+)           -> skip;
COMMENT            : '#' ~( '\r' | '\n' )*     -> skip;