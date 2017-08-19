package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.util.List;

/**
 * Created by tam-co on 12/07/2017.
 */
public class CompilationEngine {

	private List<Lexical> tokens;

	private Lexical currentToken;

	private int count = 0;

	private List<String> output;

	public CompilationEngine(List<Lexical> tokens, List<String> output) {
		this.tokens = tokens;
		this.output = output;
	}

	// 'class' className '{' classVarDec* subroutineDec* '}'
	public void compileClass() {
		output.add("<class>");

		nextToken();
		// class
		eat(currentToken);

		// className
		nextToken();
		eat(currentToken);

		// symbol {
		nextToken();
		eat(currentToken);

		nextToken();
		while (isClassSubroutineDec(currentToken) || isClassVarDec(currentToken)) {
			if (isClassVarDec(currentToken)) {
				compileClassVarDec();
			} else if (isClassSubroutineDec(currentToken)) {
				compileSubroutineDec();
			}
		}

		// symbol }
		eat(currentToken);

		output.add("</class>");
	}

	private boolean isClassVarDec(Lexical token) {
		return token.getLecical() == "static" || token.getLecical() == "field";
	}

	private boolean isClassSubroutineDec(Lexical token) {
		return token.getLecical() == "constructor" || token.getLecical() == "function" || token.getLecical() == "method";
	}

	// ( 'static' | 'field' ) type varName ( ',' varName)* ';'
	private void compileClassVarDec() {
		output.add("<classVarDec>");

		// 'static' | 'field'
		eat(currentToken);

		// type
		nextToken();
		eat(currentToken);

		// varName
		nextToken();
		eat(currentToken);

		nextToken();
		while (currentToken.getLecical() == ",") {
			// ,
			eat(currentToken);

			// varName
			nextToken();
			eat(currentToken);
			nextToken();
		}

		// ;
		eat(currentToken);

		output.add("</classVarDec>");
		nextToken();
	}

	// ( 'constructor' | 'function' | 'method' ) ( 'void' | type ) subroutineName '(' parameterList ')' subroutineBody
	private void compileSubroutineDec() {
		output.add("<subroutineDec>");

		// 'constructor' | 'function' | 'method'
		eat(currentToken);

		// 'void' | type
		nextToken();
		eat(currentToken);

		// subroutineName
		nextToken();
		eat(currentToken);

		// '('
		nextToken();
		eat(currentToken);

		// parameterList
		compileParameterList();

		// ')'
		eat(currentToken);

		// subroutineBody
		compileSubroutineBody();

		output.add("</subroutineDec>");
		nextToken();
	}

	private void compileParameterList() {

	}

	private void compileSubroutineBody() {

	}

	private void eat(Lexical token) {
		output.add(token.toString());
	}

	private void nextToken() {
		currentToken = tokens.get(count);
		count++;
	}
}
