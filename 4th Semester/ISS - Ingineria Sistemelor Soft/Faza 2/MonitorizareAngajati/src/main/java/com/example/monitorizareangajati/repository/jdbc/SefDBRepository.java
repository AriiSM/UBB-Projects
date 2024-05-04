package com.example.monitorizareangajati.repository.jdbc;

import com.example.monitorizareangajati.domain.Sef;
import com.example.monitorizareangajati.repository.ISefRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class SefDBRepository implements ISefRepository {

    private final JdbcUtils dbUtils;

    public SefDBRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Optional<Sef> findOne(Integer id) {
        Connection conn = dbUtils.getConnection();
        Sef sef = null;
        try (PreparedStatement stms = conn.prepareStatement("SELECT * FROM Sef WHERE id = ?")) {
            stms.setInt(1, id);
            try (ResultSet resultSet = stms.executeQuery()) {
                if (resultSet.next()) {
                    Integer ids = resultSet.getInt("id");
                    String usernames = resultSet.getString("username");
                    String passwords = resultSet.getString("password");
                    String emails = resultSet.getString("email");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");

                    sef = new Sef(usernames, passwords, emails, nume, prenume);
                    sef.setId(ids);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return Optional.ofNullable(sef);
    }

    @Override
    public Iterable<Sef> findAll() {
        Connection conn = dbUtils.getConnection();
        List<Sef> sefList = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sef")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    Integer ids = resultSet.getInt("id");
                    String usernames = resultSet.getString("username");
                    String passwords = resultSet.getString("password");
                    String emails = resultSet.getString("email");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");

                    Sef sef = new Sef(usernames, passwords, emails, nume, prenume);
                    sef.setId(ids);
                    sefList.add(sef);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        }
        return sefList;
    }

    @Override
    public Optional<Sef> save(Sef entity) {
        Sef nou = null;
        Connection conn = dbUtils.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO Sef(username, password, email, nume, prenume) VALUES (?, ?, ?,?,?) RETURNING id")) {
            stmt.setString(1, entity.getUsername());

            String parolaCriptata = AESUtil.encrypt(entity.getPassword());
            stmt.setString(2, parolaCriptata);

            stmt.setString(3, entity.getEmail());
            stmt.setString(4, entity.getNume());
            stmt.setString(5, entity.getPrenume());

            try (ResultSet generatedKeys = stmt.executeQuery()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating Participant failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Sef> update(Sef entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Sef> delete(Integer integer) {
        return Optional.empty();
    }

    @Override
    public Sef findAccount(String username, String parola) {
        Connection conn = dbUtils.getConnection();
        Sef sef = null;
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sef WHERE password = ? AND username = ?")) {
            String parolaCriptata = AESUtil.encrypt(parola);
            stmt.setString(1, parolaCriptata);
            stmt.setString(2, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Integer ids = resultSet.getInt("id");
                    String usernames = resultSet.getString("username");
                    String passwords = resultSet.getString("password");
                    String emails = resultSet.getString("email");
                    String nume = resultSet.getString("nume");
                    String prenume = resultSet.getString("prenume");

                    sef = new Sef(usernames, passwords, emails, nume, prenume);
                    sef.setId(ids);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB" + e);
        } catch (Exception e) {
            System.err.println("Error encrypt" + e);
        }
        return sef;
    }
}
