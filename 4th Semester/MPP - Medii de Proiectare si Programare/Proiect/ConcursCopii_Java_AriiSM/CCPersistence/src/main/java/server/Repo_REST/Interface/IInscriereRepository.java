package server.Repo_REST.Interface;

import client.Domain_REST.Inscriere;
import server.ICrudRepository;

import java.util.Optional;


public interface IInscriereRepository extends ICrudRepository<Integer, Inscriere> {
    Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs);
}
