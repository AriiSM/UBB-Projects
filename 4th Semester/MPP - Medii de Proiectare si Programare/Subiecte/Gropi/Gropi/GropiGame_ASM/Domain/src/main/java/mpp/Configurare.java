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
    private String coef;

    public void setCoef(String coef) {
        this.coef = coef;
    }

    public String getCoef() {
        return coef;
    }

    public Configurare(Integer id, String coef) {
        this.id = id;
        this.coef = coef;
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

}
