# Sentence formulas

A program that checks whether a sentence is correct in logic.

## Usage

Compile the application using this command in the console.

    javac L6Z4.java

Run the application using this command in the console.

    java L6Z4 "<formula>"

Replace `<formula>` with the logical formula you want to check.

## Building sentence formulas
The logical formulas are built using the following rules:
- Formuła atomowa to zmienna (a - z) lub stała logiczna (V - prawda, F - fałsz)
- Jeżeli F jest formułą, to (N F) jest formułą (negacja)
- Jeżeli F1 i F2 są formułami, to (F1 K F2), (F1 A F2), (F1 C F2), (F1 E F2) są formułami (koniunkcja, alternatywa, implikacja, równoważność)
- Każda formuła złożona musi być otoczona nawias
- Negacja zawsze ma jeden argument
- Operatory dwuargumentowe zawsze mają dwa argumenty

## Alphabet

```
private static List<String> alfabet = Arrays.asList(
	"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",           // Zmienne (a - z)
	"k", "l", "m", "n", "o", "p", "q", "r", "s", "t",           // Zmienne (a - z)
	"u", "v", "w", "x", "y", "z",                               // Zmienne (a - z)
	"N",                                                        // Negacja
	"K",                                                        // Koniunkcja
	"A",                                                        // Alternatywa
	"C",                                                        // Implikacja
	"E",                                                        // Równoważność
	"V",                                                        // Prawda
	"F",                                                        // Fałsz
	"(",                                                        // Lewy nawias
	")"                                                         // Prawy nawias
	);
```

## Examples

    java L6Z4 "(a K b)"         // Correct
    java L6Z4 "(N (a A b))"     // Correct
    java L6Z4 "(a C (b E c))"   // Correct
    java L6Z4 "(N (N a))"       // Correct

    java L6Z4 "a K b E c"       // Incorrect
    java L6Z4 "a K (b A)"       // Incorrect
    java L6Z4 "(a C C (b A c)"  // Incorrect

