package client.Domain_Simplu;

import client.Domain_Simplu.Persoana;

public class Participant extends Persoana {
    private Integer age;

    public Participant(String lastName, String firstName, Integer age) {
        super(lastName, firstName);
        this.age = age;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public Integer getAge() {
        return age;
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
        return "Participant {" +
                "varsta: " + this.age + '\'' +
                "nume :" + this.getLastName() + '\'' +
                "prenume: " + this.getFirstName() + '\'' +
                "}";
    }
}
