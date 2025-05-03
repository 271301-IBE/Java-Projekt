package mojeDatabaze;

import java.util.InputMismatchException;
import java.util.Scanner;

public class HlavniProgram {

    private static Databaze databaze = new Databaze();
    private static Scanner scanner = new Scanner(System.in); // Scanner pro vstup

    public static void main(String[] args) {
        int volba;
        do {
            vypisMenu();
            volba = nactiVolbu();
            zpracujVolbu(volba);
        } while (volba != 0);

      
        System.out.println("Ukládání databáze před ukončením...");
        databaze.ulozDoSQL();
        System.out.println("Program ukončen.");
        scanner.close(); 
    }


    private static void vypisMenu() {
        System.out.println("\n--- Menu Databáze Studentů ---");
        System.out.println("1. Přidat nového studenta");
        System.out.println("2. Přidat známku studentovi");
        System.out.println("3. Odstranit studenta");
        System.out.println("4. Vyhledat studenta podle ID");
        System.out.println("5. Provést dovednost studenta");
        System.out.println("6. Vypsat všechny studenty (tříděně podle oborů)");
        System.out.println("7. Vypsat studijní průměr oboru");
        System.out.println("8. Vypsat počet studentů v oboru");
        System.out.println("9. Uložit studenta do souboru");
        System.out.println("10. Načíst studenta ze souboru (POZOR: nepřidá do DB)");
        System.out.println("0. Ukončit program a uložit data");
        System.out.print("Zadejte vaši volbu: ");
    }


    private static int nactiVolbu() {
        int volba = -1;
        try {
            volba = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Neplatný vstup. Zadejte prosím číslo.");
 
        } finally {
             scanner.nextLine(); 
        }
        return volba;
    }

    private static void zpracujVolbu(int volba) {
        switch (volba) {
            case 1:
                pridejStudentaUI();
                break;
            case 2:
                pridejZnamkuUI();
                break;
            case 3:
                odeberStudentaUI();
                break;
            case 4:
                vyhledejStudentaUI();
                break;
            case 5:
                provedDovednostUI();
                break;
            case 6:
                databaze.vypisVsechnyStudentyPodleOboru();
                break;
            case 7:
                vypisPrumerOboruUI();
                break;
            case 8:
                vypisPocetStudentuUI();
                break;
            case 9:
                 ulozStudentaDoSouboruUI();
                 break;
            case 10:
                 nactiStudentaZeSouboruUI();
                 break;
            case 0:
                break;
            default:
                System.out.println("Neplatná volba, zkuste to znovu.");
        }
               if (volba != 0) {
             System.out.println("\nStiskněte Enter pro pokračování...");
             scanner.nextLine();
        }
    }
    
    private static void pridejStudentaUI() {
        System.out.print("Zadejte jméno studenta: ");
        String jmeno = scanner.nextLine();
        System.out.print("Zadejte příjmení studenta: ");
        String prijmeni = scanner.nextLine();
        System.out.print("Zadejte rok narození studenta: ");
        int rokNarozeni = nactiCeleCislo("rok narození");
        if (rokNarozeni == Integer.MIN_VALUE) return; 

        System.out.print("Zadejte obor (Telekomunikace / Kyberbezpečnost): ");
        String obor = scanner.nextLine();

        databaze.pridejStudenta(jmeno, prijmeni, rokNarozeni, obor);
    }

     private static void pridejZnamkuUI() {
         System.out.print("Zadejte ID studenta pro přidání známky: ");
         int id = nactiCeleCislo("ID studenta");
         if (id == Integer.MIN_VALUE) return;

         System.out.print("Zadejte známku (1-5): ");
         int znamka = nactiCeleCislo("známku");
         if (znamka == Integer.MIN_VALUE) return;

         databaze.pridejZnamku(id, znamka);
     }

    private static void odeberStudentaUI() {
        System.out.print("Zadejte ID studenta k odstranění: ");
        int id = nactiCeleCislo("ID studenta");
        if (id == Integer.MIN_VALUE) return;
        databaze.odeberStudenta(id);
    }

     private static void vyhledejStudentaUI() {
         System.out.print("Zadejte ID studenta k vyhledání: ");
         int id = nactiCeleCislo("ID studenta");
         if (id == Integer.MIN_VALUE) return;
         databaze.vypisInformaceStudenta(id);
     }

     private static void provedDovednostUI() {
          System.out.print("Zadejte ID studenta pro provedení dovednosti: ");
          int id = nactiCeleCislo("ID studenta");
          if (id == Integer.MIN_VALUE) return;
          databaze.provedDovednostStudenta(id);
     }

     private static void vypisPrumerOboruUI() {
         System.out.print("Zadejte obor pro výpis průměru (Telekomunikace / Kyberbezpečnost): ");
         String obor = scanner.nextLine();
         databaze.vypisPrumerOboru(obor);
     }

     private static void vypisPocetStudentuUI() {
          System.out.print("Zadejte obor pro výpis počtu studentů (Telekomunikace / Kyberbezpečnost): ");
          String obor = scanner.nextLine();
          databaze.vypisPocetStudentuOboru(obor);
     }

     private static void ulozStudentaDoSouboruUI() {
        System.out.print("Zadejte ID studenta k uložení do souboru: ");
        int id = nactiCeleCislo("ID studenta");
        if (id == Integer.MIN_VALUE) return;

        System.out.print("Zadejte název souboru (např. student.ser): ");
        String soubor = scanner.nextLine();
        databaze.ulozStudentaDoSouboru(id, soubor);
     }

     private static void nactiStudentaZeSouboruUI() {
          System.out.print("Zadejte název souboru pro načtení studenta: ");
          String soubor = scanner.nextLine();
          Student nacteny = databaze.nactiStudentaZeSouboru(soubor);
          if (nacteny != null) {
              System.out.println("Načtený student (nebyl přidán do aktuální databáze):");
              System.out.println(nacteny);
          }
     }


  
    private static int nactiCeleCislo(String popis) {
        int cislo = Integer.MIN_VALUE; 
        boolean uspesne = false;
        while (!uspesne) {
            try {
                cislo = scanner.nextInt();
                uspesne = true;
            } catch (InputMismatchException e) {
                System.err.println("Neplatný vstup. Zadejte prosím celé číslo pro " + popis + ".");
              
            } finally {
                 scanner.nextLine(); 
            }
             if (!uspesne) {
                 System.out.print("Zadejte " + popis + " znovu: ");
             }
        }
        return cislo;
    }
}