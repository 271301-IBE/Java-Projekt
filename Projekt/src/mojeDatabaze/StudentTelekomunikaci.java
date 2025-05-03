package mojeDatabaze;

public class StudentTelekomunikaci extends Student {

    private static final long serialVersionUID = 1L;

    public StudentTelekomunikaci(int id, String jmeno, String prijmeni, int rokNarozeni) {
        super(id, jmeno, prijmeni, rokNarozeni);
    }

    @Override
    public String getObor() {
        return "Telekomunikace";
    }

    @Override
    public String provedDovednost() {
        String text = getJmeno() + " " + getPrijmeni();
        return "Morseovka pro '" + text + "': " + Morseovka.prelozNaMorseovku(text);
    }
}