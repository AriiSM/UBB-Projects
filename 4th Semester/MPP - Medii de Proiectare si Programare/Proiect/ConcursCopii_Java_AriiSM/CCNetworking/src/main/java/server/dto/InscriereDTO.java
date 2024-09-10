package server.dto;

import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Participant;

import java.io.Serializable;

public class InscriereDTO implements Serializable {
    Integer id;
    Participant participant;
    Concurs concurs;

    public InscriereDTO(Integer id, Participant participant, Concurs concurs) {
        this.id = id;
        this.participant = participant;
        this.concurs = concurs;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "InscriereDTO{" +
                "participant=" + participant +
                ", concurs=" + concurs +
                '}';
    }

    public Participant getParticipant() {
        return participant;
    }

    public Concurs getConcurs() {
        return concurs;
    }
}
