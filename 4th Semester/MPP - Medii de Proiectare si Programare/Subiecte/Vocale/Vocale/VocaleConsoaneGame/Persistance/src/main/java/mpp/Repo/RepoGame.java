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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class RepoGame implements IRepoGame {
    private UtilsJDBC utils;
    private IRepoConfigurare repoConfigurare;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RepoGame(@Qualifier("props")Properties props, IRepoConfigurare repoConfigurare) {
        this.repoConfigurare = repoConfigurare;
        utils = new UtilsJDBC(props);
    }

    @Override
    public Optional<Game> findOne(Integer id1) {
        logger.traceEntry();
        Connection conn = utils.getConnection();
        Game game = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Game WHERE id = ?")) {
            stms.setInt(1, id1);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idC = resultSet.getInt("idConfigurare");
                    int score = resultSet.getInt("score");
                    String oraStart = resultSet.getString("oraStart");
                    String litereGhicite = resultSet.getString("litereGhicite");
                    int noLitereGhicite = resultSet.getInt("noLitereGhicite");


                    Configurare configuratie = repoConfigurare.findOne(idC).get();

                    game = new Game(id, configuratie, score, oraStart, noLitereGhicite,litereGhicite);

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
                    int idC = resultSet.getInt("idConfigurare");
                    int score = resultSet.getInt("score");
                    String oraStart = resultSet.getString("oraStart");
                    String litereGhicite = resultSet.getString("litereGhicite");
                    int noLitereGhicite = resultSet.getInt("noLitereGhicite");


                    Configurare configuratie = repoConfigurare.findOne(idC).get();

                    Game game = new Game(id, configuratie, score, oraStart, noLitereGhicite,litereGhicite);

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
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Game(idConfigurare, score, oraStart, noLitereGhicite, litereGhicite) VALUES (?,?,?,?,?) RETURNING id")) {
            stmt.setInt(1, entity.getConfigurare().getId());
            stmt.setInt(2, entity.getScore());
            stmt.setString(3, entity.getOraStart());
            stmt.setInt(4, entity.getNoIncercari());
            stmt.setString(5, entity.getLitereGhicite());

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
