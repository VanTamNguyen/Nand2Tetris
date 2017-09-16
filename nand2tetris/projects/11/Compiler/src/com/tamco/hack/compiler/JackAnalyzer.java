package com.tamco.hack.compiler;

import com.tamco.hack.compiler.lexical.Lexical;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
			parseSource(src1);
			parseSource(src2);
			parseSource(src3);
		} catch (IOException e) {
		}
	}

	private static void parseSource(File sourceFolder) throws IOException {
		File[] sourceFiles = sourceFolder.listFiles();
		for (File sourceFile : sourceFiles) {

			if (sourceFile.getName().endsWith(".jack")) {
				// Tokenize the source code file
				JackTokenizer tokenizer = new JackTokenizer(sourceFile);
				List<Lexical> tokens = tokenizer.tokenize();

				// Parse the source code file
				List<String> output = new ArrayList<>();
				CompilationEngine compilationEngine = new CompilationEngine(tokens, output);
				compilationEngine.compileClass();

				StringBuilder content = new StringBuilder("");
				output.forEach(line -> {
					content.append(line).append("\n");
				});

				String outputFileName = sourceFile.getName() + ".xml";
				File outputFile = new File(sourceFile.getParent(), outputFileName);
				FileWriter fileWriter = new FileWriter(outputFile);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(content.toString());
				bufferedWriter.close();
			}
		}
	}
}
