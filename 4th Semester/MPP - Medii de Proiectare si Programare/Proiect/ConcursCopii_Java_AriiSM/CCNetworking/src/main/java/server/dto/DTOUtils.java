package server.dto;

import client.Domain_Simplu.*;

public class DTOUtils {

    public static Concurs getFromDTO(ConcursDTO usdto) {
        Proba proba = usdto.getProga();
        Categorie categorie = usdto.getCategorie();
        Integer id = usdto.getId();
        Concurs c = new Concurs(categorie, proba);
        c.setId(id);
        return c;
    }

    public static ConcursDTO getDTO(Concurs concurs) {
        Proba proba = concurs.getProba();
        Categorie categorie = concurs.getCategorie();
        Integer id = concurs.getId();
        return new ConcursDTO(id, proba, categorie);
    }

    public static Participant getFromDTO(ParticipantDTO usdto) {
        String lastName = usdto.getLastName();
        String firstName = usdto.getFirstName();
        Integer age = usdto.getAge();
        Integer id = usdto.getId();
        Participant p = new Participant(lastName, firstName, age);
        p.setId(id);
        return p;
    }

    public static ParticipantDTO getDTO(Participant participant) {
        String lastName = participant.getLastName();
        String firstName = participant.getFirstName();
        Integer age = participant.getAge();
        Integer id = participant.getId();
        return new ParticipantDTO(id, lastName, firstName, age);
    }

    public static Organizator getFromDTO(OrganizatorDTO usdto) {
        String lastName = usdto.getLastName();
        String firstName = usdto.getFirstName();
        String parola = usdto.getParola();
        return new Organizator(lastName, firstName, parola);
    }

    public static OrganizatorDTO getDTO(Organizator organizator) {
        String lastName = organizator.getLastName();
        String firstName = organizator.getFirstName();
        String parola = organizator.getParola();
        return new OrganizatorDTO(lastName, firstName, parola);
    }

    public static InscriereDTO getDTO(Inscriere inscriere) {
        Participant participant = inscriere.getParticipant();
        Concurs concurs = inscriere.getConcurs();
        Integer id = inscriere.getId();
        return new InscriereDTO(id, participant, concurs);
    }

    public static Inscriere getFromDTO(InscriereDTO usdto) {
        Participant participant = usdto.getParticipant();
        Concurs concurs = usdto.getConcurs();
        Integer id = usdto.getId();
        Inscriere i = new Inscriere(participant, concurs);
        i.setId(id);
        return i;
    }
}
