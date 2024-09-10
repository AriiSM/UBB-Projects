package client.Domain_Hibernate;

import client.Domain_Hibernate.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@jakarta.persistence.Entity
public class Persoana extends Entity<Integer> {
    private String lastName;
    private String firstName;

    public Persoana() {}

    public Persoana(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "Persoana{" +
                "last_name='" + lastName + '\'' +
                ", first_name='" + firstName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Persoana persoana = (Persoana) o;
        return Objects.equals(lastName, persoana.lastName) && Objects.equals(firstName, persoana.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lastName, firstName);
    }
}
