package client.Domain_Hibernate;

import jakarta.persistence.Entity;

import java.util.Objects;

@Entity
public class Participant extends Persoana{
    private Integer age;


    public Participant() {}

    public Participant(String lastName, String firstName, Integer age) {
        super(lastName, firstName);
        this.age = age;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    public String getFirstName() {
        return super.getFirstName();
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Participant {" +
                "varsta: " + this.age + '\'' +
                "nume :" + this.getLastName() + '\'' +
                "prenume: " + this.getFirstName() + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Participant that = (Participant) o;
        return Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), age);
    }
}

