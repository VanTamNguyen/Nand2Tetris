package com.tamco.hack.vmtranslator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tam-co on 24/05/2017.
 */
public class CodeGenerator {

	private static final String SPACE = " ";

	public CodeGenerator() {}

	public List<String> generateAsmCode(List<String> vmCommands) throws SyntaxException {
		List<String> asmCommands = new ArrayList<>();

		for (String vmCommand : vmCommands) {
			asmCommands.addAll(translateVMtoASM(vmCommand));
		}

		return asmCommands;
	}

	private List<String> translateVMtoASM(String vmCommand) throws SyntaxException {
		String[] elements = vmCommand.split(SPACE);

		if (elements.length == 1) {
			return translateArithmeticLogicCommand(elements);

		} else if (elements.length == 2) {
			return Collections.emptyList();

		} else if (elements.length == 3) {
			return Collections.emptyList();

		} else {
			throw new SyntaxException("SyntaxException at command: " + vmCommand);
		}
	}

	private List<String> translateArithmeticLogicCommand(String[] elements) throws SyntaxException {
		String command = elements[0];

		if ("add".equals(command)) {
			return translateCommandAdd();

		} else if ("sub".equals(command)) {
			return translateCommandSub();

		} else if ("neg".equals(command)) {
			return translateCommandNeg();

		} else if ("eq".equals(command)) {
			return translateCommandEQ();

		} else if ("gt".equals(command)) {
			return translateCommandGT();

		} else if ("lt".equals(command)) {
			return translateCommandLT();

		} else if ("and".equals(command)) {
			return translateCommandAnd();

		} else if ("or".equals(command)) {
			return translateCommandOr();

		} else if ("not".equals(command)) {
			return translateCommandNot();

		} else {
			throw new SyntaxException("SyntaxException at command: " + command);
		}
	}

	private List<String> translateCommandAdd() {
		List<String> asms = new ArrayList<>();

		// Load second operand
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		// Load first operand
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");

		// Add
		asms.add("@R13");
		asms.add("D=D+M");


		// Store the result
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=D");

		return asms;
	}

	private List<String> translateCommandSub() {
		List<String> asms = new ArrayList<>();

		// Load second operand
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		// Load first operand
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");

		// Add
		asms.add("@R13");
		asms.add("D=D-M");


		// Store the result
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=D");

		return asms;
	}

	private List<String> translateCommandNeg() {
		List<String> asms = new ArrayList<>();

		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=-M");

		return asms;
	}

	private List<String> translateCommandEQ() {
		List<String> asms = new ArrayList<>();

		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");

		return asms;
	}

	private List<String> translateCommandGT() {
		List<String> asms = new ArrayList<>();

		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");

		return asms;
	}

	private List<String> translateCommandLT() {
		List<String> asms = new ArrayList<>();

		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");

		return asms;
	}

	private List<String> translateCommandAnd() {
		List<String> asms = new ArrayList<>();

		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");

		return asms;
	}

	private List<String> translateCommandOr() {
		List<String> asms = new ArrayList<>();

		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");

		return asms;
	}

	private List<String> translateCommandNot() {
		List<String> asms = new ArrayList<>();

		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");
		asms.add("");

		return asms;
	}
}
