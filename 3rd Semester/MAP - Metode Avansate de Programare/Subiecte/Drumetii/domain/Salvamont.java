package com.example.clienti_locatie_horel_rezervare.domain;

public class Salvamont extends Persoana{
    private String locatie;

    public Salvamont(Long aLong, String nume, String prenume, String locatie) {
        super(aLong, nume, prenume);
        this.locatie = locatie;
    }

    @Override
    public String toString() {
        return "Salvamont{" +
                "locatie='" + locatie + '\'' +
                '}';
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getLocatie() {
        return locatie;
    }
}
