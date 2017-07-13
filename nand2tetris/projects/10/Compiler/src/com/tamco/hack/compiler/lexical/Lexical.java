package com.tamco.hack.compiler.lexical;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tam-co on 12/07/2017.
 */
public class Lexical {

	public static final List<String> keywords = Arrays.asList("class", "constructor", "function",
															  "method", "field", "static", "var",
															  "int", "char", "boolean", "void", "true",
															  "false", "null", "this", "let", "do",
															  "if", "else", "while", "return");

	public static final List<String> symbols = Arrays.asList("{", "}", "(", ")", "[", "]", ".",
															 ",", ";", "+", "-", "*", "/", "&",
															 "|", "<", ">", "=", "~");


	public static enum Type {
		keyword,
		symbol,
		integerConstant,
		stringConstant,
		identifier
	}

	private Type type;

	private String lecical;

	public Lexical(Type type, String lecical) {
		this.type = type;
		this.lecical = lecical;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getLecical() {
		return lecical;
	}

	public void setLecical(String lecical) {
		this.lecical = lecical;
	}

	public static Lexical fromString(String token) {
		if (keywords.contains(token)) {
			return new Lexical(Type.keyword, token);
		}

		try {
			Integer.parseInt(token);
			return new Lexical(Type.integerConstant, token);

		} catch (NumberFormatException e) {
			return new Lexical(Type.identifier, token);
		}
	}
}
