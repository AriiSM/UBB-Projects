package mpp;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Game implements Serializable{
    private Integer id;
    private Integer puncte;
    private Integer durata;
    private Configurare configurare;

    public Game(Integer id, Integer puncte, Integer durata, Configurare configurare) {
        this.id = id;
        this.puncte = puncte;
        this.durata = durata;
        this.configurare = configurare;
    }

    public void setConfigurare(Configurare configurare) {
        this.configurare = configurare;
    }

    public Configurare getConfigurare() {
        return configurare;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPuncte() {
        return puncte;
    }

    public Integer getDurata() {
        return durata;
    }

    public void setId(Integer idGame) {
        this.id = idGame;
    }

    public void setPuncte(Integer puncte) {
        this.puncte = puncte;
    }

    public void setDurata(Integer durata) {
        this.durata = durata;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", puncte=" + puncte +
                ", durata=" + durata +
                ", configurare=" + configurare +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(puncte, game.puncte) && Objects.equals(durata, game.durata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, puncte, durata);
    }
}
