package mpp.Repo;

import mpp.Exceptions.GenericException;
import mpp.Game;
import mpp.Interface.IRepoGame;
import mpp.Interface.IRepoJucatorGame;
import mpp.Interface.IRepoPlayer;
import mpp.Jucator;
import mpp.JucatoriGames;
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
public class RepoJucatorGame implements IRepoJucatorGame {
    private UtilsJDBC utils;
    private IRepoGame repoGame;
    private IRepoPlayer repoPlayer;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RepoJucatorGame(@Qualifier("props")Properties props, IRepoGame repoGame, IRepoPlayer repoPlayer) {
        utils = new UtilsJDBC(props);
        this.repoGame = repoGame;
        this.repoPlayer = repoPlayer;
    }

    @Override
    public Optional<JucatoriGames> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<JucatoriGames> findAll() {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        List<JucatoriGames> jucatoriGamesList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM JucatorGame")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idj = resultSet.getInt("idJucator");
                    int idg = resultSet.getInt("idGame");

                    Jucator jucator = repoPlayer.findOne(idj).get();
                    Game game = repoGame.findOne(idg).get();


                    JucatoriGames jucatoriGames = new JucatoriGames(id, game, jucator);
                    jucatoriGamesList.add(jucatoriGames);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
        return jucatoriGamesList;
    }

    @Override
    public Optional<JucatoriGames> save(JucatoriGames entity) throws GenericException {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO JucatorGame(idJucator, idGame) VALUES (?, ?) RETURNING id")) {
            stmt.setInt(1, entity.getJucator().getId());
            stmt.setInt(2, entity.getGame().getId());

            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    entity.setId(id);
                    return Optional.of(entity);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            throw new GenericException(e);
        }
        logger.traceExit();
        return Optional.empty();
    }

    @Override
    public Optional<JucatoriGames> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<JucatoriGames> update(JucatoriGames entity) throws GenericException {
        return Optional.empty();
    }

    @Override
    public String findCerinta(Integer id) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT J.*, G.*, C.* FROM Jucator J JOIN JucatorGame JG ON J.id =JG.idJucator JOIN Game G ON JG.idGame = G.id JOIN Configurare C ON G.id = C.id WHERE J.id = ?;")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    String alias = resultSet.getString("J.username");
                    String configurare = resultSet.getString("C.conf");
                    Integer scor = resultSet.getInt("G.scor");

                    String cerinta = alias + " " + configurare + " " + scor;
                    logger.traceExit();
                    return cerinta;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit();
        return null;
    }
}
