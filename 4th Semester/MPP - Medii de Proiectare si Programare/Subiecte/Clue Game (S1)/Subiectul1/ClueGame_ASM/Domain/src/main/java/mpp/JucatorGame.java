package mpp;

import java.io.Serializable;

public class JucatorGame implements Serializable {
    private int id;
    private Persoana persoana;
    private Game game;

    public JucatorGame(int id, Persoana persoana, Game game) {
        this.id = id;
        this.persoana = persoana;
        this.game = game;
    }

    @Override
    public String toString() {
        return "JucatorGame{" +
                "id=" + id +
                ", persoana=" + persoana +
                ", game=" + game +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersoana(Persoana persoana) {
        this.persoana = persoana;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getId() {
        return id;
    }


    public Persoana getPersoana() {
        return persoana;
    }

    public Game getGame() {
        return game;
    }
}
