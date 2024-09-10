package mpp;

import java.io.Serializable;

public class Game implements Serializable {
    private Integer id;
    private Configurare configurare;
    private Integer scor;
    private Integer durata;

    public Game(Integer id, Configurare configurare, Integer scor, Integer durata) {
        this.id = id;
        this.configurare = configurare;
        this.scor = scor;
        this.durata = durata;
    }
    public Game(){};

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", configurare=" + configurare +
                ", scor=" + scor +
                ", durata=" + durata +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setConfigurare(Configurare configurare) {
        this.configurare = configurare;
    }

    public void setScor(Integer scor) {
        this.scor = scor;
    }

    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    public Integer getId() {
        return id;
    }

    public Configurare getConfigurare() {
        return configurare;
    }

    public Integer getScor() {
        return scor;
    }

    public Integer getDurata() {
        return durata;
    }
}
