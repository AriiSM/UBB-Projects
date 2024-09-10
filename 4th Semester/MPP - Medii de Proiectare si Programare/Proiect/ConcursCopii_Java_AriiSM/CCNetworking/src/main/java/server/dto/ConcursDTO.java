package server.dto;

import client.Domain_Simplu.Categorie;
import client.Domain_Simplu.Proba;

import java.io.Serializable;

public class ConcursDTO implements Serializable {
    Integer id;
    Proba proga;
    Categorie categorie;

    @Override
    public String toString() {
        return "ConcursDTO{" +
                "proga=" + proga +
                ", categorie=" + categorie +
                '}';
    }

    public Proba getProga() {
        return proga;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public ConcursDTO(Integer id, Proba proga, Categorie categorie) {
        this.id = id;
        this.proga = proga;
        this.categorie = categorie;
    }

    public Integer getId() {
        return id;
    }
}
