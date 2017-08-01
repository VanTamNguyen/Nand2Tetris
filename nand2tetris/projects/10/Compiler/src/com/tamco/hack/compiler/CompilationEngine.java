package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.util.List;

/**
 * Created by tam-co on 12/07/2017.
 */
public class CompilationEngine {

	private List<Lexical> tokens;

	private int count = 0;

	private List<String> output;

	public CompilationEngine(List<Lexical> tokens, List<String> output) {
		this.tokens = tokens;
		this.output = output;
	}

	public void compile() {
		Lexical token = nextToken();
		if (token.getLecical() == "class") {
			// class
			eat(token);

			// className
			token = nextToken();
			eat(token);

			// symbol {
			token = nextToken();
			eat(token);

			compileClass();

			// symbol }
			token = nextToken();
			eat(token);
		}
	}

	private void compileClass() {
		Lexical token = nextToken();
		while (token.getLecical() == "static" || token.getLecical() == "field") {
			compileClassVarDec();
			token = nextToken();
		}

		
	}

	private void compileClassVarDec() {

	}

	private void compileSubroutineDec() {

	}

	private void eat(Lexical token) {
		output.add(token.toString());
	}

	private Lexical nextToken() {
		Lexical lexical = tokens.get(count);
		count++;
		return lexical;
	}
}
