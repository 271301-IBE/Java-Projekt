package mojeDatabaze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Student implements Serializable, Comparable<Student> {

    private static final long serialVersionUID = 1L;

    private int id;
    private String jmeno;
    private String prijmeni;
    private int rokNarozeni;
    private List<Integer> znamky; 


    public Student(int id, String jmeno, String prijmeni, int rokNarozeni) {
        this.id = id;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
        this.znamky = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public String getJmeno() {
        return jmeno;
    }

    public String getPrijmeni() {
        return prijmeni;
    }

    public int getRokNarozeni() {
        return rokNarozeni;
    }

    public List<Integer> getZnamky() {
  
        return new ArrayList<>(znamky);
    }

    public boolean pridejZnamku(int znamka) {
        if (znamka >= 1 && znamka <= 5) {
            this.znamky.add(znamka);
            return true;
        }
        System.err.println("Neplatná známka: " + znamka + ". Známka musí být mezi 1 a 5.");
        return false;
    }

    public double vypocitejStudijniPrumer() {
        if (znamky.isEmpty()) {
            return 0.0; 
        }
        double soucet = 0;
        for (int znamka : znamky) {
            soucet += znamka;
        }
        return soucet / znamky.size();
    }

    public abstract String getObor();

    public abstract String provedDovednost();

    @Override
    public int compareTo(Student o) {
        int porovnaniPrijmeni = this.prijmeni.compareToIgnoreCase(o.prijmeni);
        if (porovnaniPrijmeni == 0) {
            return this.jmeno.compareToIgnoreCase(o.jmeno);
        }
        return porovnaniPrijmeni;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Jméno: %s, Příjmení: %s, Rok narození: %d, Průměr: %.2f, Obor: %s",
                             id, jmeno, prijmeni, rokNarozeni, vypocitejStudijniPrumer(), getObor());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    protected void setZnamky(List<Integer> nacteneZnamky) {
        if (nacteneZnamky != null) {
            this.znamky = new ArrayList<>(nacteneZnamky);
        } else {
            this.znamky = new ArrayList<>();
        }
    }
}