package com.tamco.hack.compiler.lexical;

/**
 * Created by tam-co on 12/07/2017.
 */
public class Lexical {

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
}
