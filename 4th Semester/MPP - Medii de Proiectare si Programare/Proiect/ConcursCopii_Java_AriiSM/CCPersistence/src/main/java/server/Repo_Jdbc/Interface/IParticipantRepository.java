package server.Repo_Jdbc.Interface;

import client.Domain_Simplu.Participant;
import server.ICrudRepository;

import java.util.List;
import java.util.Optional;

public interface IParticipantRepository extends ICrudRepository<Integer, Participant> {
    List<Participant> filterProbaCategorie(String proba, String categorie);
    Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta);
    Integer numarProbePentruParticipant(Integer id);
}
