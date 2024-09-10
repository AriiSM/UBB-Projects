package com.example.socialnetwork_1v.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Invitatii extends Entity<Long> {
    private Utilizator fromInvite;
    private Utilizator toInvite;
    private LocalDateTime dateInvite;
    private String status;

    public Invitatii(Long id, Utilizator fromInvite, Utilizator toInvite, LocalDateTime dateInvite, String status) {
        this.setId(id);
        this.fromInvite = fromInvite;
        this.toInvite = toInvite;
        this.dateInvite = dateInvite;
        this.status = status;
    }

    public String getDataString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dateInvite.format(formatter);
    }

    public Utilizator getFromInvite() {
        return fromInvite;
    }

    public Utilizator getToInvite() {
        return toInvite;
    }

    public LocalDateTime getDateInvite() {
        return dateInvite;
    }

    public String getStatus() {
        return status;
    }

    public void setFromInvite(Utilizator fromInvite) {
        this.fromInvite = fromInvite;
    }

    public void setToInvite(Utilizator toInvite) {
        this.toInvite = toInvite;
    }

    public void setDateInvite(LocalDateTime dateInvite) {
        this.dateInvite = dateInvite;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String data_mesaj = dateInvite.format(formatter);
        return new String(getId().toString() + " ->invitatie de la " + fromInvite.getFirst_name() + " " + fromInvite.getFirst_name() + " "  + data_mesaj + " " + status.toString());
    }
}
