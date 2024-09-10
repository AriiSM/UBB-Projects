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
    @Column(name = "coef00")
    private String coef00;
    @Column(name = "coef01")
    private String coef01;
    @Column(name = "coef02")
    private String coef02;
    @Column(name = "coef10")
    private String coef10;
    @Column(name = "coef11")
    private String coef11;
    @Column(name = "coef12")
    private String coef12;
    @Column(name = "coef20")
    private String coef20;
    @Column(name = "coef21")
    private String coef21;
    @Column(name = "coef22")
    private String coef22;


    public Configurare(Integer id, String coef00, String coef01, String coef02, String coef10, String coef11, String coef12, String coef20, String coef21, String coef22) {
        this.id = id;
        this.coef00 = coef00;
        this.coef01 = coef01;
        this.coef02 = coef02;
        this.coef10 = coef10;
        this.coef11 = coef11;
        this.coef12 = coef12;
        this.coef20 = coef20;
        this.coef21 = coef21;
        this.coef22 = coef22;
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

    public String getCoef00() {
        return coef00;
    }

    public String getCoef01() {
        return coef01;
    }

    public String getCoef02() {
        return coef02;
    }

    public String getCoef10() {
        return coef10;
    }

    public String getCoef11() {
        return coef11;
    }

    public String getCoef12() {
        return coef12;
    }

    public String getCoef20() {
        return coef20;
    }

    public String getCoef21() {
        return coef21;
    }

    public String getCoef22() {
        return coef22;
    }

    public void setCoef00(String coef00) {
        this.coef00 = coef00;
    }

    public void setCoef01(String coef01) {
        this.coef01 = coef01;
    }

    public void setCoef02(String coef02) {
        this.coef02 = coef02;
    }

    public void setCoef10(String coef10) {
        this.coef10 = coef10;
    }

    public void setCoef11(String coef11) {
        this.coef11 = coef11;
    }

    public void setCoef12(String coef12) {
        this.coef12 = coef12;
    }

    public void setCoef20(String coef20) {
        this.coef20 = coef20;
    }

    public void setCoef21(String coef21) {
        this.coef21 = coef21;
    }

    public void setCoef22(String coef22) {
        this.coef22 = coef22;
    }
}
