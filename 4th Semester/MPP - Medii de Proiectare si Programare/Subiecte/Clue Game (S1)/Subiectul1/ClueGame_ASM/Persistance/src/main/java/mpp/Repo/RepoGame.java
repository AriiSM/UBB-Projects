package mpp.Repo;

import mpp.Configuratie;
import mpp.Exceptions.GenericException;
import mpp.Game;
import mpp.Interface.IRepoConfiguratie;
import mpp.Interface.IRepoGame;
import mpp.Interface.IRepoPlayer;
import mpp.Persoana;
import mpp.Utils.UtilsJDBC;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class RepoGame implements IRepoGame {

    private UtilsJDBC utils;

    private IRepoPlayer repoPersoana;
    private IRepoConfiguratie repoConfiguratie;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RepoGame(@Qualifier("props") Properties props, IRepoPlayer repoPersoana, IRepoConfiguratie repoConfiguratie) {
        utils = new UtilsJDBC(props);
        this.repoPersoana = repoPersoana;
        this.repoConfiguratie = repoConfiguratie;
    }

    @Override
    public Optional<Game> findOne(Integer id) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        Game game = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Game WHERE id = ?")) {
            stms.setInt(1, id);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    int idG = resultSet.getInt("id");
                    int idC = resultSet.getInt("idConfiguratie");
                    int incercari = resultSet.getInt("incercari");
                    String cuvant = resultSet.getString("cuvant");
                    String oraStart = resultSet.getString("oraStart");
                    int score = resultSet.getInt("scor");

                    Configuratie configuratie = repoConfiguratie.findOne(idC).get();

                    game = new Game(idG, configuratie, oraStart, incercari, cuvant,score);

                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(game);
        return Optional.ofNullable(game);
    }

    @Override
    public Iterable<Game> findAll() {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        List<Game> games = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Game")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idC = resultSet.getInt("idConfiguratie");
                    int incercari = resultSet.getInt("incercari");
                    String cuvant = resultSet.getString("cuvant");
                    String oraStart = resultSet.getString("oraStart");
                    int score = resultSet.getInt("scor");

                    Configuratie configuratie = repoConfiguratie.findOne(idC).get();

                    Game game = new Game(id, configuratie, oraStart, incercari, cuvant,score);
                    games.add(game);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB" + e);
        }
        logger.traceExit(games);
        return games;
    }

    @Override
    public Optional<Game> save(Game entity) throws GenericException {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Game(idConfiguratie, oraStart, incercari, cuvant,scor) VALUES (?,?,?,?,?) RETURNING id")) {
            stmt.setInt(1, entity.getConfiguratie().getId());
            stmt.setString(2,entity.getOraStart());
            stmt.setInt(3, entity.getNrIncercari());
            stmt.setString(4, entity.getCuvant());
            stmt.setInt(5, entity.getScore());

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
    public Optional<Game> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> update(Game entity) throws GenericException {
        return Optional.empty();
    }
}
