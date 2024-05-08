package com.example.taximetrie.domain;

public class Persoana extends Entity<Long>{
    private String username;
    private String name;

    public Persoana(Long aLong, String username, String name) {
        super(aLong);
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public String toString() {
        return "Persoana{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
