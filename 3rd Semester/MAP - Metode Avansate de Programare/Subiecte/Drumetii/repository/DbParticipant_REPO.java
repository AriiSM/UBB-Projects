package com.example.clienti_locatie_horel_rezervare.repository;

import com.example.clienti_locatie_horel_rezervare.domain.Nivel;
import com.example.clienti_locatie_horel_rezervare.domain.Participant;
import com.example.clienti_locatie_horel_rezervare.domain.Persoana;

import java.sql.*;
import java.util.*;


public class DbParticipant_REPO implements RepositoryOptional<Long, Participant> {
    private final String url;
    private final String username;
    private final String password;

    private DbPersoana_REPO persoana_repo ;

    public DbParticipant_REPO(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        persoana_repo = new DbPersoana_REPO(url,username,password);
    }

    @Override
    public Optional<Participant> findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Id nu poate fi null");
        Participant participant = null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT id, id_persoana, nivel FROM public.\"Participant\" WHERE id='" + id + "'");
             ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                Long id_participant = resultSet.getLong("id");
                Long id_persoana = resultSet.getLong("id_persoana");

                String nivel = resultSet.getString("nivel");

                Persoana persoana = persoana_repo.findOne(id_persoana).get();

                if (Objects.equals(nivel, "Incepator")){
                    participant = new Participant(id_participant, persoana.getNume(), persoana.getPrenume(), Nivel.Incepator);}
                if (Objects.equals(nivel, "Mediu")){
                    participant = new Participant(id_participant, persoana.getNume(), persoana.getPrenume(), Nivel.Mediu);}
                if(Objects.equals(nivel, "Avansat")){
                    participant = new Participant(id_participant,persoana.getNume(),persoana.getPrenume(),Nivel.Avansat);}

                return Optional.of(participant);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(participant);
    }

    @Override
    public Iterable<Participant> findAll() {
        Set<Participant> participants = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM public.\"Participant\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id_participant = resultSet.getLong("id");
                Long id_persoana = resultSet.getLong("id_persoana");
                String nivel = resultSet.getString("nivel");

                Persoana persoana = persoana_repo.findOne(id_persoana).get();
                Participant participant=null;
                if (Objects.equals(nivel, "Incepator"))
                     participant = new Participant(id_participant, persoana.getNume(), persoana.getPrenume(), Nivel.Incepator);
                if (Objects.equals(nivel, "Mediu"))
                    participant = new Participant(id_participant, persoana.getNume(), persoana.getPrenume(), Nivel.Mediu);
                if(Objects.equals(nivel, "Avansat"))
                    participant = new Participant(id_participant,persoana.getNume(),persoana.getPrenume(),Nivel.Avansat);

                participants.add(participant);
            }
            return participants;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participants;
    }

    @Override
    public Optional<Participant> save(Participant entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Participant> update(Participant entity) {
        return Optional.empty();
    }

    @Override
    public Iterable<Participant> changeEntities(Map<Long, Participant> entities) {
        return null;
    }
}
