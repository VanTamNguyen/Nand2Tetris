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

		// 'class'
		nextToken();
		eat(currentToken);

		// className
		nextToken();
		eat(currentToken);

		// '{'
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

		// '}'
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
			// ','
			eat(currentToken);

			// varName
			nextToken();
			eat(currentToken);
			nextToken();
		}

		// ';'
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

	// ((type varName) ( ',' type varName)*)?
	private void compileParameterList() {
		output.add("<parameterList>");

		nextToken();
		if (currentToken.getLecical() != ")") {
			// type
			eat(currentToken);
			// varName
			nextToken();
			eat(currentToken);

			nextToken();
			while (currentToken.getLecical() == ",") {
				// ,
				eat(currentToken);

				// type
				nextToken();
				eat(currentToken);

				nextToken();
			}
		}

		output.add("</parameterList>");
	}

	// '{' varDec* statements '}'
	private void compileSubroutineBody() {
		output.add("<subroutineBody>");
		// {
		nextToken();
		eat(currentToken);

		// varDec*
		nextToken();
		while (isVarDec(currentToken)) {
			compileVarDec();
		}

		// statements
		compileStatements();

		output.add("</subroutineBody>");
	}

	private boolean isVarDec(Lexical token) {
		return token.getLecical() == "var";
	}

	private boolean isStatement(Lexical token) {
		return token.getLecical() == "let" ||
				token.getLecical() == "if" ||
				token.getLecical() == "while" ||
				token.getLecical() == "do" ||
				token.getLecical() == "return";
	}

	// 'var' type varName ( ',' varName)* ';'
	private void compileVarDec() {
		output.add("<varDec>");

		// var
		eat(currentToken);

		// type
		nextToken();
		eat(currentToken);

		// varName
		nextToken();
		eat(currentToken);

		// (',' varName)*
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
		nextToken();

		output.add("</varDec>");
	}

	// statement*
	private void compileStatements() {
		output.add("<statements>");

		while (isStatement(currentToken)) {
			compileStatement();
		}

		output.add("</statements>");
	}

	private void compileStatement() {
		if (currentToken.getLecical() == "let") {
			compileLetStatement();

		} else if (currentToken.getLecical() == "if") {
			compileIfStatement();

		} else if (currentToken.getLecical() == "while") {
			compileWhileStatement();

		} else if (currentToken.getLecical() == "do") {
			compileDoStatement();

		} else if (currentToken.getLecical() == "return") {
			compileReturnStatement();
		}
	}

	// 'let' varName ('[' expression ']')? '=' expression ';'
	private void compileLetStatement() {
		output.add("<letStatement>");
		
		// 'let'
		eat(currentToken);
		
		// varName
		nextToken();
		eat(currentToken);
		
		nextToken();
		if (currentToken.getLetLecical() == "[") {
			// '['
			eat(currentToken);
			
			// expression
			compileExpression();
			
			// ']'
			eat(currentToken);
			nextToken();
		}
		
		// '='
		eat(currentToken);
		
		// expression
		compileExpression();
		
		// ';'
		eat(currentToken);
		
		output.add("</letStatement>");
	}

	private void compileWhileStatement() {

	}

	private void compileIfStatement() {

	}

	private void compileDoStatement() {

	}

	private void compileReturnStatement() {

	}

	private void eat(Lexical token) {
		output.add(token.toString());
	}

	private void nextToken() {
		currentToken = tokens.get(count);
		count++;
	}
}
