package com.tamco.hack.vmtranslator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tam-co on 24/05/2017.
 */
public class VMTranslator {

	public static void main(String[] args) {
		File src1 = new File("../ProgramFlow/BasicLoop");
		File src2 = new File("../ProgramFlow/FibonacciSeries");
		File src3 = new File("../FunctionCalls/FibonacciElement");
		File src4 = new File("../FunctionCalls/NestedCall");
		File src5 = new File("../FunctionCalls/SimpleFunction");
		File src6 = new File("../FunctionCalls/StaticsTest");

		translateSrc(src1);
		translateSrc(src2);
		translateSrc(src3);
		translateSrc(src4);
		translateSrc(src5);
		translateSrc(src6);
	}

	private static void translateSrc(File folder) {
		File[] files = folder.listFiles();
		Map<String, List<String>> map = new HashMap<>();

		for (File file : files) {
			if (file.getName().endsWith(".vm")) {
				String vmName = file.getName();

				System.out.println("**************** Start translating " + vmName + " ****************");

				Parser parser = new Parser();
				CodeGenerator generator = new CodeGenerator(vmName);

				try {
					List<String> vmCommands = parser.parse(file);
					List<String> asmCommands = generator.generateAsmCode(vmCommands);
					map.put(vmName, asmCommands);

				} catch (IOException e) {
					System.out.println("IOException: " + e.getMessage());

				} catch (SyntaxException e) {
					System.out.println(e.getMessage());
				}

				System.out.println("**************** End   translating " + vmName + " ****************");
				System.out.println();
			}
		}

		try {
			File targetFile = new File(folder, folder.getName() + ".asm");

			List<String> allAsmCommands = new ArrayList<>();
			if (map.containsKey("Sys.vm")) {
				allAsmCommands.add("// Bootstrap code");
				allAsmCommands.add("@256");
				allAsmCommands.add("D=A");
				allAsmCommands.add("@SP");
				allAsmCommands.add("M=D");
				allAsmCommands.addAll(translateCommandCall("Sys.init", 0));
				allAsmCommands.addAll(map.get("Sys.vm"));
			}

			map.keySet().forEach(key -> {
				if (!key.equals("Sys.vm")) {
					allAsmCommands.addAll(map.get(key));
				}
			});

			FileWriter fileWriter = new FileWriter(targetFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (String asm : allAsmCommands) {
				bufferedWriter.write(asm + "\n");
			}
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}

	private static List<String> translateCommandCall(String function, int nArgs) {
		List<String> asms = new ArrayList<>();
		// add comment
		asms.add("// call " + function + " " + nArgs);

		String returnAddress = "after-finishing-all-tasks";

		// 1. push return address
		asms.add("@" + returnAddress);
		asms.add("D=A");
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");
		asms.add("@SP");
		asms.add("M=M+1");

		// 2. push LCL
		asms.add("@LCL");
		asms.add("D=M");
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");
		asms.add("@SP");
		asms.add("M=M+1");

		// 3. push ARG
		asms.add("@ARG");
		asms.add("D=M");
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");
		asms.add("@SP");
		asms.add("M=M+1");

		// 4. push THIS
		asms.add("@THIS");
		asms.add("D=M");
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");
		asms.add("@SP");
		asms.add("M=M+1");

		// 5. push THAT
		asms.add("@THAT");
		asms.add("D=M");
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");
		asms.add("@SP");
		asms.add("M=M+1");

		// 6. calculate ARG for called function
		asms.add("@5");
		asms.add("D=A");
		asms.add("@" + nArgs);
		asms.add("D=D+A");
		asms.add("@SP");
		asms.add("D=M-D");
		asms.add("@ARG");
		asms.add("M=D");

		// 7. calculate LCL for called function
		asms.add("@SP");
		asms.add("D=M");
		asms.add("@LCL");
		asms.add("M=D");

		// 8. go to function
		asms.add("@" + function);
		asms.add("0;JMP");

		// 9. mark return address
		asms.add("(" + returnAddress + ")");

		return asms;
	}
}
