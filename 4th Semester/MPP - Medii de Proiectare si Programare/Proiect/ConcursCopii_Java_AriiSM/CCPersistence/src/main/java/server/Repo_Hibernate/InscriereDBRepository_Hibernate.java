package server.Repo_Hibernate;

import client.Domain_Hibernate.Inscriere;
import client.Domain_Hibernate.Organizator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import server.Repo_Hibernate.Interface.IInscriereRepositoryHibernate;

import java.util.List;
import java.util.Optional;

public class InscriereDBRepository_Hibernate implements IInscriereRepositoryHibernate {
    @Override
    public Optional<Inscriere> findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery
                            ("from Inscriere where id= ?1", Inscriere.class)
                    .setParameter(1, id)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Inscriere> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Inscriere> inscrieres = session.createQuery("from Inscriere ", Inscriere.class).getResultList();
            return inscrieres;
        }
    }

    @Override
    public Optional<Inscriere> save(Inscriere entity) {
        //Utils_Hibernate.getSessionFactory().inTransaction(session -> session.persist(entity));
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Reattach the entity to the session and save it to the database
        entity = session.merge(entity);
        tx.commit();
        session.close();

        return Optional.of(entity);
    }

    @Override
    public Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery("from Inscriere where Participant .id= ?1 and Concurs.id= ?2", Inscriere.class)
                    .setParameter(1, id_participant)
                    .setParameter(2, id_concurs)
                    .getSingleResultOrNull());
        }
    }
}
