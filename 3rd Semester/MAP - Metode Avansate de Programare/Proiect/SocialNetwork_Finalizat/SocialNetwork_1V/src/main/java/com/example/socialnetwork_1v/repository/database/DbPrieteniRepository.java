package com.example.socialnetwork_1v.repository.database;


import com.example.socialnetwork_1v.repository.RepositoryOptional;
import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Tuple;
import com.example.socialnetwork_1v.domain.validators.Validator;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.*;

public class DbPrieteniRepository implements RepositoryOptional<Tuple<Long, Long>, Prietenie> {
    private final String url;
    private final String username;
    private final String password;

    Validator<Prietenie> validator;

    //Constructor
    public DbPrieteniRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Prietenie> findOne(Tuple<Long, Long> longLongTuple) {
        return Optional.empty();
    }

    @Override
    public Page<Prietenie> findAllOnPage(Pageable pageable) {
        List<Prietenie> prietenies = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM public.\"Prieteni\" LIMIT ? OFFSET ?");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Prieteni\" ")
        ) {
            pageStatement.setInt(1, pageable.getPageSize());
            pageStatement.setInt(2, pageable.getPageSize() * pageable.getPageNr());

            try (
                    ResultSet pageResultSet = pageStatement.executeQuery();
                    ResultSet countResultSet = countStatement.executeQuery();
            ) {
                int count = 0;
                if(countResultSet.next()){
                    count = countResultSet.getInt("count");
                }

                while(pageResultSet.next()){
                    long id1 = pageResultSet.getLong("id_utilizator");
                    long id2 = pageResultSet.getLong("id_right");
                    Timestamp date = pageResultSet.getTimestamp("data_prietenie");

                    Prietenie friend = new Prietenie(id1, id2, date.toLocalDateTime());
                    prietenies.add(friend);
                }
                return new Page<>(prietenies, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> prieteni = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Prieteni\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id1 = resultSet.getLong("id_utilizator");
                long id2 = resultSet.getLong("id_right");
                Timestamp date = resultSet.getTimestamp("data_prietenie");

                Prietenie friend = new Prietenie(id1, id2, date.toLocalDateTime());
                prieteni.add(friend);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prieteni;
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO public.\"Prieteni\"(id_utilizator,id_right,data_prietenie) VALUES (?,?,?)")) {

            statement.setLong(1, entity.getId().getLeft());
            statement.setLong(2, entity.getId().getRight());
            Timestamp timestamp = Timestamp.valueOf(entity.getDate());
            statement.setTimestamp(3, timestamp);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }

    //TODO

    @Override
    public Optional<Prietenie> delete(Tuple<Long, Long> fID) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("delete from public.\"Prieteni\" where \"id_utilizator\"=? and \"id_right\"=?")) {
            statement.setLong(1, fID.getLeft());
            statement.setLong(1, fID.getRight());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //TODO
    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }
    //TODO

    @Override
    public Iterable<Prietenie> changeEntities(Map<Tuple<Long, Long>, Prietenie> entities) {
        return null;
    }


}
