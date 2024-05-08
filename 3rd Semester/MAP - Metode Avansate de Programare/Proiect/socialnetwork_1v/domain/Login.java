package com.example.socialnetwork_1v.domain;

public class Login extends Entity<Long>{
    private Long cod;
    private Long utilizator;
    private String username;
    private String parola;

    public Login(Long utilizator, String username, String parola) {
        this.utilizator = utilizator;
        this.username = username;
        this.parola = parola;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public void setUtilizator(Long utilizator) {
        this.utilizator = utilizator;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public Long getCod() {
        return cod;
    }

    public Long getUtilizator() {
        return utilizator;
    }

    public String getUsername() {
        return username;
    }

    public String getParola() {
        return parola;
    }
}
