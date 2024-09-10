package mpp.RepoORM;

import mpp.Configurare;
import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoConfigurare;
import mpp.Utils.UtilsJDBC;
import mpp.Utils.UtilsORM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
public class RepoConfigurareHibernate implements IRepoConfigurare {

    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Configurare> findOne(Integer id) {
        logger.traceEntry();
        try (Session session = UtilsORM.getSessionFactory().openSession()) {
            logger.traceExit();
            return Optional.of(session.createSelectionQuery("from Configurare where id= ?1", Configurare.class).setParameter(1, id).getSingleResultOrNull());
        }
    }

    @Override
    public Iterable<Configurare> findAll() {
        logger.traceEntry();
        try (Session session = UtilsORM.getSessionFactory().openSession()) {
            List<Configurare> concurs = session.createQuery("from Configurare ", Configurare.class).getResultList();
            logger.traceExit();
            return concurs;
        }
    }

    @Override
    public Optional<Configurare> save(Configurare entity) throws GenericException {
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
    public Optional<Configurare> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Configurare> update(Configurare entity) throws GenericException {
        return Optional.empty();
    }

    @Override
    public Optional<Configurare> findConfigurare() {
        logger.traceEntry();
        try (Session session = UtilsORM.getSessionFactory().openSession()) {
            logger.traceExit();
            return Optional.of(session.createSelectionQuery("from Configurare", Configurare.class).getSingleResultOrNull());
        }
    }
}
