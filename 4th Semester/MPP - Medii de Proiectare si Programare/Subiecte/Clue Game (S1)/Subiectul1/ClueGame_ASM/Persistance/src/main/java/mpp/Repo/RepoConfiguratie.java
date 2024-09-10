package mpp.Repo;

import mpp.Configuratie;
import mpp.Exceptions.GenericException;
import mpp.Game;
import mpp.Interface.IRepoConfiguratie;
import mpp.Utils.UtilsJDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@Component
public class RepoConfiguratie implements IRepoConfiguratie {

    private UtilsJDBC utils;
    private static final Logger logger = LogManager.getLogger();
    @Autowired
    public RepoConfiguratie(@Qualifier("props") Properties props) {
        utils = new UtilsJDBC(props);
    }

    @Override
    public Optional<Configuratie> findOne(Integer id) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        Configuratie configuratie = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Configuratie WHERE id = ?")) {
            stms.setInt(1, id);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    Integer idp = resultSet.getInt("id");
                    String conf = resultSet.getString("conf");

                    configuratie = new Configuratie(idp, conf);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(configuratie);
        return Optional.ofNullable(configuratie);
    }

    @Override
    public Iterable<Configuratie> findAll() {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        List<Configuratie> configurares = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Configuratie")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String conf = resultSet.getString("conf");


                    Configuratie configurare = new Configuratie(id, conf);
                    configurares.add(configurare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(configurares);
        return configurares;
    }

    @Override
    public Optional<Configuratie> save(Configuratie entity) throws GenericException {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Configuratie(conf) VALUES (?) RETURNING id")) {
            stmt.setString(1, entity.getConfiguratie());
            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating Participant failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
        logger.traceExit(entity);
        return Optional.ofNullable(entity);
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
