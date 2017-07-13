package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

	public List<Lexical> tokenize() throws IOException {
		List<Lexical> tokens = new ArrayList<>();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceCode));
		String line = "";
		StringBuilder sourceContent = new StringBuilder();

		while ((line = bufferedReader.readLine()) != null) {
			sourceContent.append(removeLineComment(line)).append("\n");
		}

		// Remove all comments
		String code = sourceContent.toString().replaceAll("/\\*.*\\*/", " ");
		StringBuilder lexical = new StringBuilder("");
		boolean startStringConstant = false;

		for (char character : code.toCharArray()) {


			if (isSpace(character)) {
				if (!lexical.toString().isEmpty()) {
					tokens.add(Lexical.fromString(lexical.toString()));
				}

				lexical = new StringBuilder("");

			} else if (isSymbol(character)) {
				if (!lexical.toString().isEmpty()) {
					tokens.add(Lexical.fromString(lexical.toString()));
				}

				Lexical symbol = new Lexical(Lexical.Type.symbol, String.valueOf(character));
				tokens.add(symbol);

				lexical = new StringBuilder("");

			} else {
				lexical.append(String.valueOf(character));
			}
		}

		return tokens;
	}

	private String removeLineComment(String line) {
		int index = line.indexOf("//");
		if (index > -1) {
			return line.substring(0, index);
		}

		return line;
	}

	private boolean isSpace(char character) {
		if (character == ' ' || character == '\t' || character == '\n') {
			return true;
		}

		return false;
	}

	private boolean isSymbol(char character) {
		return Lexical.symbols.contains(String.valueOf(character));
	}
}
