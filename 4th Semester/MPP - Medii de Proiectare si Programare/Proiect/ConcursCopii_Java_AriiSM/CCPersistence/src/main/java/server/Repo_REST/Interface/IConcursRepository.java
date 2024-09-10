package server.Repo_REST.Interface;


import client.Domain_REST.Concurs;
import server.ICrudRepository;

import java.util.Optional;

public interface IConcursRepository extends ICrudRepository<Integer, Concurs> {
    Optional<Concurs> findConcursProbaCategorie(String proba, String categorie);
    void delete(Integer id);
    void update(Integer id, Concurs entity);
}
