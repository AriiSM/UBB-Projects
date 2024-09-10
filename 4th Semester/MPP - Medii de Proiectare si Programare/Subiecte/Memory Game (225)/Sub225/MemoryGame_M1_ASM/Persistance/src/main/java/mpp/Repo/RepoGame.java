package mpp.Repo;

import mpp.Configurare;
import mpp.Exceptions.GenericException;
import mpp.Game;
import mpp.Interface.IRepoConfigurare;
import mpp.Interface.IRepoGame;
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
public class RepoGame implements IRepoGame {
    private UtilsJDBC utils;
    private IRepoConfigurare repoConfigurare;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RepoGame(@Qualifier("props") Properties props, IRepoConfigurare repoConfigurare) {
        this.repoConfigurare = repoConfigurare;
        utils = new UtilsJDBC(props);
    }


    @Override
    public Optional<Game> findOne(Integer idp) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        Game game = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Game WHERE id = ?")) {
            stms.setInt(1, idp);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    Integer puncte = resultSet.getInt("puncte");
                    Integer durata = resultSet.getInt("durata");
                    Integer idConfigurare = resultSet.getInt("idConfigurare");

                    Configurare configurare = repoConfigurare.findOne(idConfigurare).get();

                    game = new Game(id, puncte, durata, configurare);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
        return Optional.ofNullable(game);
    }

    @Override
    public Iterable<Game> findAll() {
        return null;
    }

    @Override
    public Optional<Game> save(Game entity) throws GenericException {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Game(puncte, durata, idConfigurare) VALUES (?, ?, ?) RETURNING id")) {
            stmt.setInt(1, entity.getPuncte());
            stmt.setInt(2, entity.getDurata());
            stmt.setInt(3, entity.getConfigurare().getId());

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
        logger.traceExit();
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Game> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> update(Game entity) throws GenericException {
        return Optional.empty();
    }

    @Override
    public String findCerinta(Integer id) {
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT J.*, G.*, C.* FROM Jucator J JOIN JucatorGame JG ON J.id =JG.idJucator JOIN Game G ON JG.idGame = G.id JOIN Configurare C ON G.id = C.id WHERE G.id = ?;")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String alias = resultSet.getString("username");
                    String configurare = resultSet.getString("conf");
                    Integer scor = resultSet.getInt("puncte");

                    String cerinta = alias + " " + configurare + " " + scor;
                    return cerinta;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return null;
    }
}
