package client.Domain_Hibernate;


import jakarta.persistence.ManyToOne;

import java.util.Objects;
@jakarta.persistence.Entity
public class Inscriere extends Entity<Integer>{
    @ManyToOne
    private Participant participant;
    @ManyToOne
    private Concurs concurs;

    public Inscriere(Participant participant, Concurs concurs) {
        this.participant = participant;
        this.concurs = concurs;
    }

    public Inscriere() {

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
        return "Inscriere{" +
                "participant=" + participant +
                ", concurs=" + concurs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Inscriere inscriere = (Inscriere) o;
        return Objects.equals(participant, inscriere.participant) && Objects.equals(concurs, inscriere.concurs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), participant, concurs);
    }
}
