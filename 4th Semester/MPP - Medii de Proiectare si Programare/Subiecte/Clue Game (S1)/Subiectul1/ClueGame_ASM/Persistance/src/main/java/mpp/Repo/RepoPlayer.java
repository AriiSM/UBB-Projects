package mpp.Repo;

import mpp.Exceptions.GenericException;
import mpp.Interface.IRepoPlayer;
import mpp.Persoana;
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
import java.util.Optional;
import java.util.Properties;

@Component
public class RepoPlayer implements IRepoPlayer {

    private UtilsJDBC utils;

    @Autowired
    public RepoPlayer(@Qualifier("props") Properties props) {
        utils = new UtilsJDBC(props);
    }

    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<Persoana> login(String username) {
        logger.traceEntry();
        Connection con = utils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Persoana WHERE username=?")) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = Integer.valueOf(resultSet.getInt("id"));
                    String username2 = String.valueOf(resultSet.getString("username"));

                    Persoana player = new Persoana(id, username2);
                    logger.traceExit(player);
                    return Optional.of(player);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> findJucator(String username) {
        logger.traceEntry();
        Connection con = utils.getConnection();
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Persoana WHERE username=?")) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = Integer.valueOf(resultSet.getInt("id"));
                    String username2 = String.valueOf(resultSet.getString("username"));

                    Persoana player = new Persoana(id, username2);
                    logger.traceExit(player);
                    return Optional.of(player);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> findOne(Integer id) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        Persoana jucator = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Persoana WHERE id = ?")) {
            stms.setInt(1, id);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    Integer idp = resultSet.getInt("id");
                    String nume = resultSet.getString("username");

                    jucator = new Persoana(idp, nume);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
        return Optional.ofNullable(jucator);
    }

    @Override
    public Iterable<Persoana> findAll() {
        return null;
    }

    @Override
    public Optional<Persoana> save(Persoana entity) throws GenericException {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> update(Persoana entity) throws GenericException {
        return Optional.empty();
    }
}
