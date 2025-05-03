# Java-Projekt
projekt do javy

## Požadavky

*   **Java Development Kit (JDK):** Verze 17 nebo novější (kvůli použití textových bloků a moderních API).
*   **Externí knihovny:**
    *   SQLite JDBC Driver https://github.com/xerial/sqlite-jdbc/releases
 
    *   Struktura Projektu
mojeDatabaze/ - Balíček obsahující veškerý kód.
Databaze.java - Hlavní třída spravující kolekci studentů a interakci s databází (SQLite)
HlavniProgram.java - Obsahuje main metodu, uživatelské menu a interakci s uživatelem.
Student.java - Abstraktní třída reprezentující studenta (společné vlastnosti a metody).
StudentKyberbezpecnosti.java - Konkrétní třída pro studenta oboru Kyberbezpečnost.
Morseovka.java - Pomocná třída pro překlad textu do Morseovy abecedy.
