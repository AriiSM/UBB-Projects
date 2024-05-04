package com.example.monitorizareangajati.repository.jdbc;

import com.example.monitorizareangajati.domain.Sarcina;
import com.example.monitorizareangajati.repository.ISarcinaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class SarcinaDBRepository implements ISarcinaRepository{
    private final JdbcUtils dbUtils;

    public SarcinaDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Sarcina> findOne(Integer id) {
        Connection conn = dbUtils.getConnection();
        Sarcina sarcina = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sarcina WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int ids = resultSet.getInt("id");
                    String descriere = resultSet.getString("descriere");

                    sarcina = new Sarcina(descriere);
                    sarcina.setId(ids);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.ofNullable(sarcina);
    }

    @Override
    public Iterable<Sarcina> findAll() {
        Connection conn = dbUtils.getConnection();
        List<Sarcina> sarcinaList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sarcina")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int ids = resultSet.getInt("id");
                    String descriere = resultSet.getString("descriere");

                    Sarcina sarcina = new Sarcina(descriere);
                    sarcina.setId(ids);

                    sarcinaList.add(sarcina);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return sarcinaList;
    }

    @Override
    public Optional<Sarcina> save(Sarcina entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Sarcina(descriere) VALUES (?)")) {
            stmt.setString(1, entity.getDescriere());


            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entity.setId(id);
            }

        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Sarcina> update(Sarcina entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Sarcina> delete(Integer integer) {
        return Optional.empty();
    }
}
