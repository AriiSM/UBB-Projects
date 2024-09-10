package server.Repo_Jdbc.Interface;

import client.Domain_Simplu.Inscriere;
import server.ICrudRepository;

import java.util.Optional;


public interface IInscriereRepository extends ICrudRepository<Integer, Inscriere> {
    Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs);
}
