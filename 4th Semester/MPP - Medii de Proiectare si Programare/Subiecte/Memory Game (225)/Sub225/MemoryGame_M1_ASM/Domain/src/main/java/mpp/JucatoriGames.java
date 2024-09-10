package mpp;

import java.io.Serializable;

public class JucatoriGames implements Serializable {
    private Integer id;
    private Game game;
    private Jucator jucator;

    public JucatoriGames(Integer id, Game game, Jucator jucator) {
        this.id = id;
        this.game = game;
        this.jucator = jucator;
    }

    public Game getGame() {
        return game;
    }

    public Integer getId() {
        return id;
    }

    public Jucator getJucator() {
        return jucator;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setJucator(Jucator jucator) {
        this.jucator = jucator;
    }

    @Override
    public String toString() {
        return "JucatoriGames{" +
                "game=" + game +
                ", jucator=" + jucator +
                '}';
    }
}
