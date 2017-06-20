// 1. push return address
@returnAddress
D=A
@SP
A=M
M=D
@SP
M=M+1

// 2. push LCL
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1

// 3. push ARG
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1

// 4. push THIS
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1

// 5. push THAT
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1

// 6. calculate ARG for called function
@5
D=A
@nArgs
D=D+A
@SP
D=M-D
@ARG
M=D

// 7. calculate LCL for called function
@SP
D=M
@LCL
M=D

// 8. go to function
@function
0;JMP

// 9. mark return address
(returnAddress)