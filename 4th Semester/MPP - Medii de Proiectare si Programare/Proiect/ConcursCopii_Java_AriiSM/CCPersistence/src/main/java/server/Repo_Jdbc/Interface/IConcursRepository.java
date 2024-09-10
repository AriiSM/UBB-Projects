package server.Repo_Jdbc.Interface;


import client.Domain_Simplu.Concurs;
import server.ICrudRepository;

import java.util.Optional;

public interface IConcursRepository extends ICrudRepository<Integer, Concurs> {
    Optional<Concurs> findConcursProbaCategorie(String proba, String categorie);
}
