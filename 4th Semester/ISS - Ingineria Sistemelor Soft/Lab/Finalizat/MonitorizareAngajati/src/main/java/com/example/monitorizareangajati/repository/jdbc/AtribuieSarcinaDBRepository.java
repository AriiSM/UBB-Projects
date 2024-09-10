package com.example.monitorizareangajati.repository.jdbc;

import com.example.monitorizareangajati.domain.Angajat;
import com.example.monitorizareangajati.domain.AtribuireSarcina;
import com.example.monitorizareangajati.domain.Sarcina;
import com.example.monitorizareangajati.domain.StatusSarcina;
import com.example.monitorizareangajati.repository.IAtribuieSarcinaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class AtribuieSarcinaDBRepository implements IAtribuieSarcinaRepository {
    private final JdbcUtils dbUtils;
    private final AngajatDBRepository angajatDBRepository;
    private final SarcinaDBRepository sarcinaDBRepository;

    public AtribuieSarcinaDBRepository(Properties props, AngajatDBRepository angajatDBRepository, SarcinaDBRepository sarcinaDBRepository) {
        dbUtils = new JdbcUtils(props);
        this.angajatDBRepository = angajatDBRepository;
        this.sarcinaDBRepository = sarcinaDBRepository;
    }

    @Override
    public Optional<AtribuireSarcina> findOne(Integer id) {
        Connection conn = dbUtils.getConnection();
        AtribuireSarcina atribuireSarcina = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AtribuireSarcina WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int idas = resultSet.getInt("id");
                    int id_angajat = resultSet.getInt("id_angajat");
                    int id_sarcina = resultSet.getInt("id_sarcina");
                    String status = resultSet.getString("status");

                    Angajat angajat = angajatDBRepository.findOne(id_angajat).get();
                    Sarcina sarcina = sarcinaDBRepository.findOne(id_sarcina).get();

                    StatusSarcina statusSarcina = StatusSarcina.valueOf(status);

                    atribuireSarcina = new AtribuireSarcina(angajat, sarcina,statusSarcina);
                    atribuireSarcina.setId(idas);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.ofNullable(atribuireSarcina);
    }

    @Override
    public Iterable<AtribuireSarcina> findAll() {
        Connection conn = dbUtils.getConnection();
        List<AtribuireSarcina> atribuireSarcinaList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AtribuireSarcina")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int idas = resultSet.getInt("id");
                    int id_angajat = resultSet.getInt("id_angajat");
                    int id_sarcina = resultSet.getInt("id_sarcina");
                    String status = resultSet.getString("status");

                    Angajat angajat = angajatDBRepository.findOne(id_angajat).get();
                    Sarcina sarcina = sarcinaDBRepository.findOne(id_sarcina).get();

                    StatusSarcina statusSarcina = StatusSarcina.valueOf(status);

                    AtribuireSarcina atribuireSarcina = new AtribuireSarcina(angajat, sarcina,statusSarcina);
                    atribuireSarcina.setId(idas);

                    atribuireSarcinaList.add(atribuireSarcina);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return atribuireSarcinaList;
    }

    @Override
    public Optional<AtribuireSarcina> save(AtribuireSarcina entity) {
        Connection conn = dbUtils.getConnection();
        System.out.println("Salvare inscriere...");
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO AtribuireSarcina(id_angajat, id_sarcina, status) VALUES (?,?,?) RETURNING id")) {
            stmt.setInt(1, entity.getAngajat().getId());
            stmt.setInt(2, entity.getSarcina().getId());
            stmt.setString(3, entity.getStatus().toString());

            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating inscriere failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<AtribuireSarcina> update(AtribuireSarcina entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE AtribuireSarcina SET id_angajat = ?, id_sarcina = ?, status = ? WHERE id = ?")) {
            stmt.setInt(1, entity.getAngajat().getId());
            stmt.setInt(2, entity.getSarcina().getId());
            stmt.setString(3, entity.getStatus().toString());
            stmt.setInt(4, entity.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<AtribuireSarcina> delete(Integer integer) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM AtribuireSarcina WHERE id = ?")) {
            stmt.setInt(1, integer);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<AtribuireSarcina> findAtribuireSarcinaAngajat(Integer id) {
        Connection conn = dbUtils.getConnection();
        List<AtribuireSarcina> atribuireSarcinaList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM AtribuireSarcina WHERE id_angajat = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int idas = resultSet.getInt("id");
                    int id_angajat = resultSet.getInt("id_angajat");
                    int id_sarcina = resultSet.getInt("id_sarcina");
                    String status = resultSet.getString("status");

                    Angajat angajat = angajatDBRepository.findOne(id_angajat).get();
                    Sarcina sarcina = sarcinaDBRepository.findOne(id_sarcina).get();

                    StatusSarcina statusSarcina = StatusSarcina.valueOf(status);

                    AtribuireSarcina atribuireSarcina = new AtribuireSarcina(angajat, sarcina,statusSarcina);
                    atribuireSarcina.setId(idas);

                    atribuireSarcinaList.add(atribuireSarcina);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return atribuireSarcinaList;
    }
}
