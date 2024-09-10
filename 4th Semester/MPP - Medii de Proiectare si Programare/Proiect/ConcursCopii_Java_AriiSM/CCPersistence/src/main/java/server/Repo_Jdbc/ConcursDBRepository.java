package server.Repo_Jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import client.Domain_Simplu.Categorie;
import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Proba;
import server.Repo_Jdbc.Interface.IConcursRepository;


public class ConcursDBRepository implements IConcursRepository{
    private final JdbcUtils dbUtils;
    //private final Logger logger = LogManager.getLogger(ConcursDBRepository.class);


    public ConcursDBRepository(Properties props) {
        //logger.info("Initializing ConcursDBRepository with proprties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Concurs> findOne(Integer id) {
        //logger.traceEntry("ConcursDBRepository findOne: {}", id);
        Connection conn = dbUtils.getConnection();
        Concurs concurs = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Concurs WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int idc = resultSet.getInt("id");
                    String categorieStr = resultSet.getString("categorie");
                    String probaStr = resultSet.getString("proba");

                    Categorie categorie = Categorie.valueOf(categorieStr);
                    Proba proba = Proba.valueOf(probaStr);

                    concurs = new Concurs(categorie, proba);
                    concurs.setId(idc);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(concurs);
        return Optional.ofNullable(concurs);
    }

    @Override
    public Iterable<Concurs> findAll() {
        //logger.traceEntry("ConcursDBRepository findAll!");
        Connection conn = dbUtils.getConnection();
        List<Concurs> concursList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Concurs")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int idc = resultSet.getInt("id");
                    String categorieStr = resultSet.getString("categorie");
                    String probaStr = resultSet.getString("proba");

                    Categorie categorie = Categorie.valueOf(categorieStr);
                    Proba proba = Proba.valueOf(probaStr);

                    Concurs concurs = new Concurs(categorie, proba);
                    concurs.setId(idc);

                    concursList.add(concurs);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(concursList);
        return concursList;
    }

    @Override
    public Optional<Concurs> save(Concurs entity) {
//        logger.traceEntry("ConcursDBRepository save: {}", entity);
//        concursValidator.validate(entity);
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Concurs(categorie,proba) VALUES (?,?)")) {
            stmt.setString(1, entity.getCategorie().name());
            stmt.setString(2, entity.getProba().name());

            //int result = stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1); // ID-ul generat din baza de date
                entity.setId(id); // Setare ID-ul în obiectul Concurs
            }
            //logger.trace("Saved{} instances", result);
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(entity);
        return Optional.of(entity);
    }

    @Override
    public Optional<Concurs> findConcursProbaCategorie(String proba, String categorie) {
        //logger.traceEntry("ConcursDBRepository findOne: {}, {}", proba,categorie);
        Connection conn = dbUtils.getConnection();
        Concurs concurs = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Concurs WHERE proba = ? and categorie = ?")) {
            stmt.setString(1, proba);
            stmt.setString(2, categorie);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int idc = resultSet.getInt("id");
                    String categorieStr = resultSet.getString("categorie");
                    String probaStr = resultSet.getString("proba");

                    Categorie categorieC = Categorie.valueOf(categorieStr);
                    Proba probaC = Proba.valueOf(probaStr);

                    concurs = new Concurs(categorieC, probaC);
                    concurs.setId(idc);
                }
            }
        } catch (SQLException e) {
            //logger.error(e);
            System.err.println("Error DB" + e);
        }
        //logger.traceExit(concurs);
        return Optional.ofNullable(concurs);
    }
}
