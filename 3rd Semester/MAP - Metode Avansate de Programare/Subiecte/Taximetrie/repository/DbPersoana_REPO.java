package com.example.taximetrie.repository;

import com.example.taximetrie.domain.Persoana;
import com.example.taximetrie.repository.pagini.Page;
import com.example.taximetrie.repository.pagini.Pageable;
import com.example.taximetrie.repository.pagini.PagingRepository;

import java.sql.*;
import java.util.*;

public class DbPersoana_REPO implements PagingRepository<Long, Persoana> {
    private final String url;
    private final String username;
    private final String password;

    public DbPersoana_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Persoana> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Persoana persoana = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, username, nume\n" +
                     "\tFROM public.\"Persoana\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long idp = resultSet.getLong("id");
                String username1 = resultSet.getString("username");
                String nume = resultSet.getString("nume");

                persoana = new Persoana(idp, username1, nume);

                return Optional.of(persoana);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(persoana);
    }

    @Override
    public Iterable<Persoana> findAll() {
        Set<Persoana> persoanas = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Persoana\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idp = resultSet.getLong("id");
                String username1 = resultSet.getString("username");
                String nume = resultSet.getString("nume");

                Persoana persoana = new Persoana(idp, username1, nume);
                persoanas.add(persoana);
            }
            return persoanas;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persoanas;
    }

    @Override
    public Optional<Persoana> save(Persoana entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Persoana> update(Persoana entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Persoana> changeEntities(Map<Long, Persoana> entities) {
        return null;
    }

    @Override
    public Page<Persoana> findAllOnPage(Pageable pageable) {
        List<Persoana> persoanas = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pagestatement = connection.prepareStatement("SELECT * FROM public.\"Persoana\" LIMIT ? OFFSET ?");
             PreparedStatement countstatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Persoana\"")
        ) {
            pagestatement.setInt(1, pageable.getPageSize());
            pagestatement.setInt(2, pageable.getPageSize() * pageable.getPageNr());

            try (
                    ResultSet pageResultSet = pagestatement.executeQuery();
                    ResultSet countResultSet = countstatement.executeQuery();
            ) {
                int count = 0;
                if (countResultSet.next()) {
                    count = countResultSet.getInt("count");
                }

                while (pageResultSet.next()) {
                    Long idp = pageResultSet.getLong("id");
                    String username1 = pageResultSet.getString("username");
                    String nume = pageResultSet.getString("nume");

                    Persoana persoana = new Persoana(idp, username1, nume);
                    persoanas.add(persoana);
                }
                return new Page<>(persoanas, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
