package mojeDatabaze;

import java.util.HashMap;
import java.util.Map;

public class Morseovka {

    private static final Map<Character, String> morseovkaMapa = new HashMap<>();


    static {
        morseovkaMapa.put('A', ".-"); morseovkaMapa.put('B', "-..."); morseovkaMapa.put('C', "-.-.");
        morseovkaMapa.put('D', "-.."); morseovkaMapa.put('E', "."); morseovkaMapa.put('F', "..-.");
        morseovkaMapa.put('G', "--."); morseovkaMapa.put('H', "...."); morseovkaMapa.put('I', "..");
        morseovkaMapa.put('J', ".---"); morseovkaMapa.put('K', "-.-"); morseovkaMapa.put('L', ".-..");
        morseovkaMapa.put('M', "--"); morseovkaMapa.put('N', "-."); morseovkaMapa.put('O', "---");
        morseovkaMapa.put('P', ".--."); morseovkaMapa.put('Q', "--.-"); morseovkaMapa.put('R', ".-.");
        morseovkaMapa.put('S', "..."); morseovkaMapa.put('T', "-"); morseovkaMapa.put('U', "..-");
        morseovkaMapa.put('V', "...-"); morseovkaMapa.put('W', ".--"); morseovkaMapa.put('X', "-..-");
        morseovkaMapa.put('Y', "-.--"); morseovkaMapa.put('Z', "--..");
        morseovkaMapa.put('1', ".----"); morseovkaMapa.put('2', "..---"); morseovkaMapa.put('3', "...--");
        morseovkaMapa.put('4', "....-"); morseovkaMapa.put('5', "....."); morseovkaMapa.put('6', "-....");
        morseovkaMapa.put('7', "--..."); morseovkaMapa.put('8', "---.."); morseovkaMapa.put('9', "----.");
        morseovkaMapa.put('0', "-----"); morseovkaMapa.put(' ', "/"); 
       
    }

    public static String prelozNaMorseovku(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder morseovkaVysledek = new StringBuilder();
        String normalizovanyText = text.toUpperCase(); 

        for (char znak : normalizovanyText.toCharArray()) {
            String morseuvZnak = morseovkaMapa.get(znak);
            if (morseuvZnak != null) {
                morseovkaVysledek.append(morseuvZnak).append(" "); 
            } else {
               
                 morseovkaVysledek.append("? ");             }
        }
        return morseovkaVysledek.toString().trim();
    }
}