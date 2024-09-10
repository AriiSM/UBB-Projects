package mpp.Repo;

import mpp.Configurare;
import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoConfigurare;
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
public class RepoConfigurare implements IRepoConfigurare {
    private UtilsJDBC utils;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RepoConfigurare(@Qualifier("props")Properties props) {
        utils = new UtilsJDBC(props);
    }

    @Override
    public Optional<Configurare> findOne(Integer id) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        Configurare configuratie = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Configurare WHERE id = ?")) {
            stms.setInt(1, id);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    Integer idp = resultSet.getInt("id");
                    String conf = resultSet.getString("conf");

                    configuratie = new Configurare(idp, conf);
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
    public Iterable<Configurare> findAll() {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        List<Configurare> configurares = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Configurare")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Integer idp = resultSet.getInt("id");
                    String conf = resultSet.getString("conf");

                    Configurare configurare = new Configurare(idp, conf);
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
    public Optional<Configurare> save(Configurare entity) throws GenericException {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Configurare(conf) VALUES (?) RETURNING id")) {
            stmt.setString(1, entity.getCoef());


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
    public Optional<Configurare> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Configurare> update(Configurare entity) throws GenericException {
        return Optional.empty();
    }

    @Override
    public void updateWithId(Integer id, String attribute, String newValue) {
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE Configurare SET " + attribute + " = ? WHERE id = ?")) {
            stmt.setString(1, newValue);
            stmt.setInt(2, id);

            int result = stmt.executeUpdate();
            if (result == 0) {
                throw new Exception("No Configurare found for update!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
