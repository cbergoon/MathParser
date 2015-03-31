package com.cbergoon.parser;

import java.util.ArrayList;

//
// Parser Class - Recursive Decent Parser
//
class Parser {

    // Function Name: Parser
    // Purpose: Constructor 
    // Params: eqa - String containing the equation
    // Pre: None
    // Post: LexicalAnalyzer initialized, flags cleared
    public Parser(String eqa, ArrayList<Double> gl){
        lxan = new LexicalAnalyzer(eqa); // LA with input equation
        // Clear errors
        lexical_error = false;
        syntax_error = false;
        // Init variables
        var_x = gl.get(0);
        var_y = gl.get(1);
        var_z = gl.get(2);
    }
    
    // Function Name: solve 
    // Purpose: Wrapper for first production rules
    // Params: None 
    // Pre: Initialized parser class
    // Post: Returns result of equation
    public double solve(){
        // Call first production rule in grammar: expr
        double res = expr();
        return res;
    }

    // Function Name: constant
    // Purpose: Constant Production Rule
    // Params: None
    // Pre: Initialized parser
    // Post: Returns value
    private double digit(){
        return Integer.parseInt(lxan.next_l);
    }

    // Function Name: constant
    // Purpose: Constant Production Rule
    // Params: None
    // Pre: Initialized parser
    // Post: Returns value
    private double constant() {
        double tmp = digit();
        lxan.advance();
        while(lxan.next_t.equals(LexicalAnalyzer.token_type.NUMBER)){ // Consecutive digits into one number; move to constant/digit rule in future
            tmp = (tmp * 10) + Integer.parseInt(lxan.next_l);
            lxan.advance();
        }
        return tmp;
    }


    // Function Name: variable
    // Purpose: Variable Production Rule
    // Params: None
    // Pre: Initialized parser
    // Post: Returns variable
    private double variable(){
        double var;
        if(lxan.next_l.equals("x")){
            var = var_x;
        }else if(lxan.next_l.equals("y")){
            var = var_y;
        }else{
            var = var_z;
        }
        return var;
    }

    // Function Name: Factor 
    // Purpose: Factor Production rule 
    // Params: None 
    // Pre: Initialized parser, called by expr 
    // Post: Advance toward result in tree
    private double factor() {
        double res;
        if(lxan.next_t.equals(LexicalAnalyzer.token_type.NUMBER)){
            double tmp = constant();
            res = tmp;
        }else if(lxan.next_t.equals(LexicalAnalyzer.token_type.RPAR)){ // Right paren cannot appear before left
            synError();
            return 0;
        }else if(lxan.next_t.equals(LexicalAnalyzer.token_type.UNKNOWN)){ // Not yet used; error checking for invalid symbol in future
            lexError();
            return 0;
        }else if(lxan.next_t.equals(LexicalAnalyzer.token_type.LPAR)){ // Recursively build tree from expression down with contents of parens
            lxan.advance();
            res = expr();
            if(lxan.next_t.equals(LexicalAnalyzer.token_type.RPAR)){ // Match left paren
                lxan.advance();
            }else{ // No match to left paren
                synError();
                return 0;
            }
        }else if(lxan.next_t.equals(LexicalAnalyzer.token_type.VARIABLE)){
            res = variable();
            lxan.advance();
        }else{ // Catch all
            lexError();
            return 0;
        }
        return res;
    }
    
    // Function Name: term 
    // Purpose: Term Production rule 
    // Params: None 
    // Pre: Initialized parser, called by expr 
    // Post: Advance toward result in tree
    private double term(){
        double res;

        double t = factor(); // Build and walk down tree for first token
        res = t;
        while(lxan.next_t == LexicalAnalyzer.token_type.STAR){ //RHS expected
            LexicalAnalyzer.token_type control = lxan.next_t;
            lxan.advance(); // Get next symbol
            t = factor(); // Walk tree for right hand side of term
            if(control == LexicalAnalyzer.token_type.STAR){ //Multiply. Easily add division with else if
                res = res * t;
            }
        }
        return res;
    }
    
