package com.example.socialnetwork_1v.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utilizator extends Entity<Long> {
    private final String last_name;
    private final String first_name;
    private final List<Utilizator> friends = new ArrayList<>();

    public Utilizator(String lastName, String firstName) {
        this.last_name = lastName;
        this.first_name = firstName;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public List<String> getFriendsPrenume(){
        List<String> prenume = new ArrayList<>();
        for(var f:friends){
            prenume.add(f.first_name);
        }
        return prenume;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }

    public void addFriend(Utilizator utilizator) {
        if (!friends.contains(utilizator))
            friends.add(utilizator);
    }

    public boolean isFriend(Utilizator user) {
        return this.getFriends().contains(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getLast_name().equals(that.getLast_name()) &&
                getFirst_name().equals(that.getFirst_name()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public String toString() {
        String nume;
        List<String> listsName = new ArrayList<>();
        if (friends != null)
            for (var p : friends) {
                nume = p.last_name + " " + p.getFirst_name();
                listsName.add(nume);
            }
        if (!listsName.isEmpty()) {
            return "Utilizator{" +
                    "ID:" + id +
                    ",       first_name=" + first_name + '\'' +
                    "       last_name=" + last_name + '\'' +
                    ",       friends=" + listsName +
                    '}';
        } else {
            return "Utilizator{" +
                    "ID:" + id +
                    ",       first_name=" + first_name + '\'' +
                    "       last_name=" + last_name + '\'' +
                    ",       friends=" + 0 +
                    '}';
        }

    }

    @Override
    public int hashCode() {
        return Objects.hash(getLast_name(), getFirst_name(), getFriends());
    }
}
