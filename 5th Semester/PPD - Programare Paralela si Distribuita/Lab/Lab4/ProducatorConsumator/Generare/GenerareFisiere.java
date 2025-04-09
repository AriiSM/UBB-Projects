package ppd.Generare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GenerareFisiere {
    // Configurarea generării datelor
    private static final int NUMAR_TARI = 3; // Trei tari
    private static final int MIN_CONCURENTI = 80; // Numărul minim de concurenți pe țară
    private static final int MAX_CONCURENTI = 100; // Numărul maxim de concurenți pe țară
    private static final int NUMAR_PROBLEME = 10; // Zece probleme
    private static final double PROBABILITATE_FRAUDA = 0.02; // 2%
    private static final double PROBABILITATE_NEREZOLVARE = 0.10; // 10%

    // Intervalul ID-urilor pentru fiecare țară
    private static int getIntervalStartForCountry(int countryIndex) {
        return (countryIndex - 1) * (MAX_CONCURENTI + 1) + 1; // Interval de ID-uri de la 1 până la MAX_CONCURENTI
    }

    private static int getIntervalEndForCountry(int countryIndex) {
        return countryIndex * (MAX_CONCURENTI + 1);
    }

    // Generare punctaj random
    private static int genereazaPunctaj() {
        Random rand = new Random();
        if (rand.nextDouble() < PROBABILITATE_NEREZOLVARE) {
            return 0; // Nu s-a rezolvat problema
        }
        if (rand.nextDouble() < PROBABILITATE_FRAUDA) {
            return -1; // Frauda
        }
        return rand.nextInt(10) + 1; // Punctaj random între 1 și 10
    }

    // Creare fisier daca nu exista
    private static void creeazaFisierDacaNuExista(String tara, int problema, int startID, int numarConcurenti) throws IOException {
        // Creează calea fișierului
        String folderPath = "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab4\\ProducatorConsumator\\src\\main\\java\\ppd\\data"; // Folderul în care se vor salva fișierele
        File folder = new File(folderPath);

        // Dacă folderul nu există, îl creăm
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                System.out.println("Nu am putut crea folderul 'data'. Verifică permisiunile.");
            }
        }

        // Calea fișierului, folosind numele corect
        String path = folderPath + "/RezultateC" + tara.substring(1) + "_P" + problema + ".txt";
        File fisier = new File(path);

        // Verifica daca fisierul exista deja
        if (!fisier.exists()) {
            // Dacă fișierul nu există, creează-l și adaugă date
            FileWriter writer = new FileWriter(path);
            Random rand = new Random();
            // Adaugă aceleași număr de concurenți pentru toate problemele din țara respectivă
            for (int j = 0; j < numarConcurenti; j++) {
                int idConcurent = startID + j;  // Generăm ID pentru concurent pe baza intervalului
                int punctaj = genereazaPunctaj();
                // Scrierea ID-ului și a punctajului
                writer.write(idConcurent + ", " + (punctaj == 0 ? "" : punctaj) + "\n");
            }
            writer.close();
            System.out.println("Fisierul " + path + " a fost creat cu succes.");
        } else {
            System.out.println("Fisierul " + path + " exista deja.");
        }
    }

    // Generare fisier pentru toate tarile
    private static void genereazaFisierPentruToateTari() throws IOException {
        Random rand = new Random();
        for (int i = 1; i <= NUMAR_TARI; i++) {
            String tara = "C" + i;
            int startID = getIntervalStartForCountry(i); // Calculăm intervalul de start pentru ID-uri
            // Generăm un număr aleatoriu de concurenți pentru fiecare țară
            int numarConcurenti = rand.nextInt(MAX_CONCURENTI - MIN_CONCURENTI + 1) + MIN_CONCURENTI;
            for (int j = 1; j <= NUMAR_PROBLEME; j++) {
                System.out.println("Verificare si creare fisier pentru tara " + tara + ", problema " + j);
                creeazaFisierDacaNuExista(tara, j, startID, numarConcurenti);
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Generarea fisierelor pentru toate tarile
            genereazaFisierPentruToateTari();
            System.out.println("Fisierul pentru toate tarile a fost generat cu succes.");
        } catch (IOException e) {
            System.out.println("A aparut o eroare la generarea fisierelor: " + e.getMessage());
        }
    }
}
