package mpp;

import java.io.Serializable;
import java.util.Objects;

public class Game implements Serializable{
    private Integer id;
    private Integer puncte;
    private String dataOra;
    private Configurare configurare;
    private Integer LitGhicite;

    public Game(Integer id, Integer puncte, String dataOra, Configurare configurare, Integer litGhicite) {
        this.id = id;
        this.puncte = puncte;
        this.dataOra = dataOra;
        this.configurare = configurare;
        LitGhicite = litGhicite;
    }

    public void setPuncte(Integer puncte) {
        this.puncte = puncte;
    }

    public void setDataOra(String dataOra) {
        this.dataOra = dataOra;
    }

    public void setLitGhicite(Integer litGhicite) {
        LitGhicite = litGhicite;
    }

    public String getDataOra() {
        return dataOra;
    }

    public Integer getLitGhicite() {
        return LitGhicite;
    }

    public Game(){};

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

    public String getDurata() {
        return dataOra;
    }

    public void setId(Integer idGame) {
        this.id = idGame;
    }


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", puncte=" + puncte +
                ", dataOra='" + dataOra + '\'' +
                ", configurare=" + configurare +
                ", LitGhicite=" + LitGhicite +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(id, game.id) && Objects.equals(puncte, game.puncte) && Objects.equals(dataOra, game.dataOra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, puncte, dataOra);
    }
}
