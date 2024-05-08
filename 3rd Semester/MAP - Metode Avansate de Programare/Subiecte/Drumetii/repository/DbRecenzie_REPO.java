package com.example.clienti_locatie_horel_rezervare.repository;

import com.example.clienti_locatie_horel_rezervare.domain.Participant;
import com.example.clienti_locatie_horel_rezervare.domain.Persoana;
import com.example.clienti_locatie_horel_rezervare.domain.Recenzie;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbRecenzie_REPO implements RepositoryOptional<Long, Recenzie>{
    private final String url;
    private final String username;
    private final String password;

    public DbRecenzie_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Recenzie> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Recenzie recenzie = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, id_coordonator, id_drumetie, descriere, stelute\n" +
                     "\tFROM public.\"Recenzie\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                Long idr = resultSet.getLong("id");
                Long id_coordonator = resultSet.getLong("id_coordonator");
                Long id_drumetie = resultSet.getLong("id_drumetie");
                String descriere = resultSet.getString("descriere");
                Integer stelute = resultSet.getInt("stelute");

                recenzie = new Recenzie(idr,id_coordonator,id_drumetie,descriere,stelute);
                return Optional.of(recenzie);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(recenzie);
    }

    @Override
    public Iterable<Recenzie> findAll() {
        Set<Recenzie> recenzies = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Recenzie\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idr = resultSet.getLong("id");
                Long id_coordonator = resultSet.getLong("id_coordonator");
                Long id_drumetie = resultSet.getLong("id_drumetie");
                String descriere = resultSet.getString("descriere");
                Integer stelute = resultSet.getInt("stelute");

                Recenzie recenzie = new Recenzie(idr,id_coordonator,id_drumetie,descriere,stelute);
                recenzies.add(recenzie);
            }
            return recenzies;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recenzies;
    }

    @Override
    public Optional<Recenzie> save(Recenzie entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Recenzie> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Recenzie> update(Recenzie entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Recenzie> changeEntities(Map<Long, Recenzie> entities) {
        return null;
    }
}