    // Function Name: expr 
    // Purpose: Expression Production rule 
    // Params: None 
    // Pre: Initialized parser, called by solve 
    // Post: Advance toward result in tree
    private double expr(){
        double res = 0;
        if(lxan.next_t.equals(LexicalAnalyzer.token_type.VARIABLE)){
            double t;
            String which_var = lxan.next_l;
            lxan.advance();
            if(lxan.next_t.equals(LexicalAnalyzer.token_type.EQUALS)){
                lxan.advance();
                if(which_var.equals("x")){
                    t = expr();
                    var_x = t;
                }else if(which_var.equals("y")){
                    t = expr();
                    var_y = t;
                }else if(which_var.equals("z")){
                    t = expr();
                    var_z = t;
                }else{
                    synError();
                    return 0;
                }
                res = t;
            }else{
                lxan.reverse();

                t = term(); // Build and walk down tree for first token
                res = t;

                while(lxan.next_t == LexicalAnalyzer.token_type.PLUS){ // RHS Expected
                    LexicalAnalyzer.token_type control = lxan.next_t;
                    lxan.advance(); // Get next symbol
                    t = expr(); // Walk tree for right hand side of expression
                    if(control == LexicalAnalyzer.token_type.PLUS){ // Add. Easily add subtraction with else if
                        res = res + t;
                    }
                }
            }
        }else{
            double t = term(); // Build and walk down tree for first token
            res = t;

            while(lxan.next_t == LexicalAnalyzer.token_type.PLUS){ // RHS Expected
                LexicalAnalyzer.token_type control = lxan.next_t;
                lxan.advance(); // Get next symbol
                if(control == LexicalAnalyzer.token_type.PLUS){ // Add. Easily add subtraction with else if
                    t = expr(); // Walk tree for right hand side of expression
                    res = res + t;
                }
            }
        }
        return res;
    }

    // Function Name: saveGlobals
    // Purpose: Retain global variable values
    // Params: None
    // Pre: None
    // Post: Returns array list of global variable values
    public ArrayList<Double> saveGlobals(){
        ArrayList<Double> tmp = new ArrayList<Double>();
        tmp.add(var_x);
        tmp.add(var_y);
        tmp.add(var_z);
        return tmp;
    }

    // Function Name: getLexemes
    // Purpose: Getter for the lexeme ArrayList. Used for error.
    // Params: None
    // Pre: None
    // Post: Returns the lexemes.
    public ArrayList<String> getLexemes(){
        return lxan.getLexemes();
    }

    // Function Name: getLexError
    // Purpose: Getter for error flag
    // Params: None
    // Pre: None
    // Post: None
    public boolean getLexError(){
        return lexical_error;
    }
    
    // Function Name: getSynError
    // Purpose: Getter for error flag
    // Params: None
    // Pre: None
    // Post: None
    public boolean getSynError(){
        return syntax_error;
    }

    // Function Name: getSynErrorPosition
    // Purpose: Getter for error error position
    // Params: None
    // Pre: None
    // Post: Returns position of parser int hte array of lexemes when the error was flagged
    public int getSynErrorPosition(){
        return syn_error_position;
    }

    // Function Name: getLexErrorPosition
    // Purpose: Getter for error error position
    // Params: None
    // Pre: None
    // Post: Returns position of parser int hte array of lexemes when the error was flagged
    public int getLexErrorPosition(){
        return lex_error_position;
    }
    
    // Function Name: lexError
    // Purpose: Set error flag true
    // Params: None
    // Pre: None
    // Post: None
    public void lexError(){
        lexical_error = true;
        lex_error_position = lxan.getCurrentPosition();
    }
    
    // Function Name: synError
    // Purpose: Set error flag true
    // Params: None
    // Pre: None
    // Post: None
    public void synError(){
        syntax_error = true;
        syn_error_position = lxan.getCurrentPosition();
    }
    
    // Function Name: getDump
    // Purpose: Get token dump string from LA
    // Params: None
    // Pre: None
    // Post: Returns string representing token dump
    public String getDump(){
        return lxan.getDump();
    }

    private LexicalAnalyzer lxan; // Lexical analyzer for parser
    private boolean lexical_error; // Flag for lexical error
    private boolean syntax_error; // Flag for syntax error

    private int syn_error_position;
    private int lex_error_position;

    public double var_x;
    public double var_y;
    public double var_z;
}