package mpp;

import java.io.Serializable;

public class Jucator implements Serializable {
    private Integer id ;
    private String username;

    public Jucator(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Jucator(String username) {
        this.username = username;
        this.id = -1;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public String toString() {
        return "Jucator{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
