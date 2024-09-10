package client.Domain_Simplu;

import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Entity;
import client.Domain_Simplu.Participant;

public class Inscriere extends Entity<Integer> {
    private Participant participant;
    private Concurs concurs;

    public Inscriere(Participant participant, Concurs concurs) {
        this.participant = participant;
        this.concurs = concurs;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public Participant getParticipant() {
        return participant;
    }

    public Concurs getConcurs() {
        return concurs;
    }

    @Override
    public String toString() {
        return "Inscriere {" +
                "participant=" + participant +
                ", concurs=" + concurs +
                '}';
    }
}
