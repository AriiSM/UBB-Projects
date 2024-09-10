package server;

import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Organizator;
import client.Domain_Simplu.Inscriere;
import client.Domain_Simplu.Participant;
import server.Repo_Hibernate.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImplementationHibernate implements ICCServices {

    private final ParticipantDBRepository_Hibernate participantDBRepository;
    private final OrganizatorDBRepository_Hibernate organizatorDBRepository;
    private final InscriereDBRepository_Hibernate inscriereDBRepository;
    private final ConcursDBRepository_Hibernate concursDBRepository;
    private Map<String, ICCObserver> loggedClients;

    public ServiceImplementationHibernate(ParticipantDBRepository_Hibernate participantDBRepository, OrganizatorDBRepository_Hibernate organizatorDBRepository, InscriereDBRepository_Hibernate inscriereDBRepository, ConcursDBRepository_Hibernate concursDBRepository) {
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
        Iterable<client.Domain_Hibernate.Participant> participants = participantDBRepository.findAll();
        ArrayList<Participant> new_participants = new ArrayList<>();
        for (var participant : participants)
            new_participants.add(HibernateUtils.convertParticipant_FromHibernate(participant));
        return new_participants;
    }

    public synchronized Optional<Participant> saveParticipant(Participant entity) {
        System.out.println("=======================Server: ");
        System.out.println("saveParticipant");
        System.out.println("=======================");
        Optional<client.Domain_Hibernate.Participant> participant = participantDBRepository.save(HibernateUtils.convertParticipant_ToHibernate(entity));
        return Optional.of(HibernateUtils.convertParticipant_FromHibernate(participant.get()));
    }

//    public Optional<Participant> updateParticipant(Participant entity) {
//        return participantDBRepository.update(entity);
//    }

    public synchronized List<Participant> filterProbaCategorieParticipant(String proba, String categorie) {
        System.out.println("=======================Server: ");
        System.out.println("filterProbaCategorieParticipant");
        System.out.println("=======================");
        List<client.Domain_Hibernate.Participant> participantList = participantDBRepository.filterProbaCategorie(proba, categorie);
        ArrayList<Participant> new_participants = new ArrayList<>();
        for (var participant : participantList)
            new_participants.add(HibernateUtils.convertParticipant_FromHibernate(participant));
        return new_participants;
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
        Optional<client.Domain_Hibernate.Participant> participant = participantDBRepository.findParticipantNumePrenumeVarsta(nume, prenume, varsta);
        if (participant.isPresent())
            return Optional.of(HibernateUtils.convertParticipant_FromHibernate(participant.get()));
        else
            return Optional.empty();
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
        client.Domain_Hibernate.Organizator organizator = organizatorDBRepository.findAccount(parolaO, numeO, prenumeO);
        Organizator org = HibernateUtils.convertOrganizator_FromHibernate(organizator);
        return org;
    }


    public synchronized void login(Organizator organizator, ICCObserver client) throws CCException {
        System.out.println("=======================Server: ");
        client.Domain_Hibernate.Organizator or = organizatorDBRepository.findAccount(organizator.getParola(), organizator.getLastName(), organizator.getFirstName());
        Organizator userR = HibernateUtils.convertOrganizator_FromHibernate(or);
        //Organizator userR = organizator;
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
        client.Domain_Hibernate.Organizator or = organizatorDBRepository.findAccount(user.getParola(), user.getLastName(), user.getFirstName());
        Organizator userR = HibernateUtils.convertOrganizator_FromHibernate(or);
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
        Optional<client.Domain_Hibernate.Inscriere> inscriere = inscriereDBRepository.findInscrierePersConc(id_participant, id_concurs);
        return Optional.of(HibernateUtils.convertInscriere_FromHibernate(inscriere.get()));
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
    private final int defaultThreadsNo = 5;

    public synchronized Optional<Inscriere> saveInscriere(Inscriere entity) throws CCException {
        System.out.println("=======================Server: ");
        System.out.println("Saving Inscriere...");
        Optional<client.Domain_Hibernate.Inscriere> inscriere = inscriereDBRepository.save(HibernateUtils.convertInscriere_ToHibernate(entity));
        Optional<Inscriere> i = Optional.of(HibernateUtils.convertInscriere_FromHibernate(inscriere.get()));
        ExecutorService executorService = Executors.newFixedThreadPool(defaultThreadsNo);
        for (var user : loggedClients.keySet()) {
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
        Optional<client.Domain_Hibernate.Concurs> concurs = concursDBRepository.save(HibernateUtils.convertConcurs_ToHibernate(entity));
        return Optional.of(HibernateUtils.convertConcurs_FromHibernate(concurs.get()));
    }

    public synchronized Optional<Concurs> findConcursProbaCategorie(String proba, String categorie) {
        System.out.println("=======================Server: ");
        System.out.println("findConcursProbaCategorie");
        System.out.println("=======================");
        Optional<client.Domain_Hibernate.Concurs> concurs = concursDBRepository.findConcursProbaCategorie(proba, categorie);
        return Optional.of(HibernateUtils.convertConcurs_FromHibernate(concurs.get()));
    }
}
