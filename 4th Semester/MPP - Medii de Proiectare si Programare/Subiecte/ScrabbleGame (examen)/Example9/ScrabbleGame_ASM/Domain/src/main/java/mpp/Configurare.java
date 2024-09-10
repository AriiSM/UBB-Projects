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
    @Column(name = "cuvant")
    private String cuvant;
    @Column(name = "litere")
    private String litere;

    public Configurare(Integer id, String cuvant, String litere) {
        this.id = id;
        this.cuvant = cuvant;
        this.litere = litere;
    }

    public Configurare() {
    }

    ;

    public Integer getId() {
        return id;
    }



    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "Configurare{" +
                "id=" + id +
                ", cuvant='" + cuvant + '\'' +
                ", litere='" + litere + '\'' +
                '}';
    }

    public void setCuvant(String cuvant) {
        this.cuvant = cuvant;
    }

    public String getCuvant() {
        return cuvant;
    }

    public void setLitere(String litere) {
        this.litere = litere;
    }

    public String getLitere() {
        return litere;
    }
}
