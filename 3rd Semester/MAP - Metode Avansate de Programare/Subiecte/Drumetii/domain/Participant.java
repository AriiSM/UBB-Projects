package com.example.clienti_locatie_horel_rezervare.domain;

public class Participant extends Persoana{
    private Nivel nivel;

    public Participant(Long aLong, String nume, String prenume, Nivel nivel) {
        super(aLong, nume, prenume);
        this.nivel = nivel;
    }

    @Override
    public String getNume() {
        return super.getNume();
    }

    @Override
    public String getPrenume() {
        return super.getPrenume();
    }

    @Override
    public String toString() {
        return "Participant{  " +
                "nume    " + getNume()+
                "    prenume    " + getPrenume()+
                "    nivel=    " + nivel +
                "    }";
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Nivel getNivel() {
        return nivel;
    }
}
