package com.example.socialnetwork_1v.ui;

import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.service.DbUtilizatorService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

public class UiDB {
    private final DbUtilizatorService service;
    Scanner reader = new Scanner(System.in);

    public UiDB(DbUtilizatorService service) {
        this.service = service;
    }

    public void run() {

        boolean bool = true;
        while (bool) {
            Scanner reader = new Scanner(System.in);
            this.printMeniu();
            String cause = reader.nextLine();
            switch (cause) {
                case "1":
                    this.adaugaUser();
                    break;
                case "2":
                    this.stergeUser();
                    break;
                case "3":
                    //this.adaugaPrietenie();
                    break;
                case "4":
                    //this.stergePrietenie();
                    break;
                case "5":
                    this.printNrComunitati();
                    break;
                case "6":
                    this.printComunitateSociabila();
                    break;
                case "10":
                    service.getAllUsers().forEach(System.out::println);
                    break;
                case "11":
                    service.getAllFriends().forEach(System.out::println);
                    break;
                case "12":
                    this.prieteniiMei();
                    break;
                case "0":
                    System.out.println("Closing....");
                    bool = false;
                    break;
            }
        }
    }

    private void printMeniu() {
        System.out.println("MENIU");
        System.out.println("1. Adaugare utilizator");
        System.out.println("2. Sterge utilizator");
        System.out.println();
        System.out.println("3. Adaugare prietenie");
        System.out.println("4. Sterge prietenie");
        System.out.println();
        System.out.println("5.Numar de comunitati");
        System.out.println("6. Cea mai sociabila comunitate");
        System.out.println();
        System.out.println("10. Afisare utilizator");
        System.out.println("11. Afisare prietenii");
        System.out.println();
        System.out.println("12. Prietenii mei(nume+prenume+luna)");
        System.out.println("0. Exit");
        System.out.println();
    }

    private void prieteniiMei() {
        System.out.println("Introdu prenume+nume:");
        String numeSiPrenume = reader.nextLine();
        String[] list = service.desparte(numeSiPrenume);
        System.out.println("Luna in care vrei sa vezi prietenii:");
        System.out.println("JANUARY\nFEBRUARY\nMARCH\nAPRIL\nMAY\nJUNE\nJULY\nAUGUST\nSEPTEMBER\nOCTOBER\nNOVEMBER\nDECEMBER");
        String luna = reader.nextLine();

        Map<Utilizator, LocalDateTime> map = service.prieteniiMei(list[0], list[1], luna);
        if (map.isEmpty())
            System.out.println("Nu ai prieteni in acea luna!");
        else {
            for (var m : map.entrySet()) {
                System.out.println(m.getKey().getLast_name() + " | " + m.getKey().getFirst_name() + " | " + m.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        }
    }

    private void adaugaUser() {
        System.out.println("Adauga utilizator (prenume si nume)");
        String numeSiPrenume = reader.nextLine();
        String[] list = service.desparte(numeSiPrenume);

        try {
            service.addUtilizator(list[0], list[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void stergeUser() {
        System.out.println("Sterge utilizator (prenume si nume)");
        String numeSiPrenume1 = reader.nextLine();
        String[] list1 = service.desparte(numeSiPrenume1);
        try {
            //Ariana Stan
            service.stergeUtilizator(list1[0], list1[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void adaugaPrietenie() {
//        System.out.println("Creaza o prietenie intre 2 utilzatori");
//        System.out.println("User1 - prenume si nume:");
//        String numeSiPrenumeUser1 = reader.nextLine();
//        String[] listUser1 = service.desparte(numeSiPrenumeUser1);
//        System.out.println("User2 - prenume si nume");
//        System.out.println("FirstNameP2:");
//        String numeSiPrenumeUser2 = reader.nextLine();
//        String[] listUser2 = service.desparte(numeSiPrenumeUser2);
//        try {
//            service.adaugaPrietenie(listUser1[0], listUser1[1], listUser2[0], listUser2[1]);
//            service.adaugaPrietenie(listUser2[0], listUser2[1], listUser1[0], listUser1[1]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private void stergePrietenie() {
//        System.out.println("Sterge o prietenie dintre 2 utilizatori");
//        System.out.println("User1 - nume si prenume:");
//        String numeSiPrenumeUser1S = reader.nextLine();
//        String[] listUser1S = service.desparte(numeSiPrenumeUser1S);
//        System.out.println("User2 - nume si prenume");
//        System.out.println("FirstNameP2:");
//        String numeSiPrenumeUser2S = reader.nextLine();
//        String[] listUser2S = service.desparte(numeSiPrenumeUser2S);
//        try {
//            service.stergePrietenie(listUser1S[1], listUser1S[0], listUser2S[1], listUser2S[0]);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void printNrComunitati() {
        System.out.println("Numarul de comunitati este: " + service.numarComunitati());
    }

    private void printComunitateSociabila() {
        if (!service.sociabilComunitate().isEmpty()) {
            System.out.println("Cea mai sociabila comunitate:");
            service.sociabilComunitate().forEach(System.out::println);
        } else {
            System.out.println("Cea mai sociabila lista este dintr o persona");
        }
    }
}
