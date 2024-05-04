package com.example.monitorizareangajati.domain;

public class Sarcina extends Entity<Integer>{
    private String descriere;

    public Sarcina(String descriere) {
        this.descriere = descriere;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getDescriere() {
        return descriere;
    }

    @Override
    public String toString() {
        return "Sarcina{" +
                "descriere='" + descriere + '\'' +
                '}';
    }
}
