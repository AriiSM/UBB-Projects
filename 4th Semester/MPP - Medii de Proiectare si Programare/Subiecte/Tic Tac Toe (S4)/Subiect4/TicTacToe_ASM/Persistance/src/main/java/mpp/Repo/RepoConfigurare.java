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
                    String conf00 = resultSet.getString("coef00");
                    String conf01 = resultSet.getString("coef01");
                    String conf02 = resultSet.getString("coef02");
                    String conf10 = resultSet.getString("coef10");
                    String conf11 = resultSet.getString("coef11");
                    String conf12 = resultSet.getString("coef12");
                    String conf20 = resultSet.getString("coef20");
                    String conf21 = resultSet.getString("coef21");
                    String conf22 = resultSet.getString("coef22");

                    configuratie = new Configurare(idp, conf00, conf01, conf02, conf10, conf11, conf12, conf20, conf21, conf22);
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
                    int id = resultSet.getInt("id");
                    Integer idp = resultSet.getInt("id");
                    String conf00 = resultSet.getString("coef00");
                    String conf01 = resultSet.getString("coef01");
                    String conf02 = resultSet.getString("coef02");
                    String conf10 = resultSet.getString("coef10");
                    String conf11 = resultSet.getString("coef11");
                    String conf12 = resultSet.getString("coef12");
                    String conf20 = resultSet.getString("coef20");
                    String conf21 = resultSet.getString("coef21");
                    String conf22 = resultSet.getString("coef22");

                    Configurare configurare = new Configurare(id, conf00, conf01, conf02, conf10, conf11, conf12, conf20, conf21, conf22);
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
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Configurare(coef00, coef01, coef02, coef10, coef11, coef12, coef20, coef21, coef22) VALUES (?,?,?,?,?,?,?,?,?) RETURNING id")) {
            stmt.setString(1, entity.getCoef00());
            stmt.setString(2, entity.getCoef01());
            stmt.setString(3, entity.getCoef02());
            stmt.setString(4, entity.getCoef10());
            stmt.setString(5, entity.getCoef11());
            stmt.setString(6, entity.getCoef12());
            stmt.setString(7, entity.getCoef20());
            stmt.setString(8, entity.getCoef21());
            stmt.setString(9, entity.getCoef22());

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
