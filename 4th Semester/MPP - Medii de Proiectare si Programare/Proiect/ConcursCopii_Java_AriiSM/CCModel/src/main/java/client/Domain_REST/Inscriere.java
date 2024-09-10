package client.Domain_REST;

public class Inscriere extends Entity<Integer> {
    private Participant participant;
    private Concurs concurs;

    public Inscriere(Participant participant, Concurs concurs) {
        this.participant = participant;
        this.concurs = concurs;
    }

    public Inscriere() {
        super();
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
