package server.dto;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private final String lastName;
    private final String firstName;
    private final Integer age;
    private final Integer id;

    public ParticipantDTO(Integer id, String lastName, String firstName, Integer age) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }


    @Override
    public String toString() {
        return "ParticipantDTO{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                ", id=" + id +
                '}';
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getAge() {
        return age;
    }
}
