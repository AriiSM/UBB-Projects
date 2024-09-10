package mpp.RepoORM;

import mpp.Configuratie;
import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoConfiguratie;
import mpp.Utils.UtilsJDBC;
import mpp.Utils.UtilsORM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Primary
@Component
public class RepoConfiguratieHibernate implements IRepoConfiguratie {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Configuratie> findOne(Integer id) {
        logger.traceEntry();

        try (Session session = UtilsORM.getSessionFactory().openSession()) {
            logger.traceExit();
            return Optional.of(session.createSelectionQuery("from Configuratie where id= ?1", Configuratie.class).setParameter(1, id).getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Configuratie> findAll() {
        logger.traceEntry();
        try (Session session = UtilsORM.getSessionFactory().openSession()) {
            List<Configuratie> concurs = session.createQuery("from Configuratie ", Configuratie.class).getResultList();
            logger.traceExit();
            return concurs;
        }
    }

    @Override
    public Optional<Configuratie> save(Configuratie entity) throws GenericException {
        logger.traceEntry();
        Session session = UtilsORM.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        entity = session.merge(entity);
        tx.commit();
        session.close();
        logger.traceExit();
        return Optional.of(entity);
    }

    @Override
    public Optional<Configuratie> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Configuratie> update(Configuratie entity) throws GenericException {
        return Optional.empty();
    }
}
