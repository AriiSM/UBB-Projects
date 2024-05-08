package com.example.clienti_locatie_horel_rezervare.domain;

public class Persoana extends Entity<Long>{
    private String nume;
    private String prenume;

    public Persoana(Long aLong, String nume, String prenume) {
        super(aLong);
        this.nume = nume;
        this.prenume = prenume;
    }


    @Override
    public String toString() {
        return "Persoana{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                '}';
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }


}
