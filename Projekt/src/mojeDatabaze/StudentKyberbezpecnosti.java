package mojeDatabaze;

import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class StudentKyberbezpecnosti extends Student {
    private static final long serialVersionUID = 1L;

    public StudentKyberbezpecnosti(int id, String jmeno, String prijmeni, int rokNarozeni) {
        super(id, jmeno, prijmeni, rokNarozeni);
    }

    @Override
    public String getObor() {
        return "Kyberbezpečnost";
    }

    @Override
    public String provedDovednost() {
        String text = getJmeno() + " " + getPrijmeni();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            String hashHex = hexString.toString(); 


            return "SHA-256 Hash pro '" + text + "': " + hashHex;

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Chyba: Hashovací algoritmus SHA-256 nenalezen.");
            return "Jednoduchý Hash pro '" + text + "': " + text.hashCode();
        }
    }
    
}