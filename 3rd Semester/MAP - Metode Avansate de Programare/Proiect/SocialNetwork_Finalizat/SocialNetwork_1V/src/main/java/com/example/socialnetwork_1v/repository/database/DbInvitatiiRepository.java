package com.example.socialnetwork_1v.repository.database;

import com.example.socialnetwork_1v.domain.*;
import com.example.socialnetwork_1v.repository.RepositoryOptional;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class DbInvitatiiRepository implements RepositoryOptional<Long, Invitatii> {
    private String url;
    private String username;
    private String password;
    private RepositoryOptional<Long, Utilizator> utilizatorRepositoryOptional;

    private RepositoryOptional<Long, Utilizator> userDataBase;
    private RepositoryOptional<Tuple<Long, Long>, Prietenie> repoPrietenie;
    private RepositoryOptional<Long, Message> messageRepo;

    public DbInvitatiiRepository(String url, String username, String password, RepositoryOptional<Long, Utilizator> userDataBase, RepositoryOptional<Tuple<Long, Long>, Prietenie> repoPrietenie, RepositoryOptional<Long, Message> messageRepo) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userDataBase = userDataBase;
        this.repoPrietenie = repoPrietenie;
        this.messageRepo = messageRepo;
    }


    @Override
    public Optional<Invitatii> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Page<Invitatii> findAllOnPage(Pageable pageable) {
        List<Invitatii> invitatiiList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM public.\"Invitatii\" LIMIT ? OFFSET ?");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM public.\"Invitatii\"")
        ) {
            pageStatement.setInt(1, pageable.getPageSize());
            pageStatement.setInt(2, pageable.getPageSize() * pageable.getPageNr());

            try (
                    ResultSet pageResultSet = pageStatement.executeQuery();
                    ResultSet countResultSet = countStatement.executeQuery();
            ) {
                int count = 0;
                if (countResultSet.next()) {
                    count = countResultSet.getInt("count");
                }

                while (pageResultSet.next()) {
                    Long id = pageResultSet.getLong("id");
                    Long user1ID = pageResultSet.getLong("fromInvite");
                    Long user2ID = pageResultSet.getLong("toInvite");
                    java.sql.Date date = pageResultSet.getDate("dateInvite");
                    LocalDateTime ldt = date.toLocalDate().atStartOfDay();
                    String statusInvite = pageResultSet.getString("status");

                    Utilizator user1 = userDataBase.findOne(user1ID).get();
                    Utilizator user2 = userDataBase.findOne(user2ID).get();

                    Invitatii invitatie = new Invitatii(id, user1, user2, ldt, statusInvite);
                    invitatiiList.add(invitatie);
                }
                return new Page<>(invitatiiList, count);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Invitatii> findAll() {
        Set<Invitatii> invitatiiSet = new HashSet<>();
        Invitatii invitatie = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Invitatii\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long user1ID = resultSet.getLong("fromInvite");
                Long user2ID = resultSet.getLong("toInvite");
                java.sql.Date date = resultSet.getDate("dateInvite");
                LocalDateTime ldt = date.toLocalDate().atStartOfDay();
                String statusInvite = resultSet.getString("status");

                Utilizator user1 = userDataBase.findOne(user1ID).get();
                Utilizator user2 = userDataBase.findOne(user2ID).get();

                invitatie = new Invitatii(id, user1, user2, ldt, statusInvite);
                invitatiiSet.add(invitatie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitatiiSet;
    }

    @Override
    public Optional<Invitatii> save(Invitatii entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement
                     ("INSERT INTO public.\"Invitatii\"(\"fromInvite\",\"toInvite\",\"dateInvite\",status) VALUES (?,?,?,?)")) {
            statement.setLong(1, entity.getFromInvite().getId());
            statement.setLong(2, entity.getToInvite().getId());
            Timestamp timestamp = Timestamp.valueOf(entity.getDateInvite());
            Date date = new Date(timestamp.getTime());
            statement.setDate(3, date);
            statement.setString(4, entity.getStatus());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Invitatii> delete(Long id) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM public.\"Invitatii\" WHERE id=?")) {
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Invitatii> update(Invitatii entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Userul nu poate fi null!");
        }
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = connection
                     .prepareStatement("UPDATE public.\"Invitatii\" SET \"fromInvite\"=?,\"toInvite\"=?,status=?,\"dateInvite\"=? WHERE id='" + entity.getId().toString() + "'")) {
            stmt.setLong(1, entity.getFromInvite().getId());
            stmt.setLong(2, entity.getToInvite().getId());
            stmt.setString(3, entity.getStatus());
            Timestamp timestamp = Timestamp.valueOf(entity.getDateInvite());
            Date date = new Date(timestamp.getTime());
            stmt.setDate(4, date);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Invitatii> changeEntities(Map<Long, Invitatii> entities) {
        return null;
    }


}
