package mpp;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Game implements Serializable {
    private int id;
    private Configuratie configuratie;
    private String oraStart;
    private int nrIncercari;
    private String cuvant;
    private Integer score;

    public Game(int id, Configuratie configuratie, String oraStart, int nrIncercari, String cuvant, Integer score) {
        this.id = id;
        this.configuratie = configuratie;
        this.oraStart = oraStart;
        this.nrIncercari = nrIncercari;
        this.cuvant = cuvant;
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

    public String getOraStart() {
        return oraStart;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getCuvant() {
        return cuvant;
    }

    public void setCuvant(String cuvant) {
        this.cuvant = cuvant;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConfiguratie(Configuratie configuratie) {
        this.configuratie = configuratie;
    }


    public void setNrIncercari(int nrIncercari) {
        this.nrIncercari = nrIncercari;
    }

    public int getId() {
        return id;
    }

    public Configuratie getConfiguratie() {
        return configuratie;
    }

    public int getNrIncercari() {
        return nrIncercari;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", configuratie=" + configuratie +
                ", oraStart=" + oraStart +
                ", nrIncercari=" + nrIncercari +
                ", cuvant='" + cuvant + '\'' +
                '}';
    }
}
