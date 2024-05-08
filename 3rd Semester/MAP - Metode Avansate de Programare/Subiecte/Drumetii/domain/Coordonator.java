package com.example.clienti_locatie_horel_rezervare.domain;

import java.util.List;

public class Coordonator extends Participant{
    private Double stelute;
    private List<Recenzie> recenzii;

    public Coordonator(Long aLong, String nume, String prenume, Nivel nivel, Double stelute, List<Recenzie> recenzii) {
        super(aLong, nume, prenume, nivel);
        this.stelute = stelute;
        this.recenzii = recenzii;
    }

    @Override
    public String toString() {
        return "Coordonator{" +
                "stelute=" + stelute +
                ", recenzii=" + recenzii +
                '}';
    }

    public void setStelute(Double stelute) {
        this.stelute = stelute;
    }

    public void setRecenzii(List<Recenzie> recenzii) {
        this.recenzii = recenzii;
    }

    public Double getStelute() {
        return stelute;
    }

    public List<Recenzie> getRecenzii() {
        return recenzii;
    }
}
