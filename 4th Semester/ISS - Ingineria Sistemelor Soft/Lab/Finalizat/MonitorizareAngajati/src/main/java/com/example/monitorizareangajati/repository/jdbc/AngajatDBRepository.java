package com.example.monitorizareangajati.repository.jdbc;

import com.example.monitorizareangajati.domain.Angajat;
import com.example.monitorizareangajati.domain.StatusAngajat;
import com.example.monitorizareangajati.repository.IAngajatRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class AngajatDBRepository implements IAngajatRepository {
    private final JdbcUtils dbUtils;

    public AngajatDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);;
    }

    @Override
    public Optional<Angajat> findOne(Integer id) {
        Connection conn = dbUtils.getConnection();
        Angajat angajat = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Angajat WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Integer ida = resultSet.getInt("id");
                    String usernamea = resultSet.getString("username");
                    String passworda = resultSet.getString("password");
                    String emaila = resultSet.getString("email");
                    String numea = resultSet.getString("nume");
                    String prenumea = resultSet.getString("prenume");
                    String status = resultSet.getString("status");

                    StatusAngajat statusAngajat = StatusAngajat.valueOf(status);

                    angajat = new Angajat(usernamea, passworda, emaila, numea, prenumea, statusAngajat);
                    angajat.setId(ida);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.ofNullable(angajat);
    }

    @Override
    public Iterable<Angajat> findAll() {
        Connection conn = dbUtils.getConnection();
        List<Angajat> angajatList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Angajat")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Integer ida = resultSet.getInt("id");
                    String usernamea = resultSet.getString("username");
                    String passworda = resultSet.getString("password");
                    String emaila = resultSet.getString("email");
                    String numea = resultSet.getString("nume");
                    String prenumea = resultSet.getString("prenume");
                    String status = resultSet.getString("status");

                    StatusAngajat statusAngajat = StatusAngajat.valueOf(status);

                    Angajat angajat = new Angajat(usernamea, passworda, emaila, numea, prenumea, statusAngajat);
                    angajat.setId(ida);

                    angajatList.add(angajat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return angajatList;
    }

    @Override
    public Optional<Angajat> save(Angajat entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Angajat(username, password, email, nume, prenume, status) VALUES (?,?,?,?,?,?) RETURNING id")) {
            stmt.setString(1, entity.getUsername());

            String parolaCriptata = AESUtil.encrypt(entity.getPassword());
            stmt.setString(2, parolaCriptata);

            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getNume());
            stmt.setString(5, entity.getPrenume());
            stmt.setString(6, entity.getStatus().toString());

            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating Participant failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Angajat> update(Angajat entity) {
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("UPDATE Angajat SET username = ?, password = ?, email = ?, nume = ?, prenume = ?, status = ? WHERE id = ?")) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getNume());
            stmt.setString(5, entity.getPrenume());
            stmt.setString(6, entity.getStatus().toString());
            stmt.setInt(7, entity.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Angajat> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Angajat findAccount(String parola, String username) {
        Connection conn = dbUtils.getConnection();
        Angajat angajat = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Angajat WHERE password = ? AND username = ?")) {
            String parolaCriptata = AESUtil.encrypt(parola);
            stmt.setString(1, parolaCriptata);
            stmt.setString(2, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Integer ida = resultSet.getInt("id");
                    String usernamea = resultSet.getString("username");
                    String passworda = resultSet.getString("password");
                    String emaila = resultSet.getString("email");
                    String numea = resultSet.getString("nume");
                    String prenumea = resultSet.getString("prenume");
                    String status = resultSet.getString("status");

                    StatusAngajat statusAngajat = StatusAngajat.valueOf(status);

                    angajat = new Angajat(usernamea, passworda, emaila, numea, prenumea, statusAngajat);
                    angajat.setId(ida);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        } catch (Exception e) {
            System.err.println("Error encrypt" + e);
        }
        return angajat;
    }

    @Override
    public Angajat findByNumePrenume(String nume, String prenume) {
        Connection conn = dbUtils.getConnection();
        Angajat angajat = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Angajat WHERE nume = ? AND prenume = ?")) {
            stmt.setString(1, nume);
            stmt.setString(2, prenume);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Integer ida = resultSet.getInt("id");
                    String usernamea = resultSet.getString("username");
                    String passworda = resultSet.getString("password");
                    String emaila = resultSet.getString("email");
                    String numea = resultSet.getString("nume");
                    String prenumea = resultSet.getString("prenume");
                    String status = resultSet.getString("status");

                    StatusAngajat statusAngajat = StatusAngajat.valueOf(status);

                    angajat = new Angajat(usernamea, passworda, emaila, numea, prenumea, statusAngajat);
                    angajat.setId(ida);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        } catch (Exception e) {
            System.err.println("Error encrypt" + e);
        }
        return angajat;
    }
}
