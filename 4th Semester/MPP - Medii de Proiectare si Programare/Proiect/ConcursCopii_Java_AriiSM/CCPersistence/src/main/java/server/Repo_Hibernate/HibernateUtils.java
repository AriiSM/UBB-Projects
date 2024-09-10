package server.Repo_Hibernate;

import client.Domain_Hibernate.Concurs;
import client.Domain_Hibernate.Inscriere;
import client.Domain_Hibernate.Organizator;
import client.Domain_Hibernate.Participant;
import client.Domain_Simplu.Categorie;
import client.Domain_Simplu.Proba;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                sessionFactory = createNewSessionFactory();
            } catch (Exception ex) {
                // Log the exception or handle it appropriately
                ex.printStackTrace();
                throw new RuntimeException("Error initializing Hibernate session factory", ex);
            }
        }
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Concurs.class)
                .addAnnotatedClass(Inscriere.class)
                .addAnnotatedClass(Organizator.class)
                .addAnnotatedClass(Participant.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null)
            sessionFactory.close();
    }

    public static client.Domain_Simplu.Participant convertParticipant_FromHibernate(client.Domain_Hibernate.Participant participant) {
        client.Domain_Simplu.Participant part = new client.Domain_Simplu.Participant(
                participant.getLastName(),
                participant.getFirstName(),
                participant.getAge());
        part.setId(participant.getId());
        return part;
    }

    public static client.Domain_Hibernate.Participant convertParticipant_ToHibernate(client.Domain_Simplu.Participant participant) {
        client.Domain_Hibernate.Participant part = new client.Domain_Hibernate.Participant(
                participant.getLastName(),
                participant.getFirstName(),
                participant.getAge());
        part.setId(participant.getId());
        return part;
    }

    public static client.Domain_Simplu.Concurs convertConcurs_FromHibernate(client.Domain_Hibernate.Concurs concurs) {
        client.Domain_Simplu.Concurs c = new client.Domain_Simplu.Concurs(
                Categorie.valueOf(concurs.getCategorie().toString()),
                Proba.valueOf(concurs.getProba().toString()));

        c.setId(concurs.getId());
        return c;
    }

    public static client.Domain_Hibernate.Concurs convertConcurs_ToHibernate(client.Domain_Simplu.Concurs concurs) {
        client.Domain_Hibernate.Concurs c = new client.Domain_Hibernate.Concurs(
                client.Domain_Hibernate.Categorie.valueOf(concurs.getCategorie().toString()),
                client.Domain_Hibernate.Proba.valueOf(concurs.getProba().toString()));

        c.setId(concurs.getId());
        return c;
    }

    public static client.Domain_Simplu.Organizator convertOrganizator_FromHibernate(client.Domain_Hibernate.Organizator organizator) {
        client.Domain_Simplu.Organizator org = new client.Domain_Simplu.Organizator(
                organizator.getLastName(),
                organizator.getFirstName(),
                organizator.getParola());
        org.setId(organizator.getId());
        return org;
    }

    public static client.Domain_Hibernate.Organizator convertOrganizator_ToHibernate(client.Domain_Simplu.Organizator organizator) {
        client.Domain_Hibernate.Organizator org = new client.Domain_Hibernate.Organizator(
                organizator.getLastName(),
                organizator.getFirstName(),
                organizator.getParola());
        org.setId(organizator.getId());
        return org;
    }

    public static client.Domain_Simplu.Inscriere convertInscriere_FromHibernate(client.Domain_Hibernate.Inscriere inscriere) {
        client.Domain_Simplu.Inscriere i = new client.Domain_Simplu.Inscriere(
                convertParticipant_FromHibernate(inscriere.getParticipant()),
                convertConcurs_FromHibernate(inscriere.getConcurs()));
        i.setId(inscriere.getId());
        return i;
    }

    public static client.Domain_Hibernate.Inscriere convertInscriere_ToHibernate(client.Domain_Simplu.Inscriere inscriere) {
        client.Domain_Hibernate.Inscriere i = new client.Domain_Hibernate.Inscriere(
                convertParticipant_ToHibernate(inscriere.getParticipant()),
                convertConcurs_ToHibernate(inscriere.getConcurs()));
        i.setId(inscriere.getId());
        return i;
    }

}
