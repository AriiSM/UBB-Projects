package org.example.data;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileComparator {
    public static boolean equalResults(String fname1, String fname2){

        try {
            File fd1 = new File(fname1);
            File fd2 = new File(fname2);
            if(fd1.length() != fd2.length())
                return false;
            Scanner fs1 = new Scanner(fd1);
            Scanner fs2 = new Scanner(fd2);
            while(fs1.hasNextInt())
                if(fs1.nextInt() != fs2.nextInt())
                    return false;
            fs1.close();
            fs2.close();
            return true;
        }
        catch(IOException exception){
            exception.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        if (equalResults("D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab2\\Lab2_Java\\src\\main\\java\\org\\example\\data\\GT.txt", "D:\\Ariana\\Facultate\\SEM5\\PPD\\LAB\\Lab2\\Lab2_Java\\src\\main\\java\\org\\example\\data\\output.txt")) {
            System.out.println("Rezultatele corespund");
        } else {
            System.out.println("Rezultatele NU corespund si nu e OK");
        }
    }
}