package com.example.taximetrie.domain;

import java.util.List;

public class Sofer extends Persoana{
    private String indicativMasina;


    public Sofer(Long aLong, String username, String name, String indicativMasina) {
        super(aLong, username, name);
        this.indicativMasina = indicativMasina;
    }

    public void setIndicativMasina(String indicativMasina) {
        this.indicativMasina = indicativMasina;
    }
    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public String getIndicativMasina() {
        return indicativMasina;
    }

    @Override
    public String toString() {
        return "Sofer{" +
                "indicativMasina='" + indicativMasina + '\'' +
                '}';
    }
}
