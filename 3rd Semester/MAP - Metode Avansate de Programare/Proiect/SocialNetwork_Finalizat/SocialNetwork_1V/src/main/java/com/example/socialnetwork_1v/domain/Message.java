package com.example.socialnetwork_1v.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Message extends Entity<Long>{
    private String mesaj;
    private Utilizator from;
    private List<Utilizator> to;
    private LocalDateTime date;
    private Optional<Long> idReply;

    public Message(String mesaj, Utilizator from, List<Utilizator> to, LocalDateTime date) {
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public Message(Long id, String mesaj, Utilizator from, List<Utilizator> to, LocalDateTime date) {
        this.setId(id);
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.date = date;
        this.idReply = null;
    }
    public Message(Long id, String mesaj, Utilizator from, List<Utilizator> to, LocalDateTime date,Optional<Long> idReply) {
        this.setId(id);
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.date = date;
        this.idReply = idReply;
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }
    public String toStringNames(){
        String names = new String();
        for(var user:this.to){
            names+= user.getFirst_name()+" "+user.getLast_name()+" | ";
        }
        return names;
    }

    public String getMesaj() {
        return mesaj;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public String getToIdString(){
        String ids = new String();
        for(var f:to){
            ids+=f.getId();
        }
        return ids;
    }

    public List<String> getToPrenume(){
        List<String> prenume = new ArrayList<>();
        for(var f:to){
            prenume.add(f.getFirst_name());
        }
        return prenume;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDataString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return date.format(formatter);
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setIdReply(Optional<Long> idReply) {
        this.idReply = idReply;
    }

    public Optional<Long> getIdReply() {
        return idReply;
    }

    @Override
    public String toString() {
        DateTimeFormatter formetter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String data_mesaj = date.format(formetter);
        return new String(from.getFirst_name()+" "+
                from.getFirst_name()+ ": "+
                mesaj+" -> "+
                data_mesaj
                );
    }

}
