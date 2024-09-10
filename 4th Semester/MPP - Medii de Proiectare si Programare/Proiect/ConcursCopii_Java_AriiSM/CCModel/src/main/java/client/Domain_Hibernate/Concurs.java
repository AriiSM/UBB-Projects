package client.Domain_Hibernate;

import java.util.Objects;

@jakarta.persistence.Entity
public class Concurs extends Entity<Integer>{
    private Categorie categorie;
    private Proba proba;

    public Concurs() {}
    public Concurs(Categorie categorie, Proba proba) {
        this.categorie = categorie;
        this.proba = proba;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Proba getProba() {
        return proba;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    @Override
    public String toString() {
        return "Concurs {" +
                "categorie=" + categorie +
                ", proba=" + proba +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Concurs concurs = (Concurs) o;
        return categorie == concurs.categorie && proba == concurs.proba;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), categorie, proba);
    }
}
