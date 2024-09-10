package server;


import client.Domain_Simplu.Inscriere;
import client.Domain_Simplu.Organizator;
import client.Domain_Simplu.Participant;
import client.Domain_Simplu.Concurs;

import java.util.List;
import java.util.Optional;

public interface ICCServices {
    /*////////////////////////////////////////////////////////////////////
    PARTICIPANT
     */////////////////////////////////////////////////////////////////////

    Iterable<Participant> findAllParticipant() throws CCException;

    Optional<Participant> saveParticipant(Participant entity) throws CCException;

    List<Participant> filterProbaCategorieParticipant(String proba, String categorie) throws CCException;

    Integer numarProbePentruParticipantParticipant(Integer id) throws CCException;

    Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta) throws CCException;

    /*////////////////////////////////////////////////////////////////////
    ORGANIZATOR
    /*////////////////////////////////////////////////////////////////////
    Organizator findAccountOrganizator(String parolaO, String numeO, String prenumeO) throws CCException;

    void login(Organizator user, ICCObserver client) throws CCException;

    void logout(Organizator user, ICCObserver client) throws CCException;


    /*////////////////////////////////////////////////////////////////////
    INSCRIERE
     */////////////////////////////////////////////////////////////////////

    Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs) throws CCException;

    Optional<Inscriere> saveInscriere(Inscriere entity) throws CCException;


    /*////////////////////////////////////////////////////////////////////
    CONCURS
     */////////////////////////////////////////////////////////////////////

    Optional<Concurs> saveConcurs(Concurs entity) throws CCException;

    Optional<Concurs> findConcursProbaCategorie(String proba, String categorie) throws CCException;
}
