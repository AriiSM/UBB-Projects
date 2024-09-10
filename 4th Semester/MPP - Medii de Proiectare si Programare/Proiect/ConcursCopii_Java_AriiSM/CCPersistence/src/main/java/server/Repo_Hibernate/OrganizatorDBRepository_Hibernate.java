package server.Repo_Hibernate;

import client.Domain_Hibernate.Organizator;
import client.Domain_Hibernate.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import server.AESUtil;
import server.Repo_Hibernate.Interface.IOrganizatorRepositoryHibernate;

import java.util.List;
import java.util.Optional;

public class OrganizatorDBRepository_Hibernate implements IOrganizatorRepositoryHibernate {
    @Override
    public Optional<Organizator> findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery
                            ("from Organizator where id=?1", Organizator.class)
                    .setParameter(1, id)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Organizator> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Organizator> organizators = session.createQuery("from Organizator ", Organizator.class).getResultList();
            return organizators;
        }
    }

    @Override
    public Optional<Organizator> save(Organizator entity) {
        //Utils_Hibernate.getSessionFactory().inTransaction(session -> session.persist(entity));
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        // Reattach the entity to the session and save it to the database
        try {
            String parolaCriptata = AESUtil.encrypt(entity.getParola());
            entity.setParola(parolaCriptata);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entity = session.merge(entity);
        tx.commit();
        session.close();

        return Optional.of(entity);
    }

    @Override
    public Organizator findAccount(String parola, String nume, String prenume) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String parolaCriptata = AESUtil.encrypt(parola);
            Organizator organizator = session.createSelectionQuery("from Organizator where parola= ?1 and lastName= ?2 and firstName= ?3", Organizator.class)
                    .setParameter(1, parolaCriptata)
                    .setParameter(2, nume)
                    .setParameter(3, prenume)
                    .getSingleResultOrNull();
            if (organizator == null) {
                throw new RuntimeException("No Organizator found with the provided credentials");
            }
            organizator.setParola(parola);
            return organizator;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
