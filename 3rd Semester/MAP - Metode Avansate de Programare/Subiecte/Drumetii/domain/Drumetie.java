package com.example.clienti_locatie_horel_rezervare.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
public class Drumetie extends Entity<Long>{
    private Long id_Coordonator;
    private List<Participant> participanti;
    private Nivel nivel;
    private Integer durata;
    private Timestamp data_Drumetie;

    public Drumetie(Long aLong, Long id_Coordonator, List<Participant> participanti, Nivel nivel, Integer durata, Timestamp data_Drumetie) {
        super(aLong);
        this.id_Coordonator = id_Coordonator;
        this.participanti = participanti;
        this.nivel = nivel;
        this.durata = durata;
        this.data_Drumetie = data_Drumetie;
    }

    @Override
    public String toString() {
        return "Drumetie{" +
                "id_Coordonator=" + id_Coordonator +
                ", participanti=" + participanti +
                ", nivel=" + nivel +
                ", durata=" + durata +
                ", data_Drumetie=" + data_Drumetie +
                '}';
    }

    public void setId_Coordonator(Long id_Coordonator) {
        this.id_Coordonator = id_Coordonator;
    }

    public void setParticipanti(List<Participant> participanti) {
        this.participanti = participanti;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    public void setData_Drumetie(Timestamp data_Drumetie) {
        this.data_Drumetie = data_Drumetie;
    }

    public Long getId_Coordonator() {
        return id_Coordonator;
    }

    public List<Participant> getParticipanti() {
        return participanti;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public Integer getDurata() {
        return durata;
    }

    public Timestamp getData_Drumetie() {
        return data_Drumetie;
    }
}
