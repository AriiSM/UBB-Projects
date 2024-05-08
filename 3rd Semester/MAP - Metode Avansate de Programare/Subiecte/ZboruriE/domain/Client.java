package com.example.zboruri.domain;

public class Client extends Entity<Long>{
    private String username;
    private String name;

    public Client(Long aLong, String username, String name) {
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
        return "Client{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
