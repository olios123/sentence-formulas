import java.util.Arrays;
import java.util.List;

public class L6Z4 {
	// -------------------------------------------------------------------------------------------
	//
	// Autor: Oliwier Kądziołka
	// Logika dla informatyków: Lista 6, Zadanie 4
	// Sprawdzanie poprawności formuł w logice zdań
	//
	// -------------------------------------------------------------------------------------------
	// Zasady poprawnych formuł w logice zdań:
	// 1. Formuła atomowa to zmienna (a - z) lub stała logiczna (V - prawda, F - fałsz)
	// 2. Jeżeli F jest formułą, to (N F) jest formułą (negacja)
	// 3. Jeżeli F1 i F2 są formułami, to (F1 K F2), (F1 A F2), (F1 C F2), (F1 E F2) są formułami (koniunkcja, alternatywa, implikacja, równoważność)
	//
	// Dodatkowe zasady:
	// - Każda formuła złożona musi być otoczona nawiasem
	// - Negacja zawsze ma jeden argument
	// - Operatory dwuargumentowe zawsze mają dwa argumenty
	//
	// Przykłady poprawnych formuł:
	//   a) (a K b)
	//   b) (N (a A V))
	//   c) ((a C b) E (N F))
	//
	// Przykłady niepoprawnych formuł:
	//   a) a K b          (brak nawiasów)
	//   b) (N a K b)      (brak nawiasów wokół a K b)
	//   c) (a A (b C))    (brak drugiego argumentu dla C)
	// -------------------------------------------------------------------------------------------
	private enum Symbol {                                               // Symbole gramatyki w logice
		ZMIENNA, PRAWDA, FALSZ,                                         // Zdania atomowe
		NEGACJA, KONIUNKCJA, ALTERNATYWA, IMPLIKACJA, ROWNOWAZNOSC,     // Operatory
		L_NAWIAS, P_NAWIAS, KONIEC                                      // Inne
	};
	// -------------------------------------------------------------------------------------------
	private static List<String> alfabet = Arrays.asList(                // Elementy alfabetu logiki
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
	// -------------------------------------------------------------------------------------------
	private static char ostatniZnak;                                // Ostatni przeczytany znak
	private static int licznikZnakow;                               // Licznik znakow
	private static String wyrazenie;                                // Łancuch znakow zawierajacy interpretowane wyrazenie
	private static Symbol ostatniSymbol;                            // Ostatni przeczytany symbol
	// -------------------------------------------------------------------------------------------
	private enum Bledy {                                // Kody błędów
		NIEOCZEKIWANY_KONIEC_FORMULY,                   // ^ nieoczekiwany koniec formuly
		OCZEKIWANA_FORMULA_ATOMOWA_LUB_L_NAWIAS,        // ^ oczekiwana formuła atomowa lub "("
		OCZEKIWANY_P_NAWIAS,                            // ^ oczekiwany ")"
		OCZEKIWANY_SPOJNIK_DWUARGUMENTOWY,              // ^ oczekiwany spójnik dwuargumentowy
		NIEOCZEKIWANE_ZNAKI_NA_KONCU,                   // ^ nieoczekiwane znaki na końcu
		ELEMENT_SPOZA_ALFABATU                          // ^ element spoza alfabetu
	}
	// -------------------------------------------------------------------------------------------
	private static void wypiszBlad(Bledy blad) {
		// Przesuwamy napis błedu w prawo w zależności od położenia błedu
		System.out.print(wyrazenie + "\n");
		System.out.print(" ".repeat(licznikZnakow));

		// Napisy odpowiadające błędom
		switch (blad) {
			case NIEOCZEKIWANY_KONIEC_FORMULY -> System.out.print("^ nieoczekiwany koniec formuly");
			case OCZEKIWANA_FORMULA_ATOMOWA_LUB_L_NAWIAS -> System.out.print("^ oczekiwana formuła atomowa lub \"(\"");
			case OCZEKIWANY_P_NAWIAS -> System.out.print("^ oczekiwany \")\"");
			case OCZEKIWANY_SPOJNIK_DWUARGUMENTOWY -> System.out.print("^ oczekiwany spójnik dwuargumentowy");
			case NIEOCZEKIWANE_ZNAKI_NA_KONCU -> System.out.print("^ nieoczekiwane znaki na końcu");
			case ELEMENT_SPOZA_ALFABATU -> System.out.print("^ element spoza alfabatu");
		}

		System.exit(0); // Koniec programu zakończonego błędem
	}
	// -------------------------------------------------------------------------------------------
	// Sprawdza czy znak znależy do alfabetu
	// Można było to zaimplementować przy pobieraniu znaku, ale jezeli w formule znajdują się znaki spoza alfabetu
	// jego dalsza analiza mija się z celem
	private static boolean czyZnakNalezyDoAlfabetu(char znak) {
		// Pomijamy dla spacji gdyż nic nie zmienia
		if (znak == ' ') return true;
		// Pomijamy także cudzysłowia
		if (znak == '\"') return true;

		// Znak znależy do alfabetu
		if (alfabet.contains(String.valueOf(znak))) return true;
		return false; // Nie należy
	}
	// -------------------------------------------------------------------------------------------
	// Pobiera kolejny znak z wyrażenia, zwiększa aktualną pozycję znaku
	private static void pobierzZnak() {
		if (licznikZnakow < wyrazenie.length()) {
			ostatniZnak = wyrazenie.charAt(licznikZnakow);
			licznikZnakow++;
		} else {
			ostatniZnak = 0;
		}
	}
	// -------------------------------------------------------------------------------------------
	// Zamieniamy znak na symbol logiki
	private static void pobierzSymbol() {
		// Pomijamy wszystkie białe znaki
		while (ostatniZnak != 0 && Character.isWhitespace(ostatniZnak)) pobierzZnak();

		// Rozponajemy zmienne (a - z)
		if (ostatniZnak >= 'a' && ostatniZnak <= 'z') {
			ostatniSymbol = Symbol.ZMIENNA;
			pobierzZnak();
		} else { // Rozpoznajemy resztę symboli które nie są zmiennymi
			switch (ostatniZnak) {
				case 'N' -> ostatniSymbol = Symbol.NEGACJA;
				case 'A' -> ostatniSymbol = Symbol.ALTERNATYWA;
				case 'K' -> ostatniSymbol = Symbol.KONIUNKCJA;
				case 'C' -> ostatniSymbol = Symbol.IMPLIKACJA;
				case 'E' -> ostatniSymbol = Symbol.ROWNOWAZNOSC;
				case 'V' -> ostatniSymbol = Symbol.PRAWDA;
				case 'F' -> ostatniSymbol = Symbol.FALSZ;
				case '(' -> ostatniSymbol = Symbol.L_NAWIAS;
				case ')' -> ostatniSymbol = Symbol.P_NAWIAS;
				case 0   -> ostatniSymbol = Symbol.KONIEC;
			}
			pobierzZnak();
		}
	}
	// -------------------------------------------------------------------------------------------
	// Rekurencyjna analiza formuły
	private static void formula() {
		switch (ostatniSymbol) {
			// Przypadki atomowe (a - z / V / F)
			case ZMIENNA:
			case PRAWDA:
			case FALSZ:
				pobierzSymbol();
				break;

			// Przypadek formuły złożonej rozpoczynającej się od '('
			case L_NAWIAS:
				pobierzSymbol(); // Usuwamy '('

				// Sprawdzamy czy to negacja: (N formuła)
				if (ostatniSymbol == Symbol.NEGACJA) {
					pobierzSymbol(); // Usuwamy 'N'
					formula();       // Analizujemy formułę
					if (ostatniSymbol == Symbol.P_NAWIAS) pobierzSymbol();
					else wypiszBlad(Bledy.OCZEKIWANY_P_NAWIAS); // Brak ')'
				}

				// W przeciwnym razie to operacja binarna: (formuła opreator formuła)
				else {
					formula(); // Lewy operand formuły

					// Oczekujemy jakiegoś operatora
					if (
							ostatniSymbol == Symbol.KONIUNKCJA ||
							ostatniSymbol == Symbol.ALTERNATYWA ||
							ostatniSymbol == Symbol.IMPLIKACJA ||
							ostatniSymbol == Symbol.ROWNOWAZNOSC) {
						pobierzSymbol(); // Usuwamy operator
					}
					else {
						// Błąd - brak operatora
						wypiszBlad(Bledy.OCZEKIWANY_SPOJNIK_DWUARGUMENTOWY);
					}

					formula(); // Prawy operand formuły

					if (ostatniSymbol == Symbol.P_NAWIAS) pobierzSymbol();
					else wypiszBlad(Bledy.OCZEKIWANY_P_NAWIAS); // Brak ')'
				}
				break;
			default:
				// Jeżeli nie atamowa i nie '(', to błąd
				wypiszBlad(Bledy.OCZEKIWANA_FORMULA_ATOMOWA_LUB_L_NAWIAS);
		}
	}
	// -------------------------------------------------------------------------------------------
	// Główna funckja programu
	public static void main(String[] args) {
		if (args.length == 1) {
			// Pobieramy wyrażenie do analizy
			wyrazenie = args[0];

			// Brak argumentów - wypisać działanie programu
			if (wyrazenie.isEmpty()) {
				System.out.println("Wywolanie: L6Z4 \"<formula>\"");
				return;
			}

			// Inicjalizacja zmiennych
			licznikZnakow = 0;
			ostatniZnak = ' ';

			// Sprawdzamy czy wszystkie znaki należą do alfabetu
			for (int i = 0; i < wyrazenie.split("").length - 1; i++) {
				licznikZnakow = i;
				if (!czyZnakNalezyDoAlfabetu(wyrazenie.charAt(i))) {
					wypiszBlad(Bledy.ELEMENT_SPOZA_ALFABATU);
					return;
				}
			}

			// Resetujemy informacje o ilości znaków i ostatnim znaku po sprawdzeniu
			licznikZnakow = 0;
			ostatniZnak = ' ';

			// Rozpoczynamy analizę
			pobierzSymbol();

			// Jeżeli symbolem będzie koniec - wypisz błąd
			if (ostatniSymbol == Symbol.KONIEC)
				wypiszBlad(Bledy.NIEOCZEKIWANY_KONIEC_FORMULY);
			else {
				formula(); // Rozpocznij analize od glownego symbolu F

				if (ostatniSymbol != Symbol.KONIEC)
					// Jeżeli po zakończeniu analizy zdania zosatły jakieś znaki wypisz błąd
					wypiszBlad(Bledy.NIEOCZEKIWANE_ZNAKI_NA_KONCU);
				else
					// Poprawna formuła
					System.out.println("Poprwana formula");
			}
		} else {
			// Brak argumentów - wypisać działanie programu
			System.out.println("Wywolanie: L6Z4 \"<formula>\"");
		}
	}
}