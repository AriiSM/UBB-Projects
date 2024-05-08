package com.example.restaurante.repository;

import com.example.restaurante.domain.Table;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbTable_REPO implements RepositoryOptional<Long, Table> {
    private final String url;
    private final String username;
    private final String password;

    public DbTable_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Table> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Table table = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, nr\n" +
                     "\tFROM public.\"Table\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long idp = resultSet.getLong("id");
                Integer nr = resultSet.getInt("nr");

                table = new Table(idp,nr);

                return Optional.of(table);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(table);
    }

    @Override
    public Iterable<Table> findAll() {
        Set<Table> tables = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Table\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idp = resultSet.getLong("id");
                Integer nr = resultSet.getInt("nr");


                Table table = new Table(idp,nr);
                tables.add(table);
            }
            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    @Override
    public Optional<Table> save(Table entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Table> delete(Long integer) {
        return Optional.empty();
    }

    @Override
    public Optional<Table> update(Table entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Table> changeEntities(Map<Long, Table> entities) {
        return null;
    }
}
