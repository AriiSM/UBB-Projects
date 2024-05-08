package com.example.clienti_locatie_horel_rezervare.repository;

import com.example.clienti_locatie_horel_rezervare.domain.Nivel;
import com.example.clienti_locatie_horel_rezervare.domain.Participant;
import com.example.clienti_locatie_horel_rezervare.domain.Persoana;
import com.example.clienti_locatie_horel_rezervare.domain.Salvamont;

import java.sql.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class DbSalvamont_REPO implements RepositoryOptional<Long, Salvamont> {

    private final String url;
    private final String username;
    private final String password;
    private DbPersoana_REPO persoana_repo;

    public DbSalvamont_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Salvamont> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Salvamont salvamont = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, id_persoana, locatie\n" +
                     "\tFROM public.\"Salvamont\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long ids = resultSet.getLong("id");
                Long id_persoana = resultSet.getLong("id_persoana");
                String locatie = resultSet.getString("locatie");

                Persoana persoana = persoana_repo.findOne(id_persoana).get();
                salvamont = new Salvamont(ids, persoana.getNume(), persoana.getPrenume(), locatie);
                return Optional.of(salvamont);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(salvamont);
    }

    @Override
    public Iterable<Salvamont> findAll() {
        Set<Salvamont> salvamonts = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Salvamont\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long ids = resultSet.getLong("id");
                Long id_persoana = resultSet.getLong("id_persoana");
                String locatie = resultSet.getString("locatie");

                Persoana persoana = persoana_repo.findOne(id_persoana).get();
                Salvamont salvamont = new Salvamont(ids, persoana.getNume(), persoana.getPrenume(), locatie);
                salvamonts.add(salvamont);
            }
            return salvamonts;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salvamonts;
    }

    @Override
    public Optional<Salvamont> save(Salvamont entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Salvamont> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Salvamont> update(Salvamont entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Salvamont> changeEntities(Map<Long, Salvamont> entities) {
        return null;
    }
}
