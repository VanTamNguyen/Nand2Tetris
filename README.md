# Nand2Tetris
Nand2Tetris: Build a computer system from the ground up, from nand to tetris. Hardware and software.


### Chapter 1: Boolean logic
### Chapter 2: Boolean arithmetic
### Chapter 3: Sequential logic
### Chapter 4: Machine language
### Chapter 5: Computer architecture
### Chapter 6: Assembler
### Chapter 7: Virtual machine I - Stack arithmetic 
### Chapter 8: Virtual machine II - Program control
### Chapter 9: High-level language
### Chapter 10: Compiler I - Syntax analysis

### Chapter 11: Compiler II - Code generation
The compilation of high-level programming language into a low-level one focuses on 2 main issues: ***data translation*** and ***command translation***
#### 11.1 Data translation
* **Variables** For variables we need to care about some of its properties:
    * *type* integer, char, boolean, array, object
    * *kind* field, static, local, argument
    * *scope* class level, subroutine level
* **Symbol table**: A data structure to keep track all *identifiers*. Whenever a new *identifier* is encountered for the first time the compiler adds its description to *symbol table*. Whennever an *identifies* is encountered elsewhere in the source code the compiler looks it up in symbol table and get all information needed from symbol table.
![Symbol Table](images/symbol-table.png)

#### 11.2 Command translation

### Chapter 12: Operating system
