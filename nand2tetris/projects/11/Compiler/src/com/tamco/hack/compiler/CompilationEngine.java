package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;
import com.tamco.hack.compiler.symbol.Symbol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tam-co on 12/07/2017.
 */
public class CompilationEngine {

	private String clazzName;

	private List<Lexical> tokens;

	private List<String> vmCommands;

	private int count = -1;

	private Lexical currentToken;

	private Map<String, Symbol> clazzSymbolTable;

	public CompilationEngine(String clazzName, List<Lexical> tokens, List<String> vmCommands) {
		this.clazzName = clazzName;
		this.tokens = tokens;
		this.vmCommands = vmCommands;
		this.clazzSymbolTable = new HashMap<>();
	}

	// 'class' className '{' classVarDec* subroutineDec* '}'
	public void compileClass() {
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
	}

	private boolean isClassVarDec(Lexical token) {
		return token.getLecical().equals("static") || token.getLecical().equals("field");
	}

	private boolean isClassSubroutineDec(Lexical token) {
		return token.getLecical().equals("constructor") || token.getLecical().equals("function") ||
				token.getLecical().equals("method");
	}

	// ( 'static' | 'field' ) type varName ( ',' varName)* ';'
	private void compileClassVarDec() {
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
		while (currentToken.getLecical().equals(",")) {
			// ','
			eat(currentToken);

			// varName
			goNext();
			eat(currentToken);

			goNext();
		}

		// ';'
		eat(currentToken);
	}

	// ( 'constructor' | 'function' | 'method' ) ( 'void' | type ) subroutineName '(' parameterList ')' subroutineBody
	private void compileSubroutineDec() {
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
	}

	// ((type varName) ( ',' type varName)*)?
	private void compileParameterList() {
		goNext();
		if (!currentToken.getLecical().equals(")")) {
			// type
			eat(currentToken);

			// varName
			goNext();
			eat(currentToken);

			goNext();
			while (currentToken.getLecical().equals(",")) {
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
	}

	// '{' varDec* statements '}'
	private void compileSubroutineBody() {
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
	}

	private boolean isVarDec(Lexical token) {
		return token.getLecical().equals("var");
	}

	private boolean isStatement(Lexical token) {
		return token.getLecical().equals("let") ||
				token.getLecical().equals("if") ||
				token.getLecical().equals("while") ||
				token.getLecical().equals("do") ||
				token.getLecical().equals("return");
	}

	// 'var' type varName ( ',' varName)* ';'
	private void compileVarDec() {
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
		while (currentToken.getLecical().equals(",")) {
			// ','
			eat(currentToken);

			// varName
			goNext();
			eat(currentToken);

			goNext();
		}

		// ';'
		eat(currentToken);
	}

	// statement*
	private void compileStatements() {
		goNext();
		while (isStatement(currentToken)) {
			goBack();
			compileStatement();
			goNext();
		}

		goBack();
	}

	private void compileStatement() {
		goNext();

		if (currentToken.getLecical().equals("let")) {
			goBack();
			compileLetStatement();

		} else if (currentToken.getLecical().equals("if")) {
			goBack();
			compileIfStatement();

		} else if (currentToken.getLecical().equals("while")) {
			goBack();
			compileWhileStatement();

		} else if (currentToken.getLecical().equals("do")) {
			goBack();
			compileDoStatement();

		} else if (currentToken.getLecical().equals("return")) {
			goBack();
			compileReturnStatement();
		}
	}

	// 'let' varName ('[' expression ']')? '=' expression ';'
	private void compileLetStatement() {
		// 'let'
		goNext();
		eat(currentToken);

		// varName
		goNext();
		eat(currentToken);

		goNext();
		if (currentToken.getLecical().equals("[")) {
			// '['
			eat(currentToken);

			// expression
			compileExpression();

			// ']'
			goNext();
			eat(currentToken);

			goNext();
		}

		// '='
		eat(currentToken);

		// expression
		compileExpression();

		// ';'
		goNext();
		eat(currentToken);
	}

	// 'if' '(' expression ')' '{' statements '}' ( 'else' '{' statements '}' )?
	private void compileIfStatement() {
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
		if (currentToken.getLecical().equals("else")) {
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
	}

	// 'while' '(' expression ')' '{' statements '}'
	private void compileWhileStatement() {
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
	}

	// 'do' subroutineCall ';'
	private void compileDoStatement() {
		// 'do'
		goNext();
		eat(currentToken);

		// subroutineCall
		compileSubroutineCall();

		// ';'
		goNext();
		eat(currentToken);
	}

	// 'return' expression? ';'
	private void compileReturnStatement() {
		// 'return'
		goNext();
		eat(currentToken);

		goNext();
		if (!currentToken.getLecical().equals(";")) {
			// expression
			goBack();
			compileExpression();

			goNext();
		}

		// ';'
		eat(currentToken);
	}

	// term (op term)*
	private void compileExpression() {
		// term
		compileTerm();

		goNext();
		while (isOp(currentToken)) {
			// op
			eat(currentToken);

			// term
			compileTerm();
			goNext();
		}

		goBack();
	}

	// integerConstant | stringConstant | keywordConstant | varName | varName '[' expression ']' |
	// subroutineCall | '(' expression ')' | unaryOp term
	private void compileTerm() {
		goNext();
		if (currentToken.getType() == Lexical.Type.integerConstant) {
			// Case: integerConstant
			eat(currentToken);

		} else if (currentToken.getType() == Lexical.Type.stringConstant) {
			// Case: stringConstant
			eat(currentToken);

		} else if (isKeywordConstant(currentToken)) {
			// Case: keywordConstant
			eat(currentToken);

		} else if (isUnaryOp(currentToken)){
			// Case: unaryOp term

			// unaryOp
			eat(currentToken);

			// term
			compileTerm();

		} else if (currentToken.getLecical().equals("(")) {
			// Case: '(' expression ')'

			// '('
			eat(currentToken);

			// expression
			compileExpression();

			// ')'
			goNext();
			eat(currentToken);

		} else {
			goNext();
			if (currentToken.getLecical().equals("[")) {
				// Case: varName '[' expression ']'
				goBack();

				// varName
				eat(currentToken);

				// '['
				goNext();
				eat(currentToken);

				// expression
				compileExpression();

				// ']'
				goNext();
				eat(currentToken);

			} else if (currentToken.getLecical().equals("(") || currentToken.getLecical().equals(".")) {
				// Case: subroutineCall
				goBack(); goBack(); // Go to begin to compile subroutineCall
				compileSubroutineCall();

			} else {
				// Case: varName
				goBack();

				// varName
				eat(currentToken);
			}
		}
	}

	// subroutineName '(' expressionList ')' | (className | varName) '.' subroutineName '(' expressionList ')'
	private void compileSubroutineCall() {
		goNext();

		// subroutineName | (className | varName)
		eat(currentToken);

		goNext();
		if (currentToken.getLecical().equals("(")) { // Case: subroutineName '(' expressionList ')'
			// '('
			eat(currentToken);

			// expressionList
			compileExpressionList();

			// ')'
			goNext();
			eat(currentToken);

		} else if (currentToken.getLecical().equals(".")) { // Case: (className | varName) '.' subroutineName '(' expressionList ')'
			// '.'
			eat(currentToken);

			// subroutineName
			goNext();
			eat(currentToken);

			// '('
			goNext();
			eat(currentToken);

			// expressionList
			compileExpressionList();

			// ')'
			goNext();
			eat(currentToken);
		}
	}

	// (expression ( ',' expression)* )?
	private void compileExpressionList() {
		goNext();
		if (!currentToken.getLecical().equals(")")) {
			goBack();

			// expression
			compileExpression();

			goNext();
			while (currentToken.getLecical().equals(",")) {
				// ','
				eat(currentToken);

				// expression
				compileExpression();

				goNext();
			}

			goBack();

		} else {
			goBack();
		}
	}

	private boolean isOp(Lexical token) {
		return token.getLecical().equals("+") ||
				token.getLecical().equals("-") ||
				token.getLecical().equals("*") ||
				token.getLecical().equals("/") ||
				token.getLecical().equals("&") ||
				token.getLecical().equals("|") ||
				token.getLecical().equals("<") ||
				token.getLecical().equals(">") ||
				token.getLecical().equals("=");
	}

	private boolean isUnaryOp(Lexical token) {
		return token.getLecical().equals("-") || token.getLecical().equals("~");
	}

	private boolean isKeywordConstant(Lexical token) {
		return token.getLecical().equals("true") ||
				token.getLecical().equals("false") ||
				token.getLecical().equals("null") ||
				token.getLecical().equals("this");
	}

	private void eat(Lexical token) {
		// I don't want to erase this method because it makes my compilation engine looks beautiful
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
