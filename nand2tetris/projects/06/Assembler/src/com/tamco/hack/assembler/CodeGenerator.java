package com.tamco.hack.assembler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tam-co on 13/05/2017.
 */
public class CodeGenerator {

	public CodeGenerator() {
	}

	public List<String> generateCode(List<String> commands, Map<String, Integer> symbolTable) throws SyntaxException {
		List<String> instructions= new ArrayList<>();

		for (int i = 0; i < commands.size(); i++) {
			String command = commands.get(i);
			String instruction;

			if (command.startsWith("@")) {
				instruction = generateInstructionA(command, symbolTable);
			} else {
				instruction = generateInstructionC(command);
			}

			instructions.add(instruction);
		}

		return instructions;
	}

	private String generateInstructionA(String command, Map<String, Integer> symbolTable) throws SyntaxException {
		String instruction = "";
		String content = command.substring(1);
		int address;

		try {
			address = Integer.parseInt(content);
		} catch (NumberFormatException e) {
			if (symbolTable.containsKey(content)) {
				address = symbolTable.get(content).intValue();
			} else {
				throw new SyntaxException("SyntaxException at command: " + command);
			}
		}

		String addressInBinary = Integer.toBinaryString(address);
		int leadingZeros = 16 - addressInBinary.length();
		for (int i = 0; i < leadingZeros; i++) {
			instruction += "0";
		}
		instruction += addressInBinary;

		return instruction;
	}

	private String generateInstructionC(String command) throws SyntaxException {
		String instruction = "111";
		String aBit;
		String destBits;
		String compBits;
		String jumpBits;

		int equalIndex = command.indexOf("=");
		int semicolonIndex = command.indexOf(";");
		int compFirstIndex = equalIndex + 1;
		int compLastIndex = semicolonIndex > -1 ? semicolonIndex : command.length();

		String dest = "";
		String jump = "";
		String comp = command.substring(compFirstIndex, compLastIndex);

		if (equalIndex > -1) {
			dest = command.substring(0, equalIndex);
		}

		if (semicolonIndex > -1) {
			jump = command.substring(semicolonIndex + 1);
		}

		if ("".equals(dest)) {
			destBits = "000";
		} else if ("M".equals(dest)) {
			destBits = "001";
		} else if ("D".equals(dest)) {
			destBits = "010";
		} else if ("MD".equals(dest)) {
			destBits = "011";
		} else if ("A".equals(dest)) {
			destBits = "100";
		} else if ("AM".equals(dest)) {
			destBits = "101";
		} else if ("AD".equals(dest)) {
			destBits = "110";
		} else if ("AMD".equals(dest)) {
			destBits = "111";
		} else {
			throw new SyntaxException("SyntaxException at command: " + command);
		}

		if ("".equals(jump)) {
			jumpBits = "000";
		} else if ("JGT".equals(jump)) {
			jumpBits = "001";
		} else if ("JEQ".equals(jump)) {
			jumpBits = "010";
		} else if ("JGE".equals(jump)) {
			jumpBits = "011";
		} else if ("JLT".equals(jump)) {
			jumpBits = "100";
		} else if ("JNE".equals(jump)) {
			jumpBits = "101";
		} else if ("JLE".equals(jump)) {
			jumpBits = "110";
		} else if ("JMP".equals(jump)) {
			jumpBits = "111";
		} else {
			throw new SyntaxException("SyntaxException at command: " + command);
		}

		if ("0".equals(comp)) {
			aBit = "0";
			compBits = "101010";

		} else if ("1".equals(comp)) {
			aBit = "0";
			compBits = "111111";

		} else if ("-1".equals(comp)) {
			aBit = "0";
			compBits = "111010";

		} else if ("D".equals(comp)) {
			aBit = "0";
			compBits = "001100";

		} else if ("A".equals(comp)) {
			aBit = "0";
			compBits = "110000";

		} else if ("!D".equals(comp)) {
			aBit = "0";
			compBits = "001101";

		} else if ("!A".equals(comp)) {
			aBit = "0";
			compBits = "110001";

		} else if ("-D".equals(comp)) {
			aBit = "0";
			compBits = "001111";

		} else if ("-A".equals(comp)) {
			aBit = "0";
			compBits = "110011";

		} else if ("D+1".equals(comp)) {
			aBit = "0";
			compBits = "011111";

		} else if ("A+1".equals(comp)) {
			aBit = "0";
			compBits = "110111";

		} else if ("D-1".equals(comp)) {
			aBit = "0";
			compBits = "001110";

		} else if ("A-1".equals(comp)) {
			aBit = "0";
			compBits = "110010";

		} else if ("D+A".equals(comp)) {
			aBit = "0";
			compBits = "000010";

		} else if ("D-A".equals(comp)) {
			aBit = "0";
			compBits = "010011";

		} else if ("A-D".equals(comp)) {
			aBit = "0";
			compBits = "000111";

		} else if ("D&A".equals(comp)) {
			aBit = "0";
			compBits = "000000";

		} else if ("D|A".equals(comp)) {
			aBit = "0";
			compBits = "010101";

		} else if ("M".equals(comp)) { // D & M
			aBit = "1";
			compBits = "110000";

		} else if ("!M".equals(comp)) { // D & M
			aBit = "1";
			compBits = "110001";

		} else if ("-M".equals(comp)) { // D & M
			aBit = "1";
			compBits = "110011";

		} else if ("M+1".equals(comp)) { // D & M
			aBit = "1";
			compBits = "110111";

		} else if ("M-1".equals(comp)) { // D & M
			aBit = "1";
			compBits = "110010";

		} else if ("D+M".equals(comp)) { // D & M
			aBit = "1";
			compBits = "000010";

		} else if ("D-M".equals(comp)) { // D & M
			aBit = "1";
			compBits = "010011";

		} else if ("M-D".equals(comp)) { // D & M
			aBit = "1";
			compBits = "000111";

		} else if ("D&M".equals(comp)) { // D & M
			aBit = "1";
			compBits = "000000";

		} else if ("D|M".equals(comp)) { // D & M
			aBit = "1";
			compBits = "010101";

		} else {
			throw new SyntaxException("SyntaxException at command: " + command);
		}

		instruction += aBit + compBits + destBits + jumpBits;

		return instruction;
	}
}
