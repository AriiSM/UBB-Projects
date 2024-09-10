package server.dto;

import java.io.Serializable;

public class OrganizatorDTO implements Serializable {
    private final String lastName;
    private final String firstName;
    private final String parola;

    public OrganizatorDTO(String lastName, String firstName, String parola) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.parola = parola;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getParola() {
        return parola;
    }

    @Override
    public String toString() {
        return "OrganizatorDTO{" +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}
