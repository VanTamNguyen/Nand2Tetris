package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.util.List;

/**
 * Created by tam-co on 12/07/2017.
 */
public class CompilationEngine {

	private List<Lexical> tokens;

	private List<String> output;

	private int count = -1;

	private Lexical currentToken;

	public CompilationEngine(List<Lexical> tokens, List<String> output) {
		this.tokens = tokens;
		this.output = output;
	}

	// 'class' className '{' classVarDec* subroutineDec* '}'
	public void compileClass() {
		output.add("<class>");

		// 'class'
		goNext();
		eat(currentToken);

		// className
		goNext();
		eat(currentToken);

		// '{'
		goNext();
		eat(currentToken);

		goNext();
		while (isClassVarDec(currentToken) || isClassSubroutineDec(currentToken)) {
			if (isClassVarDec(currentToken)) {
				goBack();
				compileClassVarDec();

			} else if (isClassSubroutineDec(currentToken)) {
				goBack();
				compileSubroutineDec();
			}

			goNext();
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
		goNext();
		eat(currentToken);

		// type
		goNext();
		eat(currentToken);

		// varName
		goNext();
		eat(currentToken);

		goNext();
		while (currentToken.getLecical() == ",") {
			// ','
			eat(currentToken);

			// varName
			goNext();
			eat(currentToken);

			goNext();
		}

		// ';'
		eat(currentToken);

		output.add("</classVarDec>");
	}

	// ( 'constructor' | 'function' | 'method' ) ( 'void' | type ) subroutineName '(' parameterList ')' subroutineBody
	private void compileSubroutineDec() {
		output.add("<subroutineDec>");

		// 'constructor' | 'function' | 'method'
		goNext();
		eat(currentToken);

		// 'void' | type
		goNext();
		eat(currentToken);

		// subroutineName
		goNext();
		eat(currentToken);

		// '('
		goNext();
		eat(currentToken);

		// parameterList
		compileParameterList();

		// ')'
		goNext();
		eat(currentToken);

		// subroutineBody
		compileSubroutineBody();

		output.add("</subroutineDec>");
	}

	// ((type varName) ( ',' type varName)*)?
	private void compileParameterList() {
		output.add("<parameterList>");

		goNext();
		if (currentToken.getLecical() != ")") {
			// type
			eat(currentToken);

			// varName
			goNext();
			eat(currentToken);

			goNext();
			while (currentToken.getLecical() == ",") {
				// ','
				eat(currentToken);

				// type
				goNext();
				eat(currentToken);

				// varName
				goNext();
				eat(currentToken);

				goNext();
			}
		}

		goBack();

		output.add("</parameterList>");
	}

	// '{' varDec* statements '}'
	private void compileSubroutineBody() {
		output.add("<subroutineBody>");

		// '{'
		goNext();
		eat(currentToken);

		// varDec*
		goNext();
		while (isVarDec(currentToken)) {
			goBack();
			compileVarDec();
			goNext();
		}

		// statements
		goBack();
		compileStatements();

		// '}'
		goNext();
		eat(currentToken);

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

		// 'var'
		goNext();
		eat(currentToken);

		// type
		goNext();
		eat(currentToken);

		// varName
		goNext();
		eat(currentToken);

		goNext();
		while (currentToken.getLecical() == ",") {
			// ','
			eat(currentToken);

			// varName
			goNext();
			eat(currentToken);

			goNext();
		}

		// ';'
		eat(currentToken);

		output.add("</varDec>");
	}

	// statement*
	private void compileStatements() {
		output.add("<statements>");

		goNext();
		while (isStatement(currentToken)) {
			goBack();
			compileStatement();
			goNext();
		}

		goBack();

		output.add("</statements>");
	}

	private void compileStatement() {
		goNext();

		if (currentToken.getLecical() == "let") {
			goBack();
			compileLetStatement();

		} else if (currentToken.getLecical() == "if") {
			goBack();
			compileIfStatement();

		} else if (currentToken.getLecical() == "while") {
			goBack();
			compileWhileStatement();

		} else if (currentToken.getLecical() == "do") {
			goBack();
			compileDoStatement();

		} else if (currentToken.getLecical() == "return") {
			goBack();
			compileReturnStatement();
		}
	}

	// 'let' varName ('[' expression ']')? '=' expression ';'
	private void compileLetStatement() {
		output.add("<letStatement>");

		// 'let'
		goNext();
		eat(currentToken);

		// varName
		goNext();
		eat(currentToken);

		goNext();
		if (currentToken.getLecical() == "[") {
			// '['
			eat(currentToken);

			// expression
			compileExpression();

			// ']'
			goNext();
			eat(currentToken);
		}

		// '='
		eat(currentToken);

		// expression
		compileExpression();

		// ';'
		goNext();
		eat(currentToken);

		output.add("</letStatement>");
	}

	// 'if' '(' expression ')' '{' statements '}' ( 'else' '{' statements '}' )?
	private void compileIfStatement() {
		output.add("<ifStatement>");

		// 'if'
		goNext();
		eat(currentToken);

		// '('
		goNext();
		eat(currentToken);

		// expression
		compileExpression();

		// ')'
		goNext();
		eat(currentToken);

		// '{'
		goNext();
		eat(currentToken);

		// statements
		compileStatements();

		// '}'
		goNext();
		eat(currentToken);

		goNext();
		if (currentToken.getLecical() == "else") {
			// 'else'
			eat(currentToken);

			// '{'
			goNext();
			eat(currentToken);

			// statements
			compileStatements();

			// '}'
			goNext();
			eat(currentToken);

		} else {
			goBack();
		}

		output.add("</ifStatement>");
	}

	// 'while' '(' expression ')' '{' statements '}'
	private void compileWhileStatement() {
		output.add("<whileStatement>");

		// 'while'
		goNext();
		eat(currentToken);

		// '('
		goNext();
		eat(currentToken);

		// expression
		compileExpression();

		// ')'
		goNext();
		eat(currentToken);

		// '{'
		goNext();
		eat(currentToken);

		// statements
		compileStatements();

		// '}'
		goNext();
		eat(currentToken);

		output.add("</whileStatement>");
	}

	// 'do' subroutineCall ';'
	private void compileDoStatement() {
		output.add("<doStatement>");

		// 'do'
		goNext();
		eat(currentToken);

		// subroutineCall
		compileSubroutineCall();

		// ';'
		goNext();
		eat(currentToken);

		output.add("</doStatement>");
	}

	// 'return' expression? ';'
	private void compileReturnStatement() {
		output.add("<returnStatement>");

		// 'return'
		goNext();
		eat(currentToken);

		goNext();
		if (currentToken.getLecical() != ";") {
			// expression
			goBack();
			compileExpression();

			goNext();
		}

		// ';'
		eat(currentToken);

		output.add("</returnStatement>");
	}

	// term (op term)*
	private void compileExpression() {
		output.add("<expression>");

		output.add("</expression>");
	}

	// integerConstant | stringConstant | keywordConstant | varName | varName '[' expression ']' |
	// subroutineCall | '(' expression ')' | unaryOp term
	private void compileTerm() {
		output.add("<term>");

		output.add("</term>");
	}

	// subroutineName '(' expressionList ')' | (className | varName) '.' subroutineName '(' expressionList ')'
	private void compileSubroutineCall() {

	}

	// (expression ( ',' expression)* )?
	private void compileExpressionList() {
		output.add("<expressionList>");

		output.add("</expressionList>");
	}

	private void eat(Lexical token) {
		output.add(token.toString());
	}

	private void goNext() {
		count++;
		currentToken = tokens.get(count);
	}

	private void goBack() {
		count--;
		currentToken = tokens.get(count);
	}
}
