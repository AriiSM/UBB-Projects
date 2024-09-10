package server.Repo_REST.Interface;

import client.Domain_REST.Participant;
import server.ICrudRepository;

import java.util.List;
import java.util.Optional;

public interface IParticipantRepository extends ICrudRepository<Integer, Participant> {
    List<Participant> filterProbaCategorie(String proba, String categorie);
    Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta);
    Integer numarProbePentruParticipant(Integer id);
}
