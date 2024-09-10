package mpp.Repo;

import mpp.Exceptions.GenericException;
import mpp.Game;
import mpp.Interface.IRepoJucatorGame;
import mpp.JucatorGame;
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
import java.util.*;

@Component
public class RepoJucatorGame implements IRepoJucatorGame {
    private UtilsJDBC utils;
    private RepoPlayer repoPlayer;
    private RepoGame repoGame;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RepoJucatorGame(@Qualifier("props") Properties props, RepoPlayer repoPlayer, RepoGame repoGame) {
        this.repoGame = repoGame;
        this.repoPlayer = repoPlayer;
        utils = new UtilsJDBC(props);
    }

    @Override
    public Optional<JucatorGame> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<JucatorGame> findAll() {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        List<JucatorGame> jucatoriGamesList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM JucatorGames")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idP = resultSet.getInt("idPersoana");
                    int idg = resultSet.getInt("idGame");

                    Optional<Persoana> persoanaOptional = repoPlayer.findOne(idP);
                    Optional<Game> gameOptional = repoGame.findOne(idg);

                    Persoana persoana = persoanaOptional.get();
                    Game game = gameOptional.get();

                    JucatorGame jucatoriGames = new JucatorGame(id, persoana, game);
                    jucatoriGamesList.add(jucatoriGames);

                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(jucatoriGamesList);
        return jucatoriGamesList;
    }

    @Override
    public Optional<JucatorGame> save(JucatorGame entity) throws GenericException {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO JucatorGames(idPersoana, idGame) VALUES (?, ?) RETURNING id")) {
            stmt.setInt(1, entity.getPersoana().getId());
            stmt.setInt(2, entity.getGame().getId());

            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    entity.setId(id);
                    logger.traceExit(entity);
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
    public Optional<JucatorGame> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<JucatorGame> update(JucatorGame entity) throws GenericException {
        return Optional.empty();
    }

    @Override
    public String findCerinta(Integer id) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        List<JucatorGame> jucatoriGamesList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM JucatorGames WHERE idPersoana = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int idJG = resultSet.getInt("id");
                    int idP = resultSet.getInt("idPersoana");
                    int idg = resultSet.getInt("idGame");

                    Optional<Persoana> persoanaOptional = repoPlayer.findOne(idP);
                    Optional<Game> gameOptional = repoGame.findOne(idg);

                    Persoana persoana = persoanaOptional.get();
                    Game game = gameOptional.get();

                    JucatorGame jucatoriGames = new JucatorGame(idJG, persoana, game);
                    jucatoriGamesList.add(jucatoriGames);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }


        List<String> jucatoriGamesString = new ArrayList<>();

        for (JucatorGame jg : jucatoriGamesList) {

            jucatoriGamesString.add("User: " + jg.getPersoana().getUsername() + " Score: " + jg.getGame().getScore().toString() + " LitereGhicite: " + jg.getGame().getLitereGhicite());
        }

        logger.traceExit(jucatoriGamesString);
        return jucatoriGamesString.toString();
    }
}


