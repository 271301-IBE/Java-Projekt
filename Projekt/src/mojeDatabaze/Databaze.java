package mojeDatabaze;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;


public class Databaze {
    private Map<Integer, Student> studenti; 
    private int posledniId;
    private static final String DB_URL = "jdbc:sqlite:studenti.db"; 

    public Databaze() {
        studenti = new HashMap<>();
        posledniId = 0;
        nactiZSQL(); 
    }

    
    private int generujNoveId() {
        posledniId++;
        return posledniId;
    }

  
    public boolean pridejStudenta(String jmeno, String prijmeni, int rokNarozeni, String obor) {
        if (jmeno == null || jmeno.trim().isEmpty() || prijmeni == null || prijmeni.trim().isEmpty()) {
             System.err.println("Jméno a příjmení nesmí být prázdné.");
             return false;
        }
        int noveId = generujNoveId();
        Student novyStudent;
        if ("Telekomunikace".equalsIgnoreCase(obor)) {
            novyStudent = new StudentTelekomunikaci(noveId, jmeno.trim(), prijmeni.trim(), rokNarozeni);
        } else if ("Kyberbezpečnost".equalsIgnoreCase(obor)) {
            novyStudent = new StudentKyberbezpecnosti(noveId, jmeno.trim(), prijmeni.trim(), rokNarozeni);
        } else {
            System.err.println("Neznámý obor: " + obor);
            posledniId--; 
            return false;
        }
        studenti.put(noveId, novyStudent);
        System.out.println("Student přidán: " + novyStudent);
        return true;
    }

 
    public Student najdiStudenta(int id) {
        return studenti.get(id);
    }

  
    public boolean pridejZnamku(int idStudenta, int znamka) {
        Student student = najdiStudenta(idStudenta);
        if (student != null) {
            if (student.pridejZnamku(znamka)) {
                System.out.println("Známka " + znamka + " přidána studentovi ID: " + idStudenta);
                return true;
            }
        } else {
            System.err.println("Student s ID: " + idStudenta + " nenalezen.");
        }
        return false;
    }

  
    public boolean odeberStudenta(int idStudenta) {
        if (studenti.containsKey(idStudenta)) {
            Student odebrany = studenti.remove(idStudenta);
            System.out.println("Student odstraněn: " + odebrany);
            return true;
        } else {
            System.err.println("Student s ID: " + idStudenta + " nenalezen pro odstranění.");
            return false;
        }
    }

 
    public void vypisInformaceStudenta(int idStudenta) {
        Student student = najdiStudenta(idStudenta);
        if (student != null) {
            System.out.println(student);
        } else {
            System.err.println("Student s ID: " + idStudenta + " nenalezen.");
        }
    }

 
    public void provedDovednostStudenta(int idStudenta) {
        Student student = najdiStudenta(idStudenta);
        if (student != null) {
            System.out.println("Výsledek dovednosti studenta " + student.getPrijmeni() + ":");
            System.out.println(student.provedDovednost());
        } else {
            System.err.println("Student s ID: " + idStudenta + " nenalezen.");
        }
    }

  
    public void vypisVsechnyStudentyPodleOboru() {
        System.out.println("\n--- Studenti Telekomunikací ---");
        studenti.values().stream()
                .filter(s -> s instanceof StudentTelekomunikaci)
                .sorted() 
                .forEach(System.out::println);

        System.out.println("\n--- Studenti Kyberbezpečnosti ---");
        studenti.values().stream()
                .filter(s -> s instanceof StudentKyberbezpecnosti)
                .sorted() 
                .forEach(System.out::println);
        System.out.println("--- Konec výpisu ---");
    }

    public void vypisPrumerOboru(String obor) {
        double soucetPrumeru = 0;
        int pocetStudentuVOboru = 0;
        Class<?> tridaOboru = null;

        if ("Telekomunikace".equalsIgnoreCase(obor)) {
             tridaOboru = StudentTelekomunikaci.class;
        } else if ("Kyberbezpečnost".equalsIgnoreCase(obor)) {
             tridaOboru = StudentKyberbezpecnosti.class;
        } else {
             System.err.println("Neznámý obor: " + obor);
             return;
        }

        for (Student s : studenti.values()) {
            if (tridaOboru.isInstance(s)) {
                double prumerStudenta = s.vypocitejStudijniPrumer();
                if (!s.getZnamky().isEmpty()) {
                    soucetPrumeru += prumerStudenta;
                    pocetStudentuVOboru++;
                }
            }
        }

        if (pocetStudentuVOboru > 0) {
            double prumerOboru = soucetPrumeru / pocetStudentuVOboru;
            System.out.printf("Celkový studijní průměr v oboru %s: %.2f%n", obor, prumerOboru);
        } else {
            System.out.println("V oboru " + obor + " nejsou žádní studenti se známkami pro výpočet průměru.");
        }
    }

    public void vypisPocetStudentuOboru(String obor) {
         long pocet = studenti.values().stream()
                .filter(s -> s.getObor().equalsIgnoreCase(obor))
                .count();
         System.out.println("Celkový počet studentů v oboru " + obor + ": " + pocet);
    }

