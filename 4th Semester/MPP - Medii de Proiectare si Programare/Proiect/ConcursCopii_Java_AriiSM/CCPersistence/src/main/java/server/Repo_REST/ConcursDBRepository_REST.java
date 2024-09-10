package server.Repo_REST;


import client.Domain_REST.Categorie;
import client.Domain_REST.Concurs;
import client.Domain_REST.Proba;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import server.Repo_REST.Interface.IConcursRepository;
import server.Repo_REST.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


@Component
public class ConcursDBRepository_REST implements IConcursRepository{
    private final JdbcUtils dbUtils;
    //private final Logger logger = LogManager.getLogger(ConcursDBRepository.class);

    @Autowired
    public ConcursDBRepository_REST(@Qualifier("props") Properties props) {
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
        Connection conn = dbUtils.getConnection();
        try {
            // Insert the new Concurs
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Concurs(categorie,proba) VALUES (?,?)")) {
                stmt.setString(1, entity.getCategorie().name());
                stmt.setString(2, entity.getProba().name());
                stmt.executeUpdate();
            }

            // Retrieve the ID of the last inserted Concurs
            try (PreparedStatement stmt = conn.prepareStatement("SELECT last_insert_rowid()")) {
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt(1); // ID-ul generat din baza de date
                        entity.setId(id); // Setare ID-ul în obiectul Concurs
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
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

    @Override
    public void delete(Integer id) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Concurs WHERE id = ?")) {
            stmt.setInt(1, id);
            int result = stmt.executeUpdate();
            if (result == 0) {
                System.err.println("No rows deleted, check if id exists");
            } else {
                System.out.println("Deleted " + result + " instances");
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
    }

    @Override
    public void update(Integer id, Concurs entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE Concurs SET categorie = ?, proba = ? WHERE id = ?")) {
            stmt.setString(1, entity.getCategorie().name());
            stmt.setString(2, entity.getProba().name());
            stmt.setInt(3, id);
            int result = stmt.executeUpdate();
            if (result == 0) {
                System.err.println("No rows updated, check if id exists");
            } else {
                System.out.println("Updated " + result + " instances");
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
    }
}
