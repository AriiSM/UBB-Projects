package server.Repo_Hibernate;

import client.Domain_Hibernate.Concurs;
import client.Domain_Hibernate.Inscriere;
import client.Domain_Hibernate.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import server.Repo_Hibernate.Interface.IParticipantRepositoryHibernate;
import java.util.List;
import java.util.Optional;

public class ParticipantDBRepository_Hibernate implements IParticipantRepositoryHibernate {


    @Override
    public Optional<Participant> findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.of(session.createSelectionQuery
                            ("from Participant where id= ?1", Participant.class)
                    .setParameter(1, id)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Participant> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Participant> participants = session.createQuery("from Participant ", Participant.class).getResultList();
            return participants;
        }
    }

    @Override
    public Optional<Participant> save(Participant entity) {
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
    public List<Participant> filterProbaCategorie(String proba, String categorie) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("select distinct p from Participant p join Inscriere i on p.id=i.participant.id join Concurs c on i.concurs.id=c.id where c.proba= :proba and c.categorie= :categorie", Participant.class)
                    .setParameter("proba", client.Domain_Hibernate.Proba.valueOf(proba))
                    .setParameter("categorie", client.Domain_Hibernate.Categorie.valueOf(categorie))
                    .getResultList();
        }
    }

    @Override
    public Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.createSelectionQuery("from Participant where lastName= ?1 and firstName= ?2 and age= ?3", Participant.class)
                    .setParameter(1, nume)
                    .setParameter(2, prenume)
                    .setParameter(3, varsta)
                    .getSingleResultOrNull());
        }
    }

    @Override
    public Integer numarProbePentruParticipant(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Long result = (Long) session.createQuery("select count(*) from Inscriere i where i.participant.id= :id")
                    .setParameter("id", id)
                    .uniqueResult();
            return result != null ? result.intValue() : null;
        }
    }
}
