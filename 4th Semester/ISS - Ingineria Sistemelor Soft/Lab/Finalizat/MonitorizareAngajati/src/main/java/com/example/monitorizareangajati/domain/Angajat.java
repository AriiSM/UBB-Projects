package com.example.monitorizareangajati.domain;

public class Angajat extends User{
    private String nume;
    private String prenume;
    private StatusAngajat status;

    public Angajat(String username, String password, String email, String nume, String prenume, StatusAngajat status) {
        super(username, password, email);
        this.nume = nume;
        this.prenume = prenume;
        this.status = status;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getEmail() {
        return super.getEmail();
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public StatusAngajat getStatus() {
        return status;
    }

    public void setStatus(StatusAngajat status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Angajat{" +
                "nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", status=" + status +
                '}';
    }
}
