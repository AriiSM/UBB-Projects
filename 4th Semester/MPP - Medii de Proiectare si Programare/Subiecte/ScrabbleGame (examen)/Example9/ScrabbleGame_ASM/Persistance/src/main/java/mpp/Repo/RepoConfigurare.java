//package mpp.Repo;
//
//import mpp.Configurare;
//import mpp.Exceptions.GenericException;
//import mpp.Interface.IRepoConfigurare;
//import mpp.Utils.UtilsJDBC;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.Properties;
//
//public class RepoConfigurare implements IRepoConfigurare {
//    private UtilsJDBC utils;
//
//    public RepoConfigurare(Properties props) {
//        utils = new UtilsJDBC(props);
//    }
//
//    private static final Logger logger = LogManager.getLogger();
//
//    @Override
//    public Optional<Configurare> findOne(Integer id) {
//        logger.traceEntry();
//        Connection conn = utils.getConnection();
//        Configurare configuratie = null;
//        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Configurare WHERE id = ?")) {
//            stms.setInt(1, id);
//            try (ResultSet resultSet = stms.executeQuery()) {
//                if (resultSet.next()) {
//                    Integer idp = resultSet.getInt("id");
//                    String cuv = resultSet.getString("cuvant");
//                    String cuvantAmestecat = resultSet.getString("litere");
//
//
//                    configuratie = new Configurare(idp, cuv, cuvantAmestecat);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error(e);
//            System.err.println("Error DB" + e);
//        }
//        logger.traceExit(configuratie);
//        return Optional.ofNullable(configuratie);
//    }
//
//    @Override
//    public Iterable<Configurare> findAll() {
//        logger.traceEntry();
//        Connection conn = utils.getConnection();
//        List<Configurare> configurares = new ArrayList<>();
//        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Configurare")) {
//            try (ResultSet resultSet = stmt.executeQuery()) {
//                while (resultSet.next()) {
//                    Integer idp = resultSet.getInt("id");
//                    String cuv = resultSet.getString("cuvant");
//                    String cuvantAmestecat = resultSet.getString("litere");
//
//
//                    Configurare configuratie = new Configurare(idp, cuv, cuvantAmestecat);
//
//                    configurares.add(configuratie);
//                }
//            }
//        } catch (SQLException e) {
//            logger.error(e);
//            System.err.println("Error DB" + e);
//        }
//        logger.traceExit(configurares);
//        return configurares;
//    }
//
//    @Override
//    public Optional<Configurare> save(Configurare entity) throws GenericException {
//        logger.traceEntry();
//        Connection conn = utils.getConnection();
//        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Configurare(cuvant, litere) VALUES (?,?) RETURNING id")) {
//            stmt.setString(1, entity.getCuvant());
//            stmt.setString(2, entity.getLitere());
//
//            try (ResultSet generatedKeys = stmt.executeQuery()) {
//                if (generatedKeys.next()) {
//                    entity.setId(generatedKeys.getInt(1));
//                } else {
//                    throw new SQLException("Creating Participant failed, no ID obtained.");
//                }
//            }
//        } catch (SQLException e) {
//            logger.error(e);
//            throw new RuntimeException(e);
//        }
//        logger.traceExit(entity);
//        return Optional.ofNullable(entity);
//    }
//
//    @Override
//    public Optional<Configurare> delete(Integer integer) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<Configurare> update(Configurare entity) throws GenericException {
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<Configurare> findConfigurare() {
//        return Optional.empty();
//    }
//}
