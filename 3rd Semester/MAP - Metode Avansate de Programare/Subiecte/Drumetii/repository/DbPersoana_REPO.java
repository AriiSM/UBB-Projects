package com.example.clienti_locatie_horel_rezervare.repository;

import com.example.clienti_locatie_horel_rezervare.domain.Persoana;

import java.sql.*;
import java.util.*;

public class DbPersoana_REPO implements RepositoryOptional<Long, Persoana> {
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
             PreparedStatement statement = connection.prepareStatement("SELECT id, nume, prenume\n" +
                     "\tFROM public.\"Persoana\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                Long idp = resultSet.getLong("id");
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");

                persoana = new Persoana(idp,nume,prenume);

                return Optional.of(persoana);
            }else{
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
                String nume = resultSet.getString("nume");
                String prenume = resultSet.getString("prenume");

                Persoana persoana = new Persoana(idp,nume,prenume);

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
        if (entity == null) {
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        }

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO public.\"Persoana\"(nume,prenume) VALUES (?,?)")) {


            statement.setString(1, entity.getNume());
            statement.setString(2, entity.getPrenume());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity);
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
}
