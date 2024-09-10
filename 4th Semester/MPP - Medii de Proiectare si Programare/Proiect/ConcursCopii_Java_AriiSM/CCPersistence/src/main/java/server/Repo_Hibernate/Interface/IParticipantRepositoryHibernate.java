package server.Repo_Hibernate.Interface;

import client.Domain_Hibernate.Participant;
import server.ICrudRepository;

import java.util.List;
import java.util.Optional;

public interface IParticipantRepositoryHibernate extends ICrudRepository<Integer, client.Domain_Hibernate.Participant> {
    List<client.Domain_Hibernate.Participant> filterProbaCategorie(String proba, String categorie);
    Optional<client.Domain_Hibernate.Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta);
    Integer numarProbePentruParticipant(Integer id);
}
