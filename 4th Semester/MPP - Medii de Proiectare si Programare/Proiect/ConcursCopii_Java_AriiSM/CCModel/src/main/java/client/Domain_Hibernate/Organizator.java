package client.Domain_Hibernate;

import jakarta.persistence.Entity;
import java.util.Objects;

@Entity
public class Organizator extends Persoana {
    private String parola;

    public Organizator() {
    }

    public Organizator(String lastName, String firstName, String parola) {
        super(lastName, firstName);
        this.parola = parola;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    public String toString() {
        return "Organizator {" +
                "nume :" + this.getLastName() + '\'' +
                "prenume: " + this.getFirstName() + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Organizator that = (Organizator) o;
        return Objects.equals(parola, that.parola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parola);
    }
}
