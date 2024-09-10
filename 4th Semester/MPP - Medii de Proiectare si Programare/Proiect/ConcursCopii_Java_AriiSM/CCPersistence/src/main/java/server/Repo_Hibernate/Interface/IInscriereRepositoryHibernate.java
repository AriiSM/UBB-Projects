package server.Repo_Hibernate.Interface;

import client.Domain_Hibernate.Inscriere;
import server.ICrudRepository;

import java.util.Optional;

public interface IInscriereRepositoryHibernate extends ICrudRepository<Integer, Inscriere> {
    Optional<client.Domain_Hibernate.Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs);
}
