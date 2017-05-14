package com.tamco.hack.assembler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tam-co on 13/05/2017.
 */
public class Assembler {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("\nPlease specify file path to compile\n");
			return;
		}

		Map<String, Integer> symbolTable = new HashMap<>();
		predefinedSymbols(symbolTable);

		File sourceCode = new File(args[0]);
		Parser parser = new Parser();
		CodeGenerator generator = new CodeGenerator();

		try {
			List<String> commands = parser.parse(sourceCode, symbolTable);
			List<String> instructions = generator.generateCode(commands, symbolTable);

			String sourceFileName = sourceCode.getName();
			int dotIndex = sourceFileName.indexOf(".");
			String targetFileName = sourceFileName.substring(0, dotIndex) + ".hack";
			File targetFile = new File(targetFileName);

			FileWriter fileWriter = new FileWriter(targetFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (String instruction : instructions) {
				bufferedWriter.write(instruction + "\n");
			}
			bufferedWriter.close();
			System.out.println("Compilation succeeded");

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());

		} catch (SyntaxException e) {
			System.out.println("SyntaxException: " + e.getMessage());
		}
	}

	private static void predefinedSymbols(Map<String, Integer> symbolTable) {
		symbolTable.put("SP", Integer.valueOf(0));
		symbolTable.put("LCL", Integer.valueOf(1));
		symbolTable.put("ARG", Integer.valueOf(2));
		symbolTable.put("THIS", Integer.valueOf(3));
		symbolTable.put("THAT", Integer.valueOf(4));

		symbolTable.put("R0", Integer.valueOf(0));
		symbolTable.put("R1", Integer.valueOf(1));
		symbolTable.put("R2", Integer.valueOf(2));
		symbolTable.put("R3", Integer.valueOf(3));
		symbolTable.put("R4", Integer.valueOf(4));
		symbolTable.put("R5", Integer.valueOf(5));
		symbolTable.put("R6", Integer.valueOf(6));
		symbolTable.put("R7", Integer.valueOf(7));
		symbolTable.put("R8", Integer.valueOf(8));
		symbolTable.put("R9", Integer.valueOf(9));
		symbolTable.put("R10", Integer.valueOf(10));
		symbolTable.put("R11", Integer.valueOf(11));
		symbolTable.put("R12", Integer.valueOf(12));
		symbolTable.put("R13", Integer.valueOf(13));
		symbolTable.put("R14", Integer.valueOf(14));
		symbolTable.put("R15", Integer.valueOf(15));

		symbolTable.put("SCREEN", Integer.valueOf(16384));
		symbolTable.put("KBD", Integer.valueOf(24576));
	}
}
