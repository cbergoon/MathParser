package com.cbergoon.parser;

import java.util.ArrayList;

//
// LexicalAnalyzer Class
//
class LexicalAnalyzer {
    public enum token_type{ // Allowed token types
        ENDOF,
        UNKNOWN,
        EQUALS,
        PLUS,
        STAR,
        LPAR,
        RPAR,
        NUMBER,
        VARIABLE
    };

    // Function Name: LexicalAnalyzer
    // Purpose: Default constructor - Inits class to empty string
    // Params: 
    // Pre: None 
    // Post: Lexical analyzer with no lexemes at end of file condition
    public LexicalAnalyzer(){
        SYMBOLS = "+*()=";
        next_l = "";
        next_t = token_type.ENDOF;
        current_position = 0;
    }

    // Function Name: LexicalAnalyzer
    // Purpose: Constructor
    // Params: in - String to parse
    // Pre: None
    // Post: Lexical analyzer initialized to input string with lexemes from string; read to advance
    public LexicalAnalyzer(String in){
        SYMBOLS = "+*()="; // Allowable math symbols
        current_position = 0; // Index value
        for(char c: in.toCharArray()){ // Split into tokens and remove whitespace
            if(validChar(c)){
                lexemes.add(Character.toString(c));
            }
        }
        advance(); // Move to first token
        //current_position = 0;
    }
    
    // Function Name: advance
    // Purpose: Advances the 'pointer' to the next lexeme if available, else end of file condition
    // Params: None
    // Pre: Valid LexicalAnalyzer
    // Post: LA now holds values for next token of input
    public void advance(){
        if(current_position == lexemes.size()){ //Check for end of file/input
            next_t = token_type.ENDOF;
            next_l = "";
            return;
        }

        next_l = lexemes.get(++current_position); // Get the next lexeme to evaluate

        // Determine token type
        if(next_l.compareTo("+") == 0){
            next_t = token_type.PLUS;
        }else if(next_l.compareTo("*") == 0){
            next_t = token_type.STAR;
        }else if(next_l.compareTo("(") == 0){
            next_t = token_type.LPAR;
        }else if(next_l.compareTo(")") == 0){
            next_t = token_type.RPAR;
        }else if(next_l.compareTo("=") == 0){
            next_t = token_type.EQUALS;
        }else if(isValidVariableName(next_l)){
            next_t = token_type.VARIABLE;
        }else{ // Numbers are individual digits; later parsed to a multi-digit number
            next_t = token_type.NUMBER;
        }
    }

    // Function Name: reverse
    // Purpose: Advances the 'pointer' to the previous lexeme if available, else end of file condition
    // Params: None
    // Pre: Valid LexicalAnalyzer
    // Post: LA now holds values for previous token of input
    public void reverse(){
        if(current_position == 0){ //Check for end of file/input
            return;
        }

        current_position = current_position - 1;

        next_l = lexemes.get(current_position); // Get the next lexeme to evaluate

        // Determine token type
        if(next_l.compareTo("+") == 0){
            next_t = token_type.PLUS;
        }else if(next_l.compareTo("*") == 0){
            next_t = token_type.STAR;
        }else if(next_l.compareTo("(") == 0){
            next_t = token_type.LPAR;
        }else if(next_l.compareTo(")") == 0){
            next_t = token_type.RPAR;
        }else if(next_l.compareTo("=") == 0){
            next_t = token_type.EQUALS;
        }else if(isValidVariableName(next_l)){
            next_t = token_type.VARIABLE;
        }else{ // Numbers are individual digits; later parsed to a multi-digit number
            next_t = token_type.NUMBER;
        }
    }

    // Function Name: isValidVariableName
    // Purpose: Test if the string is a valid name for a variable name.
    // Params: String - object to test
    // Pre: None
    // Post: Returns true if the string is a valid variable name, otherwise false.
    public boolean isValidVariableName(String v){
        return ((v.compareTo("x") == 0) || (v.compareTo("y") == 0) || (v.compareTo("z") == 0));
    }

    // Function Name: validChar
    // Purpose: Test if a given terminal is part of the language.
    // Params: c - Character to test
    // Pre: None 
    // Post: True if is part of language, false otherwise
    public boolean validChar(char c){
        //True if character is in language
        return (c >= 'x' && c <= 'z') || (c >= '0' && c <= '9') || (c == '+') || (c == '*') || (c == '(') || (c == ')') || (c == '=');
    }
    
    // Function Name: endOfTokens
    // Purpose: Test if tokens are remaining 
    // Params: None
    // Pre: None
    // Post: Return true if no tokens, otherwise false 
    public boolean endOfTokens(){
        if(current_position == lexemes.size()){
            return true;
        }else{
            return false;
        }
    }
    
    // Function Name: getDump
    // Purpose: String for errors
    // Params: None
    // Pre: None
    // Post: Returns a formatted string of tokens
    public String getDump(){
        String tmp = "";
        // Build state string, used for errors.
        for (String lexeme : lexemes) {
            if (lexeme.compareTo("+") == 0) {
                tmp = tmp + token_type.PLUS.toString() + " " + lexeme + "\n";
            } else if (lexeme.compareTo("*") == 0) {
                tmp = tmp + token_type.STAR.toString() + " " + lexeme + "\n";
            } else if (lexeme.compareTo("(") == 0) {
                tmp = tmp + token_type.LPAR.toString() + " " + lexeme + "\n";
            } else if (lexeme.compareTo(")") == 0) {
                tmp = tmp + token_type.RPAR.toString() + " " + lexeme + "\n";
            } else if (lexeme.compareTo("=") == 0) {
                tmp = tmp + token_type.EQUALS.toString() + " " + lexeme + "\n";
            } else if (isValidVariableName(lexeme)) {
                tmp = tmp + token_type.VARIABLE.toString() + " " + lexeme + "\n";
            } else {
                tmp = tmp + token_type.NUMBER.toString() + " " + lexeme + "\n";
            }
        }
        return tmp;
    }

    // Function Name: getCurrentPosition
    // Purpose: Returns value of current position
    // Params: None
    // Pre: None
    // Post: Returns current position 
    public int getCurrentPosition(){
        return current_position;
    }

    // Function Name: getLexemes
    // Purpose: Returns the current lexemes
    // Params: None
    // Pre: None
    // Post: Returns array list of tokens 
    public ArrayList<String> getLexemes(){
        return lexemes;
    }

    public final String SYMBOLS; // Allowable symbols
    public token_type next_t; // Type of current token
    public String next_l; // Value/Symbol of next token
    private int current_position; // Index in equation while parsing
    private ArrayList<String> lexemes = new ArrayList<String>(); // Array of lexemes
}