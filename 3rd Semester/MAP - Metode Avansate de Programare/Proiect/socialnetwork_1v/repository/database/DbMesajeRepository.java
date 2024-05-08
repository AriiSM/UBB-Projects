package com.example.socialnetwork_1v.repository.database;

import com.example.socialnetwork_1v.domain.Message;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.repository.RepositoryOptional;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DbMesajeRepository implements RepositoryOptional<Long, Message> {
    private String url;
    private String username;
    private String password;
    private RepositoryOptional<Long, Utilizator> utilizatorRepositoryOptional;

    public DbMesajeRepository(String url, String username, String password, RepositoryOptional<Long, Utilizator> utilizatorRepositoryOptional) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.utilizatorRepositoryOptional = utilizatorRepositoryOptional;
    }

    @Override
    public Optional<Message> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Message message = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Message\" WHERE id ='" + id + "'");
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                Long id1 = resultSet.getLong("id");
                Long fromUser1 = resultSet.getLong("from");
                String toUser1 = resultSet.getString("to");
                String mesaj1 = resultSet.getString("mesaj");
                Timestamp date1 = resultSet.getTimestamp("data");
                Optional<Long> idReply = Optional.ofNullable(resultSet.getLong("id_reply"));


                String[] toUserOneByOne = toUser1.split(",");
                List<Utilizator> toUsersList1 = new ArrayList<>();
                for (var user : toUserOneByOne) {
                    toUsersList1.add(utilizatorRepositoryOptional.findOne(Long.parseLong(user)).get());
                }
                Utilizator userFrom1 = utilizatorRepositoryOptional.findOne(fromUser1).get();

                message = new Message(id1, mesaj1, userFrom1, toUsersList1, date1.toLocalDateTime(), idReply);
                return Optional.of(message);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(message);
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messageSet = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Message\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long fromUser = resultSet.getLong("from");
                String toUser = resultSet.getString("to");
                String mesaj = resultSet.getString("mesaj");
                Timestamp date = resultSet.getTimestamp("data");
                Optional<Long> idReply = Optional.ofNullable(resultSet.getLong("id_reply"));

                String[] toUserOneByOne = toUser.split(",");
                List<Utilizator> toUsersList = new ArrayList<>();

                for (var user : toUserOneByOne) {
                    toUsersList.add(utilizatorRepositoryOptional.findOne(Long.parseLong(user)).get());
                }
                Message message;
                Utilizator userFrom = utilizatorRepositoryOptional.findOne(fromUser).get();

                message = new Message(id, mesaj, userFrom, toUsersList, date.toLocalDateTime(), idReply);

                messageSet.add(message);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageSet;
    }

    @Override
    public Optional<Message> save(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO public.\"Message\"(\"from\",\"to\",mesaj,data) VALUES (?,?,?,?)"
             );
             PreparedStatement statementReply = connection.prepareStatement
                     ("INSERT INTO public.\"Message\"(\"from\",\"to\",mesaj,data,id_reply) VALUES (?,?,?,?,?)")) {
            //statement.setLong(1, entity.getId());
            if(entity.getIdReply().isPresent()){
                statementReply.setLong(1, entity.getFrom().getId());
                statementReply.setString(2, entity.getToIdString());
                statementReply.setString(3, entity.getMesaj());
                LocalDateTime ldt = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(ldt);
                statementReply.setTimestamp(4, timestamp);
                statementReply.setLong(5,entity.getIdReply().get());
                statementReply.executeUpdate();
            }else{
                statement.setLong(1, entity.getFrom().getId());
                statement.setString(2, entity.getToIdString());
                statement.setString(3, entity.getMesaj());
                LocalDateTime ldt = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(ldt);
                statement.setTimestamp(4, timestamp);
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Message> changeEntities(Map<Long, Message> entities) {
        return null;
    }

    @Override
    public Page<Message> findAllOnPage(Pageable pageable) {
        return null;
    }
}
