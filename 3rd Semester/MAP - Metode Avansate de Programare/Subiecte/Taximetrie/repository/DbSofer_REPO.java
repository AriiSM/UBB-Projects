package com.example.taximetrie.repository;

import com.example.taximetrie.domain.Persoana;
import com.example.taximetrie.domain.Sofer;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbSofer_REPO implements RepositoryOptional<Long, Sofer> {
    private final String url;
    private final String username;
    private final String password;

    private DbPersoana_REPO persoana_repo;

    public DbSofer_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        persoana_repo = new DbPersoana_REPO(url, username, password);
    }

    @Override
    public Optional<Sofer> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Sofer sofer = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, \"indicativMasina\", id_persoana\n" +
                     "\tFROM public.\"Sofer\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long ids = resultSet.getLong("id");
                String indicativMasina = resultSet.getString("indicativMasina");
                Long id_persoana = resultSet.getLong("id_persoana");

                Persoana persoana= persoana_repo.findOne(id_persoana).get();
                sofer = new Sofer(ids, persoana.getUsername(),persoana.getName(),indicativMasina);

                return Optional.of(sofer);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(sofer);
    }

    @Override
    public Iterable<Sofer> findAll() {
        Set<Sofer> sofers = new HashSet<>();


        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, \"indicativMasina\", id_persoana\n" +
                     "\tFROM public.\"Sofer\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long ids = resultSet.getLong("id");
                String indicativMasina = resultSet.getString("indicativMasina");
                Long id_persoana = resultSet.getLong("id_persoana");

                Persoana persoana= persoana_repo.findOne(id_persoana).get();
                Sofer sofer = new Sofer(ids, persoana.getUsername(),persoana.getName(),indicativMasina);
                sofers.add(sofer);
            }
            return sofers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sofers;
    }

    @Override
    public Optional<Sofer> save(Sofer entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Sofer> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Sofer> update(Sofer entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Sofer> changeEntities(Map<Long, Sofer> entities) {
        return null;
    }
}
