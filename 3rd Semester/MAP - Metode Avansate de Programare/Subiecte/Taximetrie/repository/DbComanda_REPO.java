package com.example.taximetrie.repository;

import com.example.taximetrie.domain.Comanda;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbComanda_REPO implements RepositoryOptional<Long, Comanda>{
    private final String url;
    private final String username;
    private final String password;

    public DbComanda_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Comanda> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Comanda comanda = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, id_persoana, id_sofer, data\n" +
                     "\tFROM public.\"Comanda\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                Long idc = resultSet.getLong("id");
                Long idp = resultSet.getLong("id_persoana");
                Long ids = resultSet.getLong("id_sofer");
                Timestamp data_comanda = resultSet.getTimestamp("data");

                comanda = new Comanda(idc,idp,ids, data_comanda.toLocalDateTime());

                return Optional.of(comanda);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(comanda);
    }

    @Override
    public Iterable<Comanda> findAll() {
        Set<Comanda> comandas = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Comanda\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idc = resultSet.getLong("id");
                Long idp = resultSet.getLong("id_persoana");
                Long ids = resultSet.getLong("id_sofer");
                Timestamp data_comanda = resultSet.getTimestamp("data");

                Comanda comanda = new Comanda(idc,idp,ids, data_comanda.toLocalDateTime());

                comandas.add(comanda);
            }
            return comandas;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comandas;
    }

    @Override
    public Optional<Comanda> save(Comanda entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Comanda> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Comanda> update(Comanda entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Comanda> changeEntities(Map<Long, Comanda> entities) {
        return null;
    }
}
