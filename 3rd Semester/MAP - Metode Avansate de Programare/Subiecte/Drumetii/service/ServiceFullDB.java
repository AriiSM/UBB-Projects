package com.example.clienti_locatie_horel_rezervare.service;

import com.example.clienti_locatie_horel_rezervare.domain.Coordonator;
import com.example.clienti_locatie_horel_rezervare.domain.Drumetie;
import com.example.clienti_locatie_horel_rezervare.domain.Participant;
import com.example.clienti_locatie_horel_rezervare.domain.Persoana;
import com.example.clienti_locatie_horel_rezervare.repository.*;

import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class ServiceFullDB {
    private DbPersoana_REPO persoana_repo;
    private DbParticipant_REPO participant_repo;
    private DbSalvamont_REPO salvamont_repo;
    private DbCoordonator_REPO coordonator_repo;
    private DbRecenzie_REPO recenzie_repo;
    private DbDrumetie_REPO drumetie_repo;

    public ServiceFullDB(DbPersoana_REPO persoana_repo, DbParticipant_REPO participant_repo, DbSalvamont_REPO salvamont_repo, DbCoordonator_REPO coordonator_repo, DbRecenzie_REPO recenzie_repo, DbDrumetie_REPO drumetie_repo) {
        this.persoana_repo = persoana_repo;
        this.participant_repo = participant_repo;
        this.salvamont_repo = salvamont_repo;
        this.coordonator_repo = coordonator_repo;
        this.recenzie_repo = recenzie_repo;
        this.drumetie_repo = drumetie_repo;
    }

    public Persoana findOnePersoana(long id){
        if(persoana_repo.findOne(id).isPresent())
            return persoana_repo.findOne(id).get();
        return null;
    }
    public Coordonator findOneCoordonator(long id){
        if(coordonator_repo.findOne(id).isPresent())
            return coordonator_repo.findOne(id).get();
        return null;
    }
    public Drumetie findOneDrumetie(long id){
        if(drumetie_repo.findOne(id).isPresent())
            return drumetie_repo.findOne(id).get();
        return null;
    }
    public Iterable<Coordonator>getCoordonatorList(){
        return coordonator_repo.findAll();
    }

    public Iterable<Persoana> getPersoanaList(){
        return persoana_repo.findAll();
    }
    public Iterable<Drumetie>getDrumetieList(){
        return drumetie_repo.findAll();
    }
    public Iterable<Participant>getParticipantList(){
        return participant_repo.findAll();
    }
}
