package server.protocol_Proto;

import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Inscriere;
import client.Domain_Simplu.Organizator;
import client.Domain_Simplu.Participant;
import server.CCException;
import server.ICCObserver;
import server.ICCServices;
import server.dto.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class CCServiceProxy_Proto implements ICCServices {

    private final String host;
    private final int port;

    private ICCObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<Protobuf.Response> qresponses;
    private volatile boolean finished;

    public CCServiceProxy_Proto(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Protobuf.Response>();
    }

    @Override
    public Iterable<Participant> findAllParticipant() throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findAllParticipant");

        //initializeConnection();
        //Request req = new Request.Builder().type(RequestType.FIND_ALL_PARTICIPANTS).build();
        Protobuf.Request req = Utils_Proto.findAllParticipant();
        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        List<ParticipantDTO> participantDTOs = Utils_Proto.getParticipantsResponse(response);
        List<Participant> participants = participantDTOs.stream()
                .map(DTOUtils::getFromDTO)
                .collect(Collectors.toList());
        System.out.println("=======================");
        return participants;
    }

    @Override
    public Optional<Participant> saveParticipant(Participant entity) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("saveParticipant");
        //initializeConnection();
        ParticipantDTO participantDTO = DTOUtils.getDTO(entity);
        //Request req = new Request.Builder().type(RequestType.SAVE_PARTICIPANT).data(participantDTO).build();
        Protobuf.Request req = Utils_Proto.saveParticipantRequest(participantDTO);

        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        ParticipantDTO resultDTO = Utils_Proto.getParticipantResponse(response);
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    @Override
    public List<Participant> filterProbaCategorieParticipant(String proba, String categorie) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("filterProbaCategorieParticipant");
        //initializeConnection();
        //Request req = new Request.Builder().type(RequestType.FILTER_PROBA_CATEGORIE_PARTICIPANT).data(new Object[]{proba, categorie}).build();

        Protobuf.Request req = Utils_Proto.filterProbaCateforieParticipantRequest(proba, categorie);

        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        List<ParticipantDTO> participantDTOs = Utils_Proto.getParticipantsResponse(response);
        List<Participant> participants = participantDTOs.stream()
                .map(Utils_Proto::convertParticipantDTO_ToParticipant)
                .collect(Collectors.toList());
        System.out.println("=======================");
        return participants;
    }

    @Override
    public Integer numarProbePentruParticipantParticipant(Integer id) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("filterProbaCategorieParticipant");
        //initializeConnection();
        //Request req = new Request.Builder().type(RequestType.NUMAR_PROBE_PENTRU_PARTICIPANT).data(id).build();
        Protobuf.Request req = Utils_Proto.numarProbePentruParticipantParticipantRequest(id);

        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        System.out.println("=======================");
        return Utils_Proto.getNumarProbePentruParticipantResponse(response);
    }

    @Override
    public Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("filterProbaCategorieParticipant");
        //initializeConnection();
        //Request req = new Request.Builder().type(RequestType.FIND_PARTICIPANT_NUME_PRENUME_VARSTA).data(new Object[]{nume, prenume, varsta}).build();
        Protobuf.Request req = Utils_Proto.findParticipantNumePrenumeVarstaRequest(nume, prenume, varsta);

        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        if (response.getType() == Protobuf.Response.ResponseType.NEED_CREATE_PARTICIPANT) {
            //Request createReq = new Request.Builder().type(RequestType.CREATE_AND_SAVE_PARTICIPANT).data(new Object[]{nume, prenume, varsta}).build();
            Protobuf.Request createReq = Utils_Proto.needCreateParticipantRequest(nume, prenume, varsta);
            System.out.println("CREARE PARTICIPANT");

            sendRequest(createReq);
            Protobuf.Response createResponse = readResponse();

            if (createResponse.getType() == Protobuf.Response.ResponseType.OK) {
                ParticipantDTO participantDTO = Utils_Proto.need_create_participantResponse(createResponse);
                Participant participant = DTOUtils.getFromDTO(participantDTO);
                return Optional.of(participant);
            } else {
                String err = createResponse.getMessage();
                //closeConnection();
                throw new CCException(err);
            }
        }
        ParticipantDTO resultDTO = Utils_Proto.need_create_participantResponse(response);
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    @Override
    public Organizator findAccountOrganizator(String parolaO, String numeO, String prenumeO) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findAccountOrganizator");
        initializeConnection();
        Organizator organizator = new Organizator(numeO, prenumeO, parolaO);
        OrganizatorDTO organizatorDTO = DTOUtils.getDTO(organizator);
        //Request req = new Request.Builder().type(RequestType.FIND_ACCOUNT_ORGANIZATOR).data(organizatorDTO).build();
        Protobuf.Request req = Utils_Proto.findAccountOrganizatorRequest(organizatorDTO);

        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            throw new CCException(err);
        }
        OrganizatorDTO resultDTO = Utils_Proto.findAccountOrganizator(response);
        System.out.println("=======================");
        return DTOUtils.getFromDTO(resultDTO);
    }

    @Override
    public void login(Organizator user, ICCObserver client) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("login");
        initializeConnection();
        OrganizatorDTO udto = DTOUtils.getDTO(user);
        //Request req = new Request.Builder().type(RequestType.LOGIN).data(udto).build();
        Protobuf.Request req = Utils_Proto.loginRequest(udto);
        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            closeConnection();
            throw new CCException(err);
        }
        System.out.println("=======================");
    }

    @Override
    public void logout(Organizator user, ICCObserver client) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("logout");
        OrganizatorDTO udto = DTOUtils.getDTO(user);
        //Request req = new Request.Builder().type(RequestType.LOGOUT).data(udto).build();
        Protobuf.Request req = Utils_Proto.logoutRequest(udto);
        sendRequest(req);
        Protobuf.Response response = readResponse();

        closeConnection();
        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            throw new CCException(err);
        }
        System.out.println("=======================");
    }

    @Override
    public Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findInscrierePersConc");
        //initializeConnection();
        //Request req = new Request.Builder().type(RequestType.FIND_INSCRIERE_PERS_CONC).data(new Object[]{id_participant, id_concurs}).build();
        Protobuf.Request req = Utils_Proto.findInscrierePersConcRequest(id_participant, id_concurs);
        sendRequest(req);
        Protobuf.Response response = readResponse();

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        InscriereDTO resultDTO = Utils_Proto.findInscrierePersConc(response);
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    @Override
    public Optional<Inscriere> saveInscriere(Inscriere entity) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("saveInscriere");
        //initializeConnection();
        InscriereDTO inscriereDTO = DTOUtils.getDTO(entity);
        //Request req = new Request.Builder().type(RequestType.SAVE_INSCRIERE).data(inscriereDTO).build();
        Protobuf.Request req = Utils_Proto.saveInscriereRequest(inscriereDTO);

        sendRequest(req);
        System.out.println("REQUEST INSCRIERE: " + req);
        //TODO : AICI PUSCA
        Protobuf.Response response = readResponse();
        System.out.println("RESPONSE INSCRIERE: " + response);

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            System.out.println("EROARE IN SAVE_INSCRIERE-PROXY: ");
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        InscriereDTO resultDTO = Utils_Proto.saveInscriere(response);
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    @Override
    public Optional<Concurs> saveConcurs(Concurs entity) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("saveConcurs");
        //initializeConnection();
        ConcursDTO concursDTO = DTOUtils.getDTO(entity);
        //Request req = new Request.Builder().type(RequestType.SAVE_CONCURS).data(concursDTO).build();
        Protobuf.Request req = Utils_Proto.saveConcursRequest(concursDTO);

        sendRequest(req);
        System.out.println("Request");
        System.out.println(req);

        Protobuf.Response response = readResponse();
        System.out.println("Raspuns");
        System.out.println(response);

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        ConcursDTO resultDTO = Utils_Proto.saveConcursResponse(response);
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    @Override
    public Optional<Concurs> findConcursProbaCategorie(String proba, String categorie) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findConcursProbaCategorie");
        //initializeConnection();
        //Request req = new Request.Builder().type(RequestType.FIND_CONCURS_PROBA_CATEGORIE).data(new Object[]{proba, categorie}).build();
        Protobuf.Request req = Utils_Proto.findConcursProbaCategorie(proba, categorie);

        sendRequest(req);
        System.out.println("Request");
        System.out.println(req);

        Protobuf.Response response = readResponse();
        System.out.println("Raspuns");
        System.out.println(response);

        if (response.getType() == Protobuf.Response.ResponseType.ERROR) {
            String err = response.getMessage();
            //closeConnection();
            throw new CCException(err);
        }
        if (response.getType() == Protobuf.Response.ResponseType.NEED_CREATE_CONCURS) {
            System.out.println("NEED_CREATE_CONCURS");
            //Request createReq = new Request.Builder().type(RequestType.CREATE_AND_SAVE_CONCURS).data(new Object[]{proba, categorie}).build();
            Protobuf.Request createReq = Utils_Proto.createAnSaveConcursRequest(proba, categorie);

            sendRequest(createReq);
            System.out.println("Request");
            System.out.println(createReq);

            Protobuf.Response createResponse = readResponse();
            System.out.println("Raspuns");
            System.out.println(createResponse);

            if (createResponse.getType() == Protobuf.Response.ResponseType.OK) {
                ConcursDTO concursDTO = Utils_Proto.saveConcursResponse(createResponse);
                Concurs concurs = new Concurs(concursDTO.getCategorie(), concursDTO.getProga());
                return Optional.of(concurs);
            } else {
                String err = createResponse.getMessage();
                //closeConnection();
                throw new CCException(err);
            }
        }
        ConcursDTO concursDTO = Utils_Proto.saveConcursResponse(response);
        Concurs concurs = DTOUtils.getFromDTO(concursDTO);
        return Optional.of(concurs);
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Protobuf.Request request) throws CCException{
        try{
            System.out.println("Sending Request...");
            request.writeDelimitedTo(output);
            output.flush();
//            output.writeObject(request);
//            output.flush();
        }catch (IOException e){
                throw new CCException("Error sending object: " + e.toString());
        }
    }
