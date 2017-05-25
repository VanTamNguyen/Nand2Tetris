package com.tamco.hack.vmtranslator;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by tam-co on 24/05/2017.
 */
public class VMTranslator {

	public static void main(String[] args) {
//		if (args.length < 1) {
//			System.out.println("\nPlease specify file path to compile\n");
//			return;
//		}

		File sourceCode = new File("BasicTest.vm");
		Parser parser = new Parser();
		CodeGenerator generator = new CodeGenerator();

		try {
			List<String> vmCommands = parser.parse(sourceCode);

		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}
