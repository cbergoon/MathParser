//
//	File: Main.java
//
//	Author: Cameron Bergoon
//	Email: cbergoon@gmail.com
//
//	Date: 3-16-15
//
//  Java Version: Java SE 1.8
//  Compilation: javac com/compilers/cb512411/Main.java
//  Execution: java com/compilers/cb512411/Main
//
//	Purpose: Recursive Decent Parser for the below grammar.
//
//  Original:
//	<expr> 		-> 	<term> | <term> "+" <expr> | <variable> "=" <expr>
//	<term> 	    -> 	<factor> | <term> * <factor>
//	<factor> 	-> 	<constant> | <variable> | "(" <expr> ")"
//	<variable> 	-> 	"x" | "y" | "z"
//	<constant> 	-> 	<digit> | <digit> <constant>
//	<digit> 	-> 	"0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" | "0"
//
//  Left Recursion Removed:
//	<expr> 		-> 	<term> | <term> "+" <expr> | <variable> "=" <expr>
//	<term> 		->  <factor> | <factor> * <term>
//	<factor> 	-> 	<constant> | <variable> | "(" <expr> ")"
//	<variable> 	-> 	"x" | "y" | "z"
//	{<constant> -> 	<digit> | <digit> <constant>
//	{<digit> 	-> 	"0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" | "0"
//
//  Examples:  [1] 3*2     [2] (3*2 +(4 * 3))      [3] (2 + 2)
//             [4] x = 3   [5] 5 * (x + 2)         [4] 3 + (x = 2)
//

package com.cbergoon.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//
// Main class - Program Driver
//
public class Main {

    //
    // Main - Entry point
    // Program Driver
    //
    public static void main(String[] args) throws IOException {
        Parser p;
        String input;
        double res;

        ArrayList<Double> globals = new ArrayList<Double>();
        for(int i = 0; i < 3; i++){
            globals.add(new Double(0));
        }

        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in)); // Readerfrom stdin

        System.out.print("> "); // Prompt
        input = consoleIn.readLine(); // Initialize input

        while (input.compareTo("") != 0){ // Empty string to exit
            p = new Parser(input, globals);  // Initialize parser with input from stdin
            res = p.solve(); // Call wrapper to production rules to evaluate
            if(p.getLexError()){
                System.out.print("Lexical Error at position " + p.getLexErrorPosition() + "\n"); // Print tokenized input on error
                String tmp = "";
                for(String st : p.getLexemes()){
                    tmp += st;
                }
                System.out.println(tmp);
                for(int i = 0; i < p.getLexErrorPosition(); i++){
                    System.out.print(" ");
                }
                System.out.println("^");
                System.out.println(p.getDump());
            }else if(p.getSynError()){
                System.out.print("Syntax Error at position " + p.getSynErrorPosition() + "\n"); // Print tokenized input on error
                String tmp = "";
                for(String st : p.getLexemes()){
                    tmp += st;
                }
                System.out.println(tmp);
                for(int i = 0; i < p.getSynErrorPosition(); i++){
                    System.out.print(" ");
                }
                System.out.println("^");
                System.out.println(p.getDump());
            }else{ // No error, print result
                System.out.println(res);
            }
            globals = p.saveGlobals();
            System.out.print("> "); // Repeat
            input = consoleIn.readLine();
        }
    }
}
