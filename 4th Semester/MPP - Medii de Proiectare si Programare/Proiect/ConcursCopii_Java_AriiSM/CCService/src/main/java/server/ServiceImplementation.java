package server;


import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Inscriere;
import client.Domain_Simplu.Organizator;
import client.Domain_Simplu.Participant;
import server.Repo_Jdbc.ConcursDBRepository;
import server.Repo_Jdbc.InscriereDBRepository;
import server.Repo_Jdbc.OrganizatorDBRepository;
import server.Repo_Jdbc.ParticipantDBRepository;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementation implements ICCServices {
    private final ParticipantDBRepository participantDBRepository;
    private final OrganizatorDBRepository organizatorDBRepository;
    private final InscriereDBRepository inscriereDBRepository;
    private final ConcursDBRepository concursDBRepository;
    private Map<String, ICCObserver> loggedClients;


    public ServiceImplementation(ParticipantDBRepository participantDBRepository, OrganizatorDBRepository organizatorDBRepository, InscriereDBRepository inscriereDBRepository, ConcursDBRepository concursDBRepository) {
        this.participantDBRepository = participantDBRepository;
        this.organizatorDBRepository = organizatorDBRepository;
        this.inscriereDBRepository = inscriereDBRepository;
        this.concursDBRepository = concursDBRepository;
        this.loggedClients = new HashMap<>();
    }

    /*////////////////////////////////////////////////////////////////////
    PARTICIPANT
     */////////////////////////////////////////////////////////////////////
//    public Optional<Participant> findOneParticipant(Integer id) {
//        return participantDBRepository.findOne(id);
//    }

    public synchronized Iterable<Participant> findAllParticipant() {
        System.out.println("=======================Server: ");
        System.out.println("findAllParticipant");
        System.out.println("=======================");
        return participantDBRepository.findAll();
    }

    public synchronized Optional<Participant> saveParticipant(Participant entity) {
        System.out.println("=======================Server: ");
        System.out.println("saveParticipant");
        System.out.println("=======================");
        return participantDBRepository.save(entity);
    }

//    public Optional<Participant> updateParticipant(Participant entity) {
//        return participantDBRepository.update(entity);
//    }

    public synchronized List<Participant> filterProbaCategorieParticipant(String proba, String categorie) {
        System.out.println("=======================Server: ");
        System.out.println("filterProbaCategorieParticipant");
        System.out.println("=======================");
        return participantDBRepository.filterProbaCategorie(proba, categorie);
    }

    public synchronized Integer numarProbePentruParticipantParticipant(Integer id) {
        System.out.println("=======================Server: ");
        System.out.println("numarProbePentruParticipantParticipant");
        System.out.println("=======================");
        return participantDBRepository.numarProbePentruParticipant(id);
    }

    public synchronized Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta) {
        System.out.println("=======================Server: ");
        System.out.println("findParticipantNumePrenumeVarsta");
        System.out.println("=======================");
        return participantDBRepository.findParticipantNumePrenumeVarsta(nume, prenume, varsta);
    }

    /*////////////////////////////////////////////////////////////////////
    ORGANIZATOR
     */////////////////////////////////////////////////////////////////////
//    public Optional<Organizator> findOneOrganizator(Integer id) {
//        return organizatorDBRepository.findOne(id);
//    }
//
//    public Iterable<Organizator> findAllOrganizator() {
//        return organizatorDBRepository.findAll();
//    }
//
//    public Optional<Organizator> saveOrganizator(Organizator entity) {
//        return organizatorDBRepository.save(entity);
//    }
//
//    public Optional<Organizator> updateOrganizator(Organizator entity) {
//        return organizatorDBRepository.update(entity);
//    }

    public synchronized Organizator findAccountOrganizator(String parolaO, String numeO, String prenumeO) throws CCException {
        System.out.println("=======================Server: ");
        System.out.println("findAccountOrganizator");
        System.out.println("=======================");
        return organizatorDBRepository.findAccount(parolaO, numeO, prenumeO);

    }


    public synchronized void login(Organizator organizator, ICCObserver client) throws CCException {
        System.out.println("=======================Server: ");
        Organizator userR = organizatorDBRepository.findAccount(organizator.getParola(), organizator.getLastName(), organizator.getFirstName());
        if (userR != null) {
            if (loggedClients.get(userR.getId().toString()) != null)
                throw new CCException("User already logged in.");
            System.out.println("User " + userR.getId().toString() + " is logged in.");
            loggedClients.put(userR.getId().toString(), client);
            //notify();
        } else
            throw new CCException("Authentication failed.");
        System.out.println("=======================");
    }

    public synchronized void logout(Organizator user, ICCObserver client) throws CCException {
        System.out.println("=======================Server: ");
        Organizator userR = organizatorDBRepository.findAccount(user.getParola(), user.getLastName(), user.getFirstName());
        ICCObserver localClient = loggedClients.remove(userR.getId().toString());
        System.out.println("User " + userR.getId().toString() + " is logged out.");
        System.out.println("=======================");
        if (localClient == null)
            throw new CCException("User " + user.getId() + " is not logged in.");
    }

    @Override
    public Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs) {
        System.out.println("=======================Server: ");
        System.out.println("findInscrierePersConc");
        System.out.println("=======================");
        return inscriereDBRepository.findInscrierePersConc(id_participant,id_concurs);
    }


    /*////////////////////////////////////////////////////////////////////
    INSCRIERE
     */////////////////////////////////////////////////////////////////////
//    public Optional<Inscriere> findOneInscriere(Integer id) {
//        return inscriereDBRepository.findOne(id);
//    }
//
//    public Iterable<Inscriere> findAllInscriere() {
//        return inscriereDBRepository.findAll();
//    }
    private final int defaultThreadsNo=5;
    public synchronized Optional<Inscriere> saveInscriere(Inscriere entity) throws CCException{
        System.out.println("=======================Server: ");
        System.out.println("Saving Inscriere...");
        Optional<Inscriere> i = inscriereDBRepository.save(entity);
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);
        for(var user: loggedClients.keySet()){
            System.out.println("Notifying user with id: " + user.toString());
            loggedClients.get(user).inscriereReceives(entity);
        }
        executorService.shutdown();
        System.out.println("=======================");
        return i;
    }


    /*////////////////////////////////////////////////////////////////////
    CONCURS
     */////////////////////////////////////////////////////////////////////
//    public Optional<Concurs> findOneConcurs(Integer id) {
//        return concursDBRepository.findOne(id);
//    }
//
//    public Iterable<Concurs> findAllConcurs() {
//        return concursDBRepository.findAll();
//    }

    public synchronized Optional<Concurs> saveConcurs(Concurs entity) {
        System.out.println("=======================Server: ");
        System.out.println("saveConcurs");
        System.out.println("=======================");
        return concursDBRepository.save(entity);
    }

    public synchronized Optional<Concurs> findConcursProbaCategorie(String proba, String categorie) {
        System.out.println("=======================Server: ");
        System.out.println("findConcursProbaCategorie");
        System.out.println("=======================");
        return concursDBRepository.findConcursProbaCategorie(proba, categorie);
    }
}