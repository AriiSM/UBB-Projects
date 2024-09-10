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
    @Column(name = "masca")
    private String masca;
    @Column(name = "cuvant")
    private String cuvant;

    public Configurare(Integer id, String masca, String cuvant) {
        this.id = id;
        this.masca = masca;
        this.cuvant = cuvant;
    }

    public Configurare() {
    };

    @Override
    public String toString() {
        return "Configurare{" +
                "id=" + id +
                ", masca='" + masca + '\'' +
                ", cuvant='" + cuvant + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getMasca() {
        return masca;
    }

    public String getCuvant() {
        return cuvant;
    }
}
