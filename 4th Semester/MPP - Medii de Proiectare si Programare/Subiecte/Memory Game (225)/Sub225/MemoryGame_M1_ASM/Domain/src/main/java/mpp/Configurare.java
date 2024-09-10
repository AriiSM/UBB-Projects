package mpp;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "Configurare")
public class Configurare implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "conf")
    private String configurare;


    public Configurare(Integer id, String configurare) {
        this.id = id;
        this.configurare = configurare;
    }

    public Configurare() {
    }

    ;

    public Integer getId() {
        return id;
    }

    public String getConfigurare() {
        return configurare;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public void setConfigurare(String configurare) {
        this.configurare = configurare;
    }

    @Override
    public String toString() {
        return "Configurare{" +
                "id=" + id +
                ", configurare='" + configurare + '\'' +
                '}';
    }
}
