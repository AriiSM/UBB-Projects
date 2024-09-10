package server.Repo_Hibernate.Interface;

import server.ICrudRepository;

import java.util.Optional;

public interface IConcursRepositoryHibernate extends ICrudRepository<Integer, client.Domain_Hibernate.Concurs> {
    Optional<client.Domain_Hibernate.Concurs> findConcursProbaCategorie(String proba, String categorie);
}
