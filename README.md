# Nand2Tetris
Nand2Tetris: Build a computer system from the ground up, from nand to tetris. Hardware and software.


### [Chapter 1: Boolean logic](nand2tetris/projects/01)
* Truth table representation
* Canonical representation
* Logic gates
    * Nand(a, b)
    * Not(in)
    * And(a, b)
    * Or(a, b)
    * Xor(a, b)
    * Mux(a, b, sel) multiplexor choose one from many
    * DMux(in, sel)

### [Chapter 2: Boolean arithmetic](nand2tetris/projects/02)
### [Chapter 3: Sequential logic](nand2tetris/projects/03)
### [Chapter 4: Machine language](nand2tetris/projects/04)
### [Chapter 5: Computer architecture](nand2tetris/projects/05)
### [Chapter 6: Assembler](nand2tetris/projects/06)
### [Chapter 7: Virtual machine I - Stack arithmetic](nand2tetris/projects/07) 
### [Chapter 8: Virtual machine II - Program control](nand2tetris/projects/08)
### [Chapter 9: High-level language](nand2tetris/projects/09)
### [Chapter 10: Compiler I - Syntax analysis](nand2tetris/projects/10)

### [Chapter 11: Compiler II - Code generation](nand2tetris/projects/11)
The compilation of high-level programming language into a low-level one focuses on 2 main issues: ***data translation*** and ***command translation***
#### 11.1 Data translation
* **Variables** For variables we need to care about some of its properties
    * *type*: integer, char, boolean, array, object
    * *kind*: field, static, local, argument
    * *scope*: class level, subroutine level
* **Symbol table**: A data structure to keep track all *identifiers*. Whenever a new *identifier* is encountered for the first time the compiler adds its description to *symbol table*. Whennever an *identifies* is encountered elsewhere in the source code the compiler looks it up in symbol table and get all information needed from symbol table.
![Symbol Table](images/symbol-table.png)

* **Handling variable**

* **Handling object**
![](images/handling-object.png)

* **Handling array**
![](images/handling-array.png)

#### 11.2 Command translation
* **Handling expression**

* **Handling flow of control**

### [Chapter 12: Operating system](nand2tetris/projects/12)
