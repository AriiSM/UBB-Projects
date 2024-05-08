package com.example.zboruri.repository;
import com.example.zboruri.domain.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DbFlight_REPO implements RepositoryOptional<Long, Flight> {
    private final String url;
    private final String username;
    private final String password;

    public DbFlight_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Flight> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Flight flight = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, \"from\", \"to\", \"departureTime\", \"landingTime\", seats\n" +
                     "\tFROM public.\"Flight\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                Long idf = resultSet.getLong("id");
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                LocalDateTime departureTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landingTime").toLocalDateTime();
                Integer seats = resultSet.getInt("seats");

                flight = new Flight(idf,from,to,departureTime,landingTime,seats);

                return Optional.of(flight);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(flight);
    }

    @Override
    public Iterable<Flight> findAll() {
        Set<Flight> flights = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Flight\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idf = resultSet.getLong("id");
                String from = resultSet.getString("from");
                String to = resultSet.getString("to");
                LocalDateTime departureTime = resultSet.getTimestamp("departureTime").toLocalDateTime();
                LocalDateTime landingTime = resultSet.getTimestamp("landingTime").toLocalDateTime();
                Integer seats = resultSet.getInt("seats");

                Flight flight = new Flight(idf,from,to,departureTime,landingTime,seats);

                flights.add(flight);
            }
            return flights;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public Optional<Flight> save(Flight entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Flight> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Flight> update(Flight entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Flight> changeEntities(Map<Long, Flight> entities) {
        return null;
    }

}
