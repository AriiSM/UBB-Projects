package mpp;

import java.io.Serializable;

public class JucatorGame implements Serializable {
    private Integer id;
    private Persoana persoana;
    private Game game;

    @Override
    public String toString() {
        return "JucatorGame{" +
                "id=" + id +
                ", persoana=" + persoana +
                ", game=" + game +
                '}';
    }
    public JucatorGame(){};

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPersoana(Persoana persoana) {
        this.persoana = persoana;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getId() {
        return id;
    }

    public Persoana getPersoana() {
        return persoana;
    }

    public Game getGame() {
        return game;
    }

    public JucatorGame(Integer id, Persoana persoana, Game game) {
        this.id = id;
        this.persoana = persoana;
        this.game = game;
    }
}
