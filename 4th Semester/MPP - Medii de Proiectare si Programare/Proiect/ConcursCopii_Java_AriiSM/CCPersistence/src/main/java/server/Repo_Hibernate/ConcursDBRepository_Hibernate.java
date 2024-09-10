package server.Repo_Hibernate;

import client.Domain_Hibernate.Concurs;
import client.Domain_Hibernate.Inscriere;
import org.hibernate.Session;
import org.hibernate.Transaction;
import server.Repo_Hibernate.Interface.IConcursRepositoryHibernate;

import java.util.List;
import java.util.Optional;

public class ConcursDBRepository_Hibernate implements IConcursRepositoryHibernate {
    @Override
    public Optional<Concurs> findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery("from Concurs where id= ?1", Concurs.class).setParameter(1, id).getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Concurs> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Concurs> concurs = session.createQuery("from Concurs ", Concurs.class).getResultList();
            return concurs;
        }
    }

    @Override
    public Optional<Concurs> save(Concurs entity) {
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
    public Optional<Concurs> findConcursProbaCategorie(String proba, String categorie) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {{
            return Optional.of(session.createSelectionQuery("from Concurs where proba= ?1 and categorie= ?2", Concurs.class)
                    .setParameter(1, client.Domain_Hibernate.Proba.valueOf(proba))
                    .setParameter(2, client.Domain_Hibernate.Categorie.valueOf(categorie))
                    .getSingleResultOrNull());
        }}
    }
}
