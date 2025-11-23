import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SentenceFormulas {

	private String sentence;            // Formuła zdania do sprawdzenia
	private String[] splitSentence;     // Formuła podzielona na pojedyńcze znaki
	private List<String> alphabet;      // Alfabet dostępnych znaków
	private List<String> elements;      // Lista elementów do użycia (zmiennych)
	private enum ERRORS {               // Lista błędów
		MISSING_SENTENCE,   // Nie wprowadzono zdania
		NO_ALPHABET_CHAR,   // Symbol nie należący do alfabetu
		BRACKET_BILANS,     // Różna liczba nawiasów otwierających i zamykających
		LEFT_BRACKET,       // Brak lewego nawiasu
		RIGHT_BRACKET,      // Brak prawego nawiasu
	}

	public SentenceFormulas(String sentence) {
		// Pozbywamy się od razu wszystkich spacji
		this.sentence = sentence;
		this.splitSentence = sentence.replaceAll(" ", "").split("");

		// Nie wprowadzono zdania
		if (sentence.isEmpty()) {
			error(ERRORS.MISSING_SENTENCE);
			return;
		}

		// Definiowanie alfabetu i jego elementów które będzie można użyć w formule
		alphabet = new ArrayList<>();
		elements = new ArrayList<>();
		// elementy a - z
		for (int i = 97; i <= 122; i++) {
			alphabet.add("" + (char) i);
			elements.add("" + (char) i);
		}
		// Negacja
		alphabet.add("N");
		// Koniunkcja
		alphabet.add("K");
		// Alternatywa
		alphabet.add("A");
		// Implikacja
		alphabet.add("C");
		// Równoważność
		alphabet.add("E");
		// Nawiasy
		alphabet.add("(");
		alphabet.add(")");
		// Prawda
		alphabet.add("V");
		// Fałsz
		alphabet.add("F");

		if (checkIfSentence())
			System.out.println("Poprawna formuła");
	}

	// Sprawdzanie czy formuła jest prawidłowa
	private boolean checkIfSentence() {
		// Sprawdzanie czy zostały użyte elementy tylko które należą do alfabetu
		if (!checkIfCorrectAlphabetUse())
			return false;

		if (!checkFormula(this.splitSentence, 0))
			return false;

		return true;
	}

	private boolean checkIfCorrectAlphabetUse() {
		for (int i = 0; i < this.splitSentence.length; i++) {
			// Sprawdzamy czy element należy do alfabetu
			if (!alphabet.contains(splitSentence[i])) {
				error(ERRORS.NO_ALPHABET_CHAR, i);
				return false;
			}
		}
		return true;
	}

	private boolean checkFormula(String[] formula, int from) {
		int leftBracket = -1;   // Pozycja lewego nawiasu
		int rightBracket = -1;  // Pozycja prawego nawiasu

		System.out.println(String.join("", formula));

		// Szukamy lewego nawiasu
		for (int i = from; i < formula.length; i++) {
			if (formula[i].equals("(")) {
				leftBracket = i;
				break;
			}
		}
		// Szukamy prawego nawiasu
		for (int i = formula.length - 1; i >= from; i--) {
			if (formula[i].equals(")")) {
				rightBracket = i;
				break;
			}
		}

		System.out.println("Left bracket: " + leftBracket);
		System.out.println("Right bracket: " + rightBracket);

		// Przy okazji spradzamy czy każdy otwarty nawias ma swoje zakmnięcie
		if (leftBracket == -1 && rightBracket != -1) { // Brak lewego
			error(ERRORS.LEFT_BRACKET, 0); // TODO poprawne spacje
			return false;
		}
		if (leftBracket != -1 && rightBracket == -1) {
			error(ERRORS.RIGHT_BRACKET, 0); // TODO poprawne spacje
			return false;
		}

		// Przypadek podstawowy -> brak nawiasów -> najbłęgszy nawias
		if (leftBracket == -1 && rightBracket == -1) {
			// Sprawdzamy teraz czy "środek" nawiasu jest poprawny


			return true;
		}

		// Schodzimy w dół -> sprawdzamy zawias zagłębiony
		checkFormula(
				Arrays.copyOfRange(formula, leftBracket + 1, rightBracket),
				leftBracket + 1);

		return false;
	}

//	private boolean checkIfCorrectBracketUse() {
//		String[] splitSentence = this.sentence.split("");
//		int bracketBilans = 0;
//
//		for (String sentenceChar : splitSentence) {
//			if (sentenceChar.equals("(")) {
//				bracketBilans++;
//			} else if (sentenceChar.equals(")")) {
//				bracketBilans--;
//			}
//		}
//
//		// Bilans wynosi 0 czyli tyle samo nawiasów otwierających i zamykających
//		if (bracketBilans == 0) return true;
//
//		error(ERRORS.BRACKET_BILANS);
//		return false;
//	}

	private void error(ERRORS error, int ... spaces) {
		// Wypisujemy nieprawidłową formułę
		System.out.println(this.sentence);

		// Dopisywanie spacji w celu lokalizacji błędu
		String spacesString = " ".repeat(spaces[0]);
		System.out.print(spacesString);

		switch (error) {
			case MISSING_SENTENCE:
				System.out.println("Wywołanie: L6Z4 \"<formuła>\"");
				break;
			case NO_ALPHABET_CHAR:
				System.out.println("^ Wprowadzono znak spoza alfabetu");
				break;
			case BRACKET_BILANS:
				System.out.println("Nieprawidłowy bilans nawiasów");
				break;
			case LEFT_BRACKET:
				System.out.println("^ Oczekiwano \"(\"");
				break;
			case RIGHT_BRACKET:
				System.out.println("^ Oczekiwano \")\"");

		}
	}

}
