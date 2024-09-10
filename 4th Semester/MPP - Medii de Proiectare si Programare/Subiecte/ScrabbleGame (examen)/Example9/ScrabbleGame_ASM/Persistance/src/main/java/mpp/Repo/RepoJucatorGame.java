package mpp.Repo;

import mpp.Exceptions.GenericException;
import mpp.Game;
import mpp.Interface.IRepoGame;
import mpp.Interface.IRepoJucatorGame;
import mpp.Interface.IRepoPlayer;
import mpp.JucatoriGames;
import mpp.Persoana;
import mpp.Utils.UtilsJDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.NaturalId;
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
    private IRepoGame repoGame;
    private IRepoPlayer repoPlayer;

    @Autowired
    public RepoJucatorGame(@Qualifier("props") Properties props, IRepoGame repoGame, IRepoPlayer repoPlayer) {
        this.repoGame = repoGame;
        this.repoPlayer = repoPlayer;
        utils = new UtilsJDBC(props);
    }

    private static final Logger logger = LogManager.getLogger();

    @Override
    public Optional<JucatoriGames> findOne(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Iterable<JucatoriGames> findAll() {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        List<JucatoriGames> jucatoriGamesList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM JucatoriGames")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idP = resultSet.getInt("idPersoana");
                    int idg = resultSet.getInt("idGame");

                    Optional<Persoana> persoanaOptional = repoPlayer.findOne(idP);
                    Optional<Game> gameOptional = repoGame.findOne(idg);

                    Persoana persoana = persoanaOptional.get();
                    Game game = gameOptional.get();

                    JucatoriGames jucatoriGames = new JucatoriGames(id, game, persoana);
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
    public Optional<JucatoriGames> save(JucatoriGames entity) throws GenericException {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO JucatoriGames(idPersoana, idGame) VALUES (?, ?) RETURNING id")) {
            stmt.setInt(1, entity.getJucator().getId());
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
        List<JucatoriGames> jucatoriGamesList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM JucatoriGames WHERE idPersoana = ?")) {
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

                    JucatoriGames jucatoriGames = new JucatoriGames(idJG, game, persoana);
                    jucatoriGamesList.add(jucatoriGames);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }

        List<String> jucatoriGamesString = new ArrayList<>();

        for (JucatoriGames jg : jucatoriGamesList) {
            if (jg.getGame().getLitGhicite() >= 2) {
                jucatoriGamesString.add("Game: " + jg.getGame().getDataOra() + "  " + jg.getGame().getPuncte().toString() + "  " + jg.getGame().getLitGhicite().toString());
            }

        }

        logger.traceExit(jucatoriGamesString);
        return jucatoriGamesString.toString();
    }
}
