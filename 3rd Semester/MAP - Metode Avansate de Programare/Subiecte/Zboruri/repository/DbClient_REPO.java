package com.example.zboruri.repository;

import com.example.zboruri.domain.Client;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbClient_REPO implements RepositoryOptional<Long, Client>{
    private final String url;
    private final String username;
    private final String password;

    public DbClient_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Client> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Client client = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, username, nume\n" +
                     "\tFROM public.\"Client\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                Long idc = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String nume = resultSet.getString("nume");

                client = new Client(idc,username,nume);

                return Optional.of(client);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(client);
    }

    @Override
    public Iterable<Client> findAll() {
        Set<Client> clients = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Client\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long idc = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String nume = resultSet.getString("nume");

                Client client =  new Client(idc,username,nume);

                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Optional<Client> save(Client entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Client> update(Client entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Client> changeEntities(Map<Long, Client> entities) {
        return null;
    }
}
