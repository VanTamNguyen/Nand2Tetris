package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.util.List;

/**
 * Created by tam-co on 12/07/2017.
 */
public class CompilationEngine {

	private List<Lexical> tokens;

	private List<String> output;

	public CompilationEngine(List<Lexical> tokens, List<String> output) {
		this.tokens = tokens;
		this.output = output;
	}

	// 'class' className '{' classVarDec* subroutineDec* '}'
	public void compileClass() {
		output.add("<class>");

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

		output.add("</classVarDec>");
	}

	// ( 'constructor' | 'function' | 'method' ) ( 'void' | type ) subroutineName '(' parameterList ')' subroutineBody
	private void compileSubroutineDec() {
		output.add("<subroutineDec>");

		output.add("</subroutineDec>");
	}

	// ((type varName) ( ',' type varName)*)?
	private void compileParameterList() {
		output.add("<parameterList>");

		output.add("</parameterList>");
	}

	// '{' varDec* statements '}'
	private void compileSubroutineBody() {
		output.add("<subroutineBody>");

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

		output.add("</varDec>");
	}

	// statement*
	private void compileStatements() {
		output.add("<statements>");

		output.add("</statements>");
	}

	private void compileStatement() {

	}

	// 'let' varName ('[' expression ']')? '=' expression ';'
	private void compileLetStatement() {
		output.add("<letStatement>");

		output.add("</letStatement>");
	}

	private void compileWhileStatement() {

	}

	// 'if' '(' expression ')' '{' statements '}' ( 'else' '{' statements '}' )?
	private void compileIfStatement() {
		output.add("<ifStatement>");

		output.add("</ifStatement>");
	}

	private void compileDoStatement() {

	}

	private void compileReturnStatement() {

	}

	private void compileExpression() {

	}

	private void compileTerm() {

	}

	private void eat(Lexical token) {
		output.add(token.toString());
	}
}
