package client.Domain_Simplu;

import client.Domain_Simplu.Persoana;

public class Organizator extends Persoana {
    private String parola;


    public Organizator( String lastName, String firstName, String parola) {
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
}
