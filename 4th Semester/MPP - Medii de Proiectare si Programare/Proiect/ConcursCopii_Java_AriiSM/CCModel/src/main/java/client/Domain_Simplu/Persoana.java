package client.Domain_Simplu;

import client.Domain_Simplu.Entity;

public class Persoana extends Entity<Integer> {
    private String lastName;
    private String firstName;

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

    @Override
    public Integer getId() {
        return super.getId();
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
}
