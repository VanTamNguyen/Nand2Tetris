package com.tamco.hack.vmtranslator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by tam-co on 24/05/2017.
 */
public class CodeGenerator {

	private static final String SPACE = " ";

	private String vmName;

	private int eqCount = 0;

	private int gtCount = 0;

	private int ltCount = 0;

	private int callCount = 0;

	public CodeGenerator(String vmName) { this.vmName = vmName; }

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
			if ("return".equals(elements[0])) {
				return translateCommandReturn();
			} else {
				return translateArithmeticLogicCommand(elements);
			}

		} else if (elements.length == 2) {
			return translateProgramFlowCommand(elements);

		} else if (elements.length == 3) {
			if ("push".equals(elements[0]) || "pop".equals(elements[0])) {
				return translateMemoryAccessCommand(elements);

			} else if ("call".equals(elements[0])) {
				try {
					int nArgs = Integer.parseInt(elements[2]);
					return translateCommandCall(elements[1], nArgs);
				} catch (NumberFormatException e) {
					throw new SyntaxException("SyntaxException at command: " + vmCommand);
				}

			} else if ("function".equals(elements[0])) {
				try {
					int nVars = Integer.parseInt(elements[2]);
					return translateCommandFunction(elements[1], nVars);
				} catch (NumberFormatException e) {
					throw new SyntaxException("SyntaxException at command: " + vmCommand);
				}

			} else {
				throw new SyntaxException("SyntaxException at command: " + vmCommand);
			}

		} else {
			throw new SyntaxException("SyntaxException at command: " + vmCommand);
		}
	}

	private List<String> translateProgramFlowCommand(String[] elements) throws SyntaxException {
		String command = elements[0];
		String label = elements[1];
		String vmCommand = command + " " + label;

		if ("label".equals(command)) {
			return translateCommandLabel(label);

		} else if ("goto".equals(command)) {
			return translateCommandGoto(label);

		} else if ("if-goto".equals(command)) {
			return translateCommandIfGoto(label);

		} else {
			throw new SyntaxException("SyntaxException at command: " + vmCommand);
		}
	}

	private List<String> translateCommandLabel(String label) {
		List<String> asms = new ArrayList<>();
		// add comment
		asms.add("// label " + label);
		asms.add("(" + label + ")");

		return asms;
	}

	private List<String> translateCommandGoto(String label) {
		List<String> asms = new ArrayList<>();
		// add comment
		asms.add("// goto " + label);

		asms.add("@" + label);
		asms.add("0;JMP");

		return asms;
	}

	private List<String> translateCommandIfGoto(String label) {
		List<String> asms = new ArrayList<>();
		// add comment
		asms.add("// if-goto " + label);

		// pop the top most value
		asms.add("@SP");
		asms.add("AM=M-1");
		// check condition and jump
		asms.add("D=M");
		asms.add("@" + label);
		asms.add("D;JNE");

		return asms;
	}

	private List<String> translateCommandFunction(String function, int nVars) {
		List<String> asms = new ArrayList<>();
		// add comment
		asms.add("// function " + function + " " + nVars);

		asms.add("(" + function + ")");
		for (int i = 0; i < nVars; i++) {
			asms.add("@SP");
			asms.add("A=M");
			asms.add("M=0");
			asms.add("@SP");
			asms.add("M=M+1");
		}

		return asms;
	}

	private List<String> translateCommandCall(String function, int nArgs) {
		List<String> asms = new ArrayList<>();
		// add comment
		asms.add("// call " + function + " " + nArgs);

		String returnAddress = vmName + "-return-address-" + callCount;
		callCount++;

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

	private List<String> translateCommandReturn() {
		List<String> asms = new ArrayList<>();
		// add comment
		asms.add("// return");

		// 1. R13 <--- FRAME = LCL
		asms.add("@LCL");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("M=D");

		// 2. R14 <--- RET = *(FRAME - 5)
		asms.add("@R13");
		asms.add("D=M");
		asms.add("@5");
		asms.add("A=D-A");
		asms.add("D=M");
		asms.add("@R14");
		asms.add("M=D");

		// 3. *ARG = pop()
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@ARG");
		asms.add("A=M");
		asms.add("M=D");

		// 4. SP = ARG + 1
		asms.add("@ARG");
		asms.add("D=M+1");
		asms.add("@SP");
		asms.add("M=D");

		// 5. THAT = *(FRAME - 1)
		asms.add("@R13");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@THAT");
		asms.add("M=D");

		// 6. THIS = *(FRAME - 2)
		asms.add("@R13");
		asms.add("D=M");
		asms.add("@2");
		asms.add("A=D-A");
		asms.add("D=M");
		asms.add("@THIS");
		asms.add("M=D");

		// 7. ARG = *(FRAME - 3)
		asms.add("@R13");
		asms.add("D=M");
		asms.add("@3");
		asms.add("A=D-A");
		asms.add("D=M");
		asms.add("@ARG");
		asms.add("M=D");

		// 8. LCL = *(FRAME - 4)
		asms.add("@R13");
		asms.add("D=M");
		asms.add("@4");
		asms.add("A=D-A");
		asms.add("D=M");
		asms.add("@LCL");
		asms.add("M=D");

		// 9. goto RET
		asms.add("@R14");
		asms.add("A=M");
		asms.add("0;JMP");

		return asms;
	}

	private List<String> translateMemoryAccessCommand(String[] elements) throws SyntaxException {
		String command = elements[0];
		String segment = elements[1];
		String indexStr = elements[2];

		String vmCommand = command + " " + segment + " " + indexStr;

		int index;
		try {
			 index = Integer.parseInt(indexStr);
		} catch (NumberFormatException e) {
			throw new SyntaxException("SyntaxException at command: " + vmCommand);
		}

		if ("push".equals(command)) {
			return translateCommandPush(segment, index);

		} else if ("pop".equals(command)) {
			return translateCommandPop(segment, index);

		} else {
			throw new SyntaxException("SyntaxException at command: " + vmCommand);
		}
	}

	private List<String> translateCommandPush(String segment, int index) throws SyntaxException {
		if ("argument".equals(segment)) {
			return translatePushArgument(index);

		} else if ("local".equals(segment)) {
			return translatePushLocal(index);

		} else if ("this".equals(segment)) {
			return translatePushThis(index);

		} else if ("that".equals(segment)) {
			return translatePushThat(index);

		} else if ("static".equals(segment)) {
			return translatePushStatic(index);

		} else if ("constant".equals(segment)) {
			return translatePushConstant(index);

		} else if ("pointer".equals(segment)) {
			return translatePushPointer(index);

		} else if ("temp".equals(segment)) {
			return translatePushTemp(index);

		} else {
			throw new SyntaxException("SyntaxException at command: push " + segment + " " + index);
		}
	}

	private List<String> translatePushArgument(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push argument " + index);

		// Load (segment + index) content
		asms.add("@ARG");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("A=D+A");
		asms.add("D=M");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translatePushLocal(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push local " + index);

		// Load (segment + index) content
		asms.add("@LCL");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("A=D+A");
		asms.add("D=M");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translatePushThis(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push this " + index);

		// Load (segment + index) content
		asms.add("@THIS");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("A=D+A");
		asms.add("D=M");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translatePushThat(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push that " + index);

		// Load (segment + index) content
		asms.add("@THAT");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("A=D+A");
		asms.add("D=M");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translatePushStatic(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push static " + index);

		// Load (segment + index) content
		asms.add("@" + vmName + ".static." + index);
		asms.add("D=M");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translatePushConstant(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push constant " + index);

		// Load (segment + index) content
		asms.add("@" + index);
		asms.add("D=A");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translatePushPointer(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push pointer " + index);

		// Load (segment + index) content
		asms.add("@3");
		asms.add("D=A");
		asms.add("@" + index);
		asms.add("A=D+A");
		asms.add("D=M");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translatePushTemp(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// push temp " + index);

		// Load (segment + index) content
		asms.add("@5");
		asms.add("D=A");
		asms.add("@" + index);
		asms.add("A=D+A");
		asms.add("D=M");

		// Push to stack
		asms.add("@SP");
		asms.add("A=M");
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M+1");

		return asms;
	}

	private List<String> translateCommandPop(String segment, int index) throws SyntaxException {
		if ("argument".equals(segment)) {
			return translatePopArgument(index);

		} else if ("local".equals(segment)) {
			return translatePopLocal(index);

		} else if ("this".equals(segment)) {
			return translatePopThis(index);

		} else if ("that".equals(segment)) {
			return translatePopThat(index);

		} else if ("static".equals(segment)) {
			return translatePopStatic(index);

		} else if ("pointer".equals(segment)) {
			return translatePopPointer(index);

		} else if ("temp".equals(segment)) {
			return translatePopTemp(index);

		} else {
			throw new SyntaxException("SyntaxException at command: pop " + segment + " " + index);
		}
	}

	private List<String> translatePopArgument(int index) {
		List<String> asms = new ArrayList<>();
		// Add comment to asm
		asms.add("// pop argument " + index);

		//
		asms.add("@ARG");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("D=D+A");
		asms.add("@R13");
		asms.add("M=D");

		//
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("A=M");
		asms.add("M=D");


		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		return asms;
	}

	private List<String> translatePopLocal(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// pop local " + index);

		asms.add("@LCL");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("D=D+A");
		asms.add("@R13");
		asms.add("M=D");

		//
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("A=M");
		asms.add("M=D");


		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		return asms;
	}

	private List<String> translatePopThis(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// pop this " + index);

		asms.add("@THIS");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("D=D+A");
		asms.add("@R13");
		asms.add("M=D");

		//
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("A=M");
		asms.add("M=D");


		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		return asms;
	}

	private List<String> translatePopThat(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// pop that " + index);

		asms.add("@THAT");
		asms.add("D=M");
		asms.add("@" + index);
		asms.add("D=D+A");
		asms.add("@R13");
		asms.add("M=D");

		//
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("A=M");
		asms.add("M=D");


		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		return asms;
	}

	private List<String> translatePopStatic(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// pop static " + index);

		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");

		asms.add("@" + vmName + ".static." + index);
		asms.add("M=D");

		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		return asms;
	}

	private List<String> translatePopPointer(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// pop pointer " + index);

		asms.add("@3");
		asms.add("D=A");
		asms.add("@" + index);
		asms.add("D=D+A");
		asms.add("@R13");
		asms.add("M=D");

		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("A=M");
		asms.add("M=D");


		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");


		return asms;
	}

	private List<String> translatePopTemp(int index) {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// pop temp " + index);

		asms.add("@5");
		asms.add("D=A");
		asms.add("@" + index);
		asms.add("D=D+A");
		asms.add("@R13");
		asms.add("M=D");

		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("D=M");
		asms.add("@R13");
		asms.add("A=M");
		asms.add("M=D");


		// Update stack pointer
		asms.add("@SP");
		asms.add("M=M-1");

		return asms;
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

		// Add comment to asm
		asms.add("// add");

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

		// Add comment to asm
		asms.add("// sub");

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

		// Sub
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

		// Add comment to asm
		asms.add("// neg");

		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=-M");

		return asms;
	}

	private List<String> translateCommandEQ() {
		String labelEQTrue = vmName + ".EQ.true." + eqCount;
		String labelEQEnd = vmName + ".EQ.end." + eqCount;
		eqCount++;

		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// eq");

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

		// Sub
		asms.add("@R13");
		asms.add("D=D-M");

		// Check to jump
		asms.add("@" + labelEQTrue);
		asms.add("D;JEQ");

		// No jump
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=0");
		asms.add("@" + labelEQEnd);
		asms.add("0;JMP");

		// Jump to here if true/equal
		asms.add("(" + labelEQTrue + ")");
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=-1");

		// End
		asms.add("(" + labelEQEnd + ")");

		return asms;
	}

	private List<String> translateCommandGT() {
		String labelGTTrue = vmName + ".GT.true." + gtCount;
		String labelGTEnd = vmName + ".GT.end." + gtCount;
		gtCount++;

		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// gt");

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

		// Sub
		asms.add("@R13");
		asms.add("D=D-M");

		// Check to jump
		asms.add("@" + labelGTTrue);
		asms.add("D;JGT");

		// No jump
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=0");
		asms.add("@" + labelGTEnd);
		asms.add("0;JMP");

		// Jump to here if true/greater than
		asms.add("(" + labelGTTrue + ")");
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=-1");

		// End
		asms.add("(" + labelGTEnd + ")");

		return asms;
	}

	private List<String> translateCommandLT() {
		String labelLTTrue = vmName + ".LT.true." + ltCount;
		String labelLTEnd = vmName + ".LT.end." + ltCount;
		ltCount++;

		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// lt");

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

		// Sub
		asms.add("@R13");
		asms.add("D=D-M");

		// Check to jump
		asms.add("@" + labelLTTrue);
		asms.add("D;JLT");

		// No jump
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=0");
		asms.add("@" + labelLTEnd);
		asms.add("0;JMP");

		// Jump to here if true/lesser than
		asms.add("(" + labelLTTrue + ")");
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=-1");

		// End
		asms.add("(" + labelLTEnd + ")");

		return asms;
	}

	private List<String> translateCommandAnd() {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// and");

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

		// And
		asms.add("@R13");
		asms.add("D=D&M");


		// Store the result
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=D");

		return asms;
	}

	private List<String> translateCommandOr() {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// or");

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

		// Or
		asms.add("@R13");
		asms.add("D=D|M");


		// Store the result
		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=D");

		return asms;
	}

	private List<String> translateCommandNot() {
		List<String> asms = new ArrayList<>();

		// Add comment to asm
		asms.add("// not");

		asms.add("@SP");
		asms.add("A=M-1");
		asms.add("M=!M");

		return asms;
	}
}
