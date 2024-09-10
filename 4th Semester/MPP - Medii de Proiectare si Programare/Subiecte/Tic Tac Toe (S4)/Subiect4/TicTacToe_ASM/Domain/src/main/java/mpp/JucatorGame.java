package mpp;

import java.io.Serializable;

public class JucatorGame implements Serializable {
    private Integer id;
    private Persoana jucator;
    private Game game;

    public JucatorGame(Integer id, Persoana jucator, Game game) {
        this.id = id;
        this.jucator = jucator;
        this.game = game;
    }

    public JucatorGame() {
    }

    @Override
    public String toString() {
        return "JucatorGame{" +
                "id=" + id +
                ", jucator=" + jucator +
                ", game=" + game +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setJucator(Persoana jucator) {
        this.jucator = jucator;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getId() {
        return id;
    }

    public Persoana getJucator() {
        return jucator;
    }

    public Game getGame() {
        return game;
    }
}