//    private void sendRequest(Protobuf.Request request) throws CCException {
//        final int MAX_RETRIES = 3;
//        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
//            try {
//                System.out.println("Sending Request...");
//                request.writeDelimitedTo(output);
//                output.flush();
//                return; // if successful, exit the method
//            } catch (IOException e) {
//                System.err.println("Failed to send request: " + e.toString());
//                if (attempt == MAX_RETRIES - 1) { // if last attempt
//                    throw new CCException("Error sending object: " + e.toString());
//                } else {
//                    System.out.println("Retrying to send request...");
//                    // Here you might want to add some delay before retrying, e.g. Thread.sleep(1000);
//                }
//            }
//        }
//    }

    private Protobuf.Response readResponse() {
        Protobuf.Response response = null;
        try {
            response = qresponses.take();
            System.out.println("READ RESPONSE" + response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = connection.getOutputStream();
//            output.flush();
            input = connection.getInputStream();
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new CCServiceProxy_Proto.ReaderThread());
        tw.start();
    }


    private void handleUpdate(Protobuf.Response response) {
        System.out.println("IN handleUpdate" + response);
        if (response.getType() == Protobuf.Response.ResponseType.UPDATE) {
            System.out.println("Inscriere Added UPDATING: ");
            try {
                client.inscriereReceives(Utils_Proto.saveInscriereResponse(response));
            } catch (CCException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isUpdate(Protobuf.Response response) {
        return response.getType() == Protobuf.Response.ResponseType.UPDATE;
    }

    private class ReaderThread implements Runnable{
        @Override
        public void run() {
            while(!finished){
                try{
                    Protobuf.Response response = Protobuf.Response.parseDelimitedFrom(input);
                    System.out.println("Response recieved " + response.getType());
                    if(isUpdate(response)){
                        handleUpdate(response);
                    }else{
                        try{
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
