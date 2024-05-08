package com.example.clienti_locatie_horel_rezervare.repository;

import com.example.clienti_locatie_horel_rezervare.domain.Coordonator;
import com.example.clienti_locatie_horel_rezervare.domain.Participant;
import com.example.clienti_locatie_horel_rezervare.domain.Recenzie;

import java.sql.*;
import java.util.*;

public class DbCoordonator_REPO implements RepositoryOptional<Long, Coordonator> {
    private final String url;
    private final String username;
    private final String password;

    DbParticipant_REPO participant_repo;
    DbRecenzie_REPO recenzie_repo;

    public DbCoordonator_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        recenzie_repo= new DbRecenzie_REPO(url,username,password);
        participant_repo=new DbParticipant_REPO(url,username,password);
    }

    @Override
    public Optional<Coordonator> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Coordonator coordonator = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, id_participant, stelute, recenzii\n" +
                     "\tFROM public.\"Coordonator\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long id_coord = resultSet.getLong("id");
                Long id_participant = resultSet.getLong("id_participant");
                Double stelute = resultSet.getDouble("stelute");
                String recenzii = resultSet.getString("recenzii");

                Participant participant = participant_repo.findOne(id_participant).get();

                List<String> list = List.of(recenzii.split(", "));
                List<Recenzie> recenzieList = new ArrayList<>();
                for (var r : list) {
                    Recenzie recenzie = recenzie_repo.findOne(Long.parseLong(r)).get();
                    recenzieList.add(recenzie);
                }

                coordonator = new Coordonator(id_coord, participant.getNume(), participant.getPrenume(), participant.getNivel(), stelute, recenzieList);

                return Optional.of(coordonator);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(coordonator);
    }

    @Override
    public Iterable<Coordonator> findAll() {
        Set<Coordonator> coordonators = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Coordonator\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id_coord = resultSet.getLong("id");
                Long id_participant = resultSet.getLong("id_participant");
                Double stelute = resultSet.getDouble("stelute");
                String recenzii = resultSet.getString("recenzii");

                Participant participant = participant_repo.findOne(id_participant).get();

                List<String> list = List.of(recenzii.split(", "));
                List<Recenzie> recenzieList = new ArrayList<>();
                for (var r : list) {
                    Recenzie recenzie = recenzie_repo.findOne(Long.parseLong(r)).get();
                    recenzieList.add(recenzie);
                }

                Coordonator coordonator = new Coordonator(id_coord, participant.getNume(), participant.getPrenume(), participant.getNivel(), stelute, recenzieList);

                coordonators.add(coordonator);
            }
            return coordonators;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coordonators;
    }

    @Override
    public Optional<Coordonator> save(Coordonator entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Coordonator> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Coordonator> update(Coordonator entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Coordonator> changeEntities(Map<Long, Coordonator> entities) {
        return null;
    }
}
