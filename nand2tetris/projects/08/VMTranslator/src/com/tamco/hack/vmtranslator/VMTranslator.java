package com.tamco.hack.vmtranslator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

		File sourceCode = new File("../MemoryAccess");
		translateSrc(sourceCode, "src");
	}

	private static void translateSrc(File sourceCode, String folderPath) {
		if (sourceCode.isDirectory()) {
			File[] files = sourceCode.listFiles();
			for (File file : files) {
				String path = sourceCode.isDirectory() ? folderPath + "." + sourceCode.getName() : folderPath;
				translateSrc(file, path);
			}

		} else if (sourceCode.isFile()) {
			if (sourceCode.getName().endsWith(".vm")) {
				String vmName = folderPath + "." + sourceCode.getName();
				System.out.println("**************** Start translating " + vmName + " ****************");

				Parser parser = new Parser();
				CodeGenerator generator = new CodeGenerator(vmName);

				try {
					List<String> vmCommands = parser.parse(sourceCode);
					List<String> asmCommands = generator.generateAsmCode(vmCommands);

					String sourceFileName = sourceCode.getName();
					File sourceParent = sourceCode.getParentFile();
					int dotIndex = sourceFileName.indexOf(".");
					String targetFileName = sourceFileName.substring(0, dotIndex) + ".asm";
					File targetFile = new File(sourceParent, targetFileName);

					FileWriter fileWriter = new FileWriter(targetFile);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					for (String asm : asmCommands) {
						bufferedWriter.write(asm + "\n");
					}
					bufferedWriter.close();

				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());

				} catch (SyntaxException e) {
					System.out.println(e.getMessage());
				}

				System.out.println("**************** End   translating " + vmName + " ****************");
				System.out.println();
			}
		}
	}
}
