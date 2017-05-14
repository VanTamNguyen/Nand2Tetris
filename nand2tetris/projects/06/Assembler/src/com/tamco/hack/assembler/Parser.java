package com.tamco.hack.assembler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tam-co on 13/05/2017.
 */
public class Parser {

	public Parser() {
	}

	public List<String> parse(File sourceCode, Map<String, Integer> symbolTable) throws IOException, SyntaxException {
		int variableLocation = 16;
		int removedLabels = 0;

		List<String> commands = eliminateCommentsAndSpaces(sourceCode);

		for (int i = 0; i < commands.size(); i++) {
			String command = commands.get(i);

			if (command.startsWith("(")) {
				if (command.endsWith(")")) {
					String label = command.substring(1, command.indexOf(")"));
					symbolTable.put(label, Integer.valueOf(i - removedLabels));
					removedLabels = removedLabels + 1;

				} else {
					throw new SyntaxException("SyntaxException at command: " + command);
				}
			}
		}

		for (int i = 0; i < commands.size(); i++) {
			String command = commands.get(i);

			if (command.startsWith("@")) {
				String instructionA = command.substring(1);

				try {
					Integer.parseInt(instructionA);
				} catch (NumberFormatException e) {
					symbolTable.putIfAbsent(instructionA, variableLocation);
					variableLocation++;
				}
			}
		}

		Iterator<String> iterator = commands.iterator();
		while (iterator.hasNext()) {
			String command = iterator.next();
			if (command.startsWith("(")) {
				iterator.remove();
			}
		}

		return commands;
	}

	private List<String> eliminateCommentsAndSpaces(File sourceCode) throws IOException {
		List<String> commandsAndLabels = new ArrayList<>();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(sourceCode));
		String line = "";

		while ((line = bufferedReader.readLine()) != null) {
			String command = removeSpaces(removeComment(line));
			if (!("".equals(command))) {
				commandsAndLabels.add(command);
			}
		}

		return commandsAndLabels;
	}

	private String removeComment(String line) {
		int index = line.indexOf("//");
		if (index > -1) {
			return line.substring(0, index);
		}

		return line;
	}

	private String removeSpaces(String line) {
		return line.replaceAll(" ", "").replaceAll("\\t", "");
	}
}
