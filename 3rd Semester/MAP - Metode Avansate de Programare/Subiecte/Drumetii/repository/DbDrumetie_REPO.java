package com.example.clienti_locatie_horel_rezervare.repository;

import com.example.clienti_locatie_horel_rezervare.domain.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class DbDrumetie_REPO implements RepositoryOptional<Long, Drumetie>{
    private final String url;
    private final String username;
    private final String password;

    DbParticipant_REPO participant_repo;

    public DbDrumetie_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        participant_repo = new DbParticipant_REPO(url,username,password);
    }

    @Override
    public Optional<Drumetie> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Drumetie drumetie = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, id_coordonator, participanti, nivel, durata, \"dataDrumetie\"\n" +
                     "\tFROM public.\"Drumetie\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if(resultSet.next()){
                Long idd = resultSet.getLong("id");
                Long id_coordonator = resultSet.getLong("id_coordonator");
                String participanti = resultSet.getString("participanti");
                String nivel = resultSet.getString("nivel");
                Integer durata = resultSet.getInt("durata");
                Timestamp dataDrumetie = resultSet.getTimestamp("dataDrumetie");

                List<String> list = List.of(participanti.split(", "));
                List<Participant> participantiList = new ArrayList<>();
                for (var r : list) {
                    Participant participant = participant_repo.findOne(Long.parseLong(r)).get();
                    participantiList.add(participant);
                }
                if(Objects.equals(nivel, "Incepator"))
                    drumetie = new Drumetie(idd,id_coordonator,participantiList, Nivel.Incepator,durata,dataDrumetie);
                if(Objects.equals(nivel, "Mediu"))
                    drumetie = new Drumetie(idd,id_coordonator,participantiList, Nivel.Mediu,durata,dataDrumetie);
                if(Objects.equals(nivel, "Avansat"))
                    drumetie = new Drumetie(idd,id_coordonator,participantiList, Nivel.Avansat,durata,dataDrumetie);

                return Optional.of(drumetie);
            }else{
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(drumetie);
    }

    @Override
    public Iterable<Drumetie> findAll() {
        Set<Drumetie> drumeties = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Drumetie\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long idd = resultSet.getLong("id");
                Long id_coordonator = resultSet.getLong("id_coordonator");
                String participanti = resultSet.getString("participanti");
                String nivel = resultSet.getString("nivel");
                Integer durata = resultSet.getInt("durata");
                Timestamp dataDrumetie = resultSet.getTimestamp("dataDrumetie");

                List<String> list = List.of(participanti.split(", "));
                List<Participant> participantiList = new ArrayList<>();
                for (var r : list) {
                    Participant participant = participant_repo.findOne(Long.parseLong(r)).get();
                    participantiList.add(participant);
                }

                Drumetie drumetie=null;

                if(Objects.equals(nivel, "Incepator"))
                    drumetie = new Drumetie(idd,id_coordonator,participantiList, Nivel.Incepator,durata,dataDrumetie);
                if(Objects.equals(nivel, "Mediu"))
                    drumetie = new Drumetie(idd,id_coordonator,participantiList, Nivel.Mediu,durata,dataDrumetie);
                if(Objects.equals(nivel, "Avansat"))
                    drumetie = new Drumetie(idd,id_coordonator,participantiList, Nivel.Avansat,durata,dataDrumetie);

                drumeties.add(drumetie);
            }
            return drumeties;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drumeties;
    }

    @Override
    public Optional<Drumetie> save(Drumetie entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Drumetie> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Drumetie> update(Drumetie entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Drumetie> changeEntities(Map<Long, Drumetie> entities) {
        return null;
    }
}
