# MathParser
Recursive Decent Parser for basic mathematic expressions.

# Grammar 
'''
'<expr>' 		-> 	<term> | <term> "+" <expr> | <variable> "=" <expr>
<term> 		->      <factor> | <factor> * <term>
<factor> 	-> 	<constant> | <variable> | "(" <expr> ")"
<variable> 	-> 	"x" | "y" | "z"
<constant>      -> 	<digit> | <digit> <constant>
<digit> 	-> 	"0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" | "0"
'''
