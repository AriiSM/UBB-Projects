package com.example.zboruri.repository;
import com.example.zboruri.domain.Tiket;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DbTiket_REPO implements RepositoryOptional<Long, Tiket> {
    private final String url;
    private final String username;
    private final String password;

    public DbTiket_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Tiket> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Tiket tiket = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, username, id_flight, \"purchaseTime\"\n" +
                     "\tFROM public.\"Tiket\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                Long idt = resultSet.getLong("id");
                String username = resultSet.getString("username");
                Long id_flight = resultSet.getLong("id_flight");
                LocalDateTime purchaseTime = resultSet.getTimestamp("purchaseTime").toLocalDateTime();


                tiket = new Tiket(idt,username,purchaseTime,id_flight);

                return Optional.of(tiket);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(tiket);
    }

    @Override
    public Iterable<Tiket> findAll() {
        Set<Tiket> tikets = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Tiket\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idt = resultSet.getLong("id");
                String username = resultSet.getString("username");
                Long id_flight = resultSet.getLong("id_flight");
                LocalDateTime purchaseTime = resultSet.getTimestamp("purchaseTime").toLocalDateTime();

                Tiket tiket = new Tiket(idt,username,purchaseTime,id_flight);

                tikets.add(tiket);
            }
            return tikets;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tikets;
    }

    @Override
    public Optional<Tiket> save(Tiket entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO public.\"Tiket\"(username, id_flight, \"purchaseTime\")\n" +
                             "\tVALUES (?, ?, ?)")) {


            statement.setString(1, entity.getUsername());
            statement.setLong(2, entity.getId_flight());
            statement.setTimestamp(3, Timestamp.valueOf(entity.getPurchaseTime()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Tiket> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Tiket> update(Tiket entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Tiket> changeEntities(Map<Long, Tiket> entities) {
        return null;
    }


}
