package mpp.Utils;

import mpp.Configuratie;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class UtilsORM {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            try {
                sessionFactory = createNewSessionFactory();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Error initializing Hibernate session factory", ex);
            }
        }
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Configuratie.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
