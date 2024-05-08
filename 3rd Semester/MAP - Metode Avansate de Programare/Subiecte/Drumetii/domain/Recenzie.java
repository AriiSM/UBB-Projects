package com.example.clienti_locatie_horel_rezervare.domain;

public class Recenzie extends Entity<Long>{
    private Long id_Coordonator;
    private Long id_Drumetie;
    private String descriere;
    private Integer stelute;

    public Recenzie(Long aLong, Long id_Coordonator, Long id_Drumetie, String descriere, Integer stelute) {
        super(aLong);
        this.id_Coordonator = id_Coordonator;
        this.id_Drumetie = id_Drumetie;
        this.descriere = descriere;
        this.stelute = stelute;
    }

    @Override
    public String toString() {
        return "Recenzie{" +
                "id_Coordonator=" + id_Coordonator +
                ", id_Drumetie=" + id_Drumetie +
                ", descriere='" + descriere + '\'' +
                ", stelute=" + stelute +
                '}';
    }

    public void setId_Coordonator(Long id_Coordonator) {
        this.id_Coordonator = id_Coordonator;
    }

    public void setId_Drumetie(Long id_Drumetie) {
        this.id_Drumetie = id_Drumetie;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public void setStelute(Integer stelute) {
        this.stelute = stelute;
    }

    public Long getId_Coordonator() {
        return id_Coordonator;
    }

    public Long getId_Drumetie() {
        return id_Drumetie;
    }

    public String getDescriere() {
        return descriere;
    }

    public Integer getStelute() {
        return stelute;
    }
}
