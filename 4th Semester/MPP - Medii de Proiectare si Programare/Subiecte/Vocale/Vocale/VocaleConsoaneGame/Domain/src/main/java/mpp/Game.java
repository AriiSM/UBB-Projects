package mpp;

import java.io.Serializable;

public class Game implements Serializable {
    private Integer id;
    private Configurare configurare;
    private Integer score;
    private String oraStart;
    private Integer noIncercari;
    private String litereGhicite;


    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", configurare=" + configurare +
                ", score=" + score +
                ", oraStart='" + oraStart + '\'' +
                ", noIncercari=" + noIncercari +
                '}';
    }

    public Game(){};
    public void setId(Integer id) {
        this.id = id;
    }

    public void setConfigurare(Configurare configurare) {
        this.configurare = configurare;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setOraStart(String oraStart) {
        this.oraStart = oraStart;
    }

    public void setNoIncercari(Integer noIncercari) {
        this.noIncercari = noIncercari;
    }

    public Integer getId() {
        return id;
    }

    public Configurare getConfigurare() {
        return configurare;
    }

    public Integer getScore() {
        return score;
    }

    public String getOraStart() {
        return oraStart;
    }

    public Integer getNoIncercari() {
        return noIncercari;
    }

    public void setLitereGhicite(String litereGhicite) {
        this.litereGhicite = litereGhicite;
    }

    public String getLitereGhicite() {
        return litereGhicite;
    }

    public Game(Integer id, Configurare configurare, Integer score, String oraStart, Integer noIncercari, String litereGhicite) {
        this.id = id;
        this.configurare = configurare;
        this.score = score;
        this.oraStart = oraStart;
        this.noIncercari = noIncercari;
        this.litereGhicite = litereGhicite;
    }
}
