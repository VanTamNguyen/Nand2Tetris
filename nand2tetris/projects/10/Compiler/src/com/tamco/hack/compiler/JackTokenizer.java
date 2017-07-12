package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tam-co on 12/07/2017.
 */

public class JackTokenizer {

	private File sourceCode;

	public JackTokenizer(File sourceCode) {
		this.sourceCode = sourceCode;
	}

	public List<Lexical> tokenize() {
		List<Lexical> tokens = new ArrayList<>();
		return tokens;
	}
}