    public boolean ulozStudentaDoSouboru(int idStudenta, String nazevSouboru) {
        Student student = najdiStudenta(idStudenta);
        if (student == null) {
            System.err.println("Student s ID: " + idStudenta + " nenalezen pro uložení.");
            return false;
        }

        try (FileOutputStream fos = new FileOutputStream(nazevSouboru);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(student);
            System.out.println("Student " + student.getPrijmeni() + " (ID: " + idStudenta + ") uložen do souboru: " + nazevSouboru);
            return true;

        } catch (IOException e) {
            System.err.println("Chyba při ukládání studenta do souboru: " + e.getMessage());
            return false;
        }
    }
    public Student nactiStudentaZeSouboru(String nazevSouboru) {
        try (FileInputStream fis = new FileInputStream(nazevSouboru);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Student nactenyStudent = (Student) ois.readObject();
            System.out.println("Student načten ze souboru " + nazevSouboru + ": " + nactenyStudent);
            return nactenyStudent;

        } catch (FileNotFoundException e) {
            System.err.println("Chyba: Soubor '" + nazevSouboru + "' nenalezen.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Chyba při načítání studenta ze souboru: " + e.getMessage());
        }
        return null;
    }

    private Connection getSpojeni() throws SQLException {

        return DriverManager.getConnection(DB_URL);
    }

    private void vytvorTabulkuPokudNeexistuje() {
        String sql = """
                     CREATE TABLE IF NOT EXISTS studenti (
                         id INTEGER PRIMARY KEY,
                         jmeno TEXT NOT NULL,
                         prijmeni TEXT NOT NULL,
                         rok_narozeni INTEGER,
                         obor TEXT NOT NULL,
                         znamky TEXT
                     );
                     """;
        try (Connection conn = getSpojeni();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Chyba při vytváření tabulky: " + e.getMessage());
        }
    }

    public void ulozDoSQL() {
        vytvorTabulkuPokudNeexistuje();

        String sqlDelete = "DELETE FROM studenti;";
        String sqlInsert = "INSERT INTO studenti(id, jmeno, prijmeni, rok_narozeni, obor, znamky) VALUES(?,?,?,?,?,?)";

        try (Connection conn = getSpojeni()) {
            conn.setAutoCommit(false);

            try (Statement stmtDelete = conn.createStatement()) {
                stmtDelete.executeUpdate(sqlDelete);
                System.out.println("Stávající data v SQL databázi vymazána.");
            }

            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                System.out.println("Ukládání aktuálních dat do SQL databáze...");
                for (Student s : studenti.values()) {
                    pstmtInsert.setInt(1, s.getId());
                    pstmtInsert.setString(2, s.getJmeno());
                    pstmtInsert.setString(3, s.getPrijmeni());
                    pstmtInsert.setInt(4, s.getRokNarozeni());
                    pstmtInsert.setString(5, s.getObor());
                    String znamkyStr = s.getZnamky().stream()
                                        .map(String::valueOf)
                                        .collect(Collectors.joining(","));
                    pstmtInsert.setString(6, znamkyStr);
                    pstmtInsert.addBatch(); 
                }
                pstmtInsert.executeBatch(); 
                conn.commit();
                System.out.println("Data úspěšně uložena do SQL databáze.");
            } catch (SQLException eInsert) {
                 System.err.println("Chyba při vkládání dat do SQL: " + eInsert.getMessage());
                 conn.rollback();
            } finally {
                 conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.err.println("Chyba při práci s SQL databází (ukládání): " + e.getMessage());
        }
    }


   
    public void nactiZSQL() {
        vytvorTabulkuPokudNeexistuje();
        String sql = "SELECT id, jmeno, prijmeni, rok_narozeni, obor, znamky FROM studenti";
        studenti.clear(); 
        int maxId = 0; 

        try (Connection conn = getSpojeni();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Načítání dat z SQL databáze...");
            while (rs.next()) {
                int id = rs.getInt("id");
                String jmeno = rs.getString("jmeno");
                String prijmeni = rs.getString("prijmeni");
                int rokNarozeni = rs.getInt("rok_narozeni");
                String obor = rs.getString("obor");
                String znamkyStr = rs.getString("znamky");

                Student student;
                if ("Telekomunikace".equalsIgnoreCase(obor)) {
                    student = new StudentTelekomunikaci(id, jmeno, prijmeni, rokNarozeni);
                } else if ("Kyberbezpečnost".equalsIgnoreCase(obor)) {
                    student = new StudentKyberbezpecnosti(id, jmeno, prijmeni, rokNarozeni);
                } else {
                    System.err.println("Neznámý obor '" + obor + "' v databázi pro studenta ID: " + id + ". Přeskakuji.");
                    continue; 
                }

        
                List<Integer> znamkyList = new ArrayList<>();
                if (znamkyStr != null && !znamkyStr.isEmpty()) {
                    try {
                        String[] znamkyPole = znamkyStr.split(",");
                        for (String z : znamkyPole) {
                            if (!z.trim().isEmpty()) {
                                znamkyList.add(Integer.parseInt(z.trim()));
                            }
                        }
                    } catch (NumberFormatException eParse) {
                         System.err.println("Chyba při parsování známek pro studenta ID: " + id + " (" + znamkyStr + "). Známky nebudou načteny.");
                         znamkyList.clear();
                    }
                }
                student.setZnamky(znamkyList);
                studenti.put(id, student);


                if (id > maxId) {
                    maxId = id;
                }
            }
            posledniId = maxId; 
            System.out.println("Data úspěšně načtena z SQL databáze. Poslední ID: " + posledniId);

        } catch (SQLException e) {
            System.err.println("Chyba při načítání dat z SQL databáze: " + e.getMessage());
            studenti.clear();
            posledniId = 0;
        }
    }
}