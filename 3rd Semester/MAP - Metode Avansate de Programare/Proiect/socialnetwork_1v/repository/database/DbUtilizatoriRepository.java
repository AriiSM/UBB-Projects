package com.example.socialnetwork_1v.repository.database;


import com.example.socialnetwork_1v.repository.Repository;
import com.example.socialnetwork_1v.repository.RepositoryOptional;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.domain.validators.Validator;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;
import com.example.socialnetwork_1v.repository.database.paging.PagingRepository;

import java.sql.*;
import java.util.*;

public class DbUtilizatoriRepository implements PagingRepository<Long, Utilizator> {
    private final String url;
    private final String username;
    private final String password;

    Validator<Utilizator> validator;

    public DbUtilizatoriRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Optional<Utilizator> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Utilizator utilizator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"Utilizatori\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                String nume = resultSet.getString("last_name");
                String prenume = resultSet.getString("first_name");

                utilizator = new Utilizator(nume, prenume);
                utilizator.setId(id1);

                return Optional.of(utilizator);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(utilizator);
    }

    @Override
    public Page<Utilizator> findAllOnPage(Pageable pageable) {
        List<Utilizator> utilizators = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pagestatement = connection.prepareStatement("SELECT * FROM public.\"Utilizatori\" LIMIT ? OFFSET ?");
             PreparedStatement countstatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Utilizatori\"")
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
                    Long id = pageResultSet.getLong("id");
                    String nume = pageResultSet.getString("last_name");
                    String prenume = pageResultSet.getString("first_name");
                    Utilizator user = new Utilizator(nume, prenume);
                    user.setId(id);

                    String friends = pageResultSet.getString("friends");
                    if (!Objects.equals(friends, "0")) {
                        List<String> list = List.of(friends.split(", "));
                        for (var f : list) {
                            long id1 = Long.parseLong(f);
                            user.addFriend(findOne(id1).get());
                        }
                    }
                    utilizators.add(user);
                }
                return new Page<>(utilizators, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Utilizatori\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nume = resultSet.getString("last_name");
                String prenume = resultSet.getString("first_name");
                Utilizator user = new Utilizator(nume, prenume);
                user.setId(id);

                String friends = resultSet.getString("friends");
                if (!Objects.equals(friends, "0")) {
                    List<String> list = List.of(friends.split(", "));
                    for (var f : list) {
                        long id1 = Long.parseLong(f);
                        user.addFriend(findOne(id1).get());
                    }
                }
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO public.\"Utilizatori\"(id,first_name,last_name,friends) VALUES (?,?,?,?)")) {
            statement.setLong(1, entity.getId());
            statement.setString(2, entity.getLast_name());
            statement.setString(3, entity.getFirst_name());

            if (!entity.getFriends().isEmpty()) {
                List<Long> ids = new ArrayList<>();
                for (var i : entity.getFriends()) {
                    ids.add(i.getId());
                }
                String lili = ids.toString();
                statement.setString(4, lili.substring(1, lili.length() - 1));
            } else {
                statement.setString(4, "0");
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Utilizator> delete(Long id) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (PreparedStatement statementf = connection.prepareStatement("delete from public.\"Prieteni\" where \"id_utilizator\"=? or \"id_right\"=?")) {
                statementf.setLong(1, id);
                statementf.setLong(2, id);
                statementf.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (PreparedStatement stmt = connection
                    .prepareStatement("DELETE FROM public.\"Utilizatori\" WHERE id='" + id + "'")) {
                //stmt.setLong(1,id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilizator> update(Utilizator entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Userul nu poate fi null!");
        }
        validator.validate(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection
                     .prepareStatement("UPDATE public.\"Utilizatori\" SET first_name=?,last_name=?,friends=? WHERE id='" + entity.getId().toString() + "'")
        ) {
            stmt.setString(1, entity.getFirst_name());
            stmt.setString(2, entity.getLast_name());

            PreparedStatement stmtFr = connection.prepareStatement("SELECT * FROM public.\"Prieteni\"");
            ResultSet resultSet = stmtFr.executeQuery();

            List<Long> ids = new ArrayList<>();
            while (resultSet.next()) {
                long id1 = resultSet.getLong("id_utilizator");
                long id2 = resultSet.getLong("id_right");
                if (id1 == entity.getId())
                    ids.add(id2);
            }
            if (!ids.isEmpty()) {
                String list = ids.toString();
                stmt.setString(3, list.substring(1, list.length() - 1));
            } else
                stmt.setString(3, "0");
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }

    @Override
    public Iterable<Utilizator> changeEntities(Map<Long, Utilizator> entities) {
        return new HashSet<>();
    }


}
