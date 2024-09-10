package mpp.RepoOrm;

import mpp.Configurare;
import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoConfigurare;
import mpp.Utils.UtilsJDBC;
import mpp.Utils.UtilsORM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

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
        Session session = UtilsORM.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        entity = session.merge(entity);
        tx.commit();
        session.close();

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
    public void updateWithId(Integer id, String attribute, String newValue) {
        try (Session session = UtilsORM.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Configurare configurare = session.find(Configurare.class, id);
            switch (attribute) {
                case "coef00":
                    configurare.setCoef00(newValue);
                    break;
                case "coef01":
                    configurare.setCoef01(newValue);
                    break;
                case "coef02":
                    configurare.setCoef02(newValue);
                    break;
                case "coef10":
                    configurare.setCoef10(newValue);
                    break;
                case "coef11":
                    configurare.setCoef11(newValue);
                    break;
                case "coef12":
                    configurare.setCoef12(newValue);
                    break;
                case "coef20":
                    configurare.setCoef20(newValue);
                    break;
                case "coef21":
                    configurare.setCoef21(newValue);
                    break;
                case "coef22":
                    configurare.setCoef22(newValue);
                    break;
            }
            session.update(configurare);
            tx.commit();
        }
    }

}
