package mpp;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="Configuratie")
public class Configuratie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "conf")
    private String configuratie;

    public Configuratie(int id, String configuratie) {
        this.id = id;
        this.configuratie = configuratie;
    }

    public Configuratie(){};

    @Override
    public String toString() {
        return "Configuratie{" +
                "id=" + id +
                ", configuratie='" + configuratie + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConfiguratie(String configuratie) {
        this.configuratie = configuratie;
    }


    public int getId() {
        return id;
    }

    public String getConfiguratie() {
        return configuratie;
    }

}
