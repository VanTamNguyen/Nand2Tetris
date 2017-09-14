package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by tam-co on 12/07/2017.
 */
public class JackAnalyzer {

	public static void main(String[] args) {
		File src1 = new File("../ArrayTest");
		File src2 = new File("../Square");
		File src3 = new File("../ExpressionLessSquare");

		try {
			tokenSource(src1);
			tokenSource(src2);
			tokenSource(src3);
		} catch (IOException e) {
		}
	}

	private static void tokenSource(File sourceFolder) throws IOException {
		File[] sourceFiles = sourceFolder.listFiles();
		for (File sourceFile : sourceFiles) {
			if (sourceFile.getName().endsWith(".jack")) {
				JackTokenizer tokenizer = new JackTokenizer(sourceFile);
				List<Lexical> tokens = tokenizer.tokenize();

				StringBuilder output = new StringBuilder("");
				for (Lexical token : tokens) {
					output.append(token.toString()).append("\n");
				}

				String outputFileName = sourceFile.getName() + ".xml";
				File outputFile = new File(sourceFile.getParent(), outputFileName);
				FileWriter fileWriter = new FileWriter(outputFile);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(output.toString());
				bufferedWriter.close();
			}
		}
	}

	private static void parseSource(File sourceFolder) {
		File[] sourceFiles = sourceFolder.listFiles();
		for (File sourceFile : sourceFiles) {

		}
	}
}
