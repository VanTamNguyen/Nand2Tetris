// 1. R13 <--- FRAME = LCL
@LCL
D=M
@R13
M=D

// 2. R14 <--- RET = *(FRAME - 5)
@R13
D=M
@5
A=D-A
D=M
@R14
M=D

// 3. *ARG = pop()
@SP
A=M-1
D=M
@ARG
A=M
M=D

// 4. SP = ARG + 1
@ARG
D=M+1
@SP
M=D

// 5. THAT = *(FRAME - 1)
@R13
A=M-1
D=M
@THAT
M=D

// 6. THIS = *(FRAME - 2)
@R13
D=M
@2
A=D-A
D=M
@THIS
M=D

// 7. ARG = *(FRAME - 3)
@R13
D=M
@3
A=D-A
D=M
@ARG
M=D

// 8. LCL = *(FRAME - 4)
@R13
D=M
@4
A=D-A
D=M
@LCL
M=D

// 9. goto RET
@R14
A=M
0;JMP