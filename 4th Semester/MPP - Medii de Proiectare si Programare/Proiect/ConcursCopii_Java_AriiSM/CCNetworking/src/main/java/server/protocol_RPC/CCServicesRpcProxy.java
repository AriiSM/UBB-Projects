package server.protocol_RPC;

import client.Domain_Simplu.Concurs;
import client.Domain_Simplu.Inscriere;
import client.Domain_Simplu.Organizator;
import client.Domain_Simplu.Participant;
import server.CCException;
import server.ICCObserver;
import server.ICCServices;
import server.dto.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class CCServicesRpcProxy implements ICCServices {
    private final String host;
    private final int port;

    private ICCObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public CCServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }


    public Iterable<Participant> findAllParticipant() throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findAllParticipant");

        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.FIND_ALL_PARTICIPANTS).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        List<ParticipantDTO> participantDTOs = (List<ParticipantDTO>) response.data();
        System.out.println("=======================");
        return participantDTOs.stream().map(DTOUtils::getFromDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<Participant> saveParticipant(Participant entity) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("saveParticipant");
        //initializeConnection();
        ParticipantDTO participantDTO = DTOUtils.getDTO(entity);
        Request req = new Request.Builder().type(RequestType.SAVE_PARTICIPANT).data(participantDTO).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        ParticipantDTO resultDTO = (ParticipantDTO) response.data();
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    public List<Participant> filterProbaCategorieParticipant(String proba, String categorie) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("filterProbaCategorieParticipant");
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.FILTER_PROBA_CATEGORIE_PARTICIPANT).data(new Object[]{proba, categorie}).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        List<ParticipantDTO> participantDTOs = (List<ParticipantDTO>) response.data();
        System.out.println("=======================");
        return participantDTOs.stream().map(DTOUtils::getFromDTO).collect(Collectors.toList());
    }

    public Integer numarProbePentruParticipantParticipant(Integer id) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("filterProbaCategorieParticipant");
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.NUMAR_PROBE_PENTRU_PARTICIPANT).data(id).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        System.out.println("=======================");
        return (Integer) response.data();
    }

    public Optional<Participant> findParticipantNumePrenumeVarsta(String nume, String prenume, Integer varsta) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("filterProbaCategorieParticipant");
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.FIND_PARTICIPANT_NUME_PRENUME_VARSTA).data(new Object[]{nume, prenume, varsta}).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        if (response.type() == ResponseType.NEED_CREATE_PARTICIPANT) {
            Request createReq = new Request.Builder().type(RequestType.CREATE_AND_SAVE_PARTICIPANT).data(new Object[]{nume, prenume, varsta}).build();
            System.out.println("CREARE PARTICIPANT");

            sendRequest(createReq);
            Response createResponse = readResponse();

            if (createResponse.type() == ResponseType.OK) {
                ParticipantDTO participantDTO = (ParticipantDTO) createResponse.data();
                Participant participant = DTOUtils.getFromDTO(participantDTO);
                return Optional.of(participant);
            } else {
                String err = createResponse.data().toString();
                //closeConnection();
                throw new CCException(err);
            }
        }
        ParticipantDTO resultDTO = (ParticipantDTO) response.data();
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }



    public Organizator findAccountOrganizator(String parolaO, String numeO, String prenumeO) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findAccountOrganizator");
        initializeConnection();
        Organizator organizator = new Organizator(numeO, prenumeO, parolaO);
        OrganizatorDTO organizatorDTO = DTOUtils.getDTO(organizator);
        Request req = new Request.Builder().type(RequestType.FIND_ACCOUNT_ORGANIZATOR).data(organizatorDTO).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new CCException(err);
        }
        OrganizatorDTO resultDTO = (OrganizatorDTO) response.data();
        System.out.println("=======================");
        return DTOUtils.getFromDTO(resultDTO);
    }

    public void login(Organizator user, ICCObserver client) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("login");
        initializeConnection();
        OrganizatorDTO udto = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(udto).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new CCException(err);
        }
        System.out.println("=======================");
    }

    public void logout(Organizator user, ICCObserver client) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("logout");
        OrganizatorDTO udto = DTOUtils.getDTO(user);
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(udto).build();

        sendRequest(req);
        Response response = readResponse();

        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new CCException(err);
        }
        System.out.println("=======================");
    }

    @Override
    public Optional<Inscriere> findInscrierePersConc(Integer id_participant, Integer id_concurs) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findInscrierePersConc");
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.FIND_INSCRIERE_PERS_CONC).data(new Object[]{id_participant, id_concurs}).build();

        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        InscriereDTO resultDTO = (InscriereDTO) response.data();
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    public Optional<Inscriere> saveInscriere(Inscriere entity) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("saveInscriere");
        //initializeConnection();
        InscriereDTO inscriereDTO = DTOUtils.getDTO(entity);
        Request req = new Request.Builder().type(RequestType.SAVE_INSCRIERE).data(inscriereDTO).build();

        sendRequest(req);
        System.out.println("REQUEST INSCRIERE: " + req);
        Response response = readResponse();
        System.out.println("RESPONSE INSCRIERE: " + response);

        if (response.getType() == ResponseType.ERROR) {
            System.out.println("EROARE IN SAVE_INSCRIERE-PROXY: ");
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        InscriereDTO resultDTO = (InscriereDTO) response.data();
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    @Override
    public Optional<Concurs> saveConcurs(Concurs entity) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("saveConcurs");
        //initializeConnection();
        ConcursDTO concursDTO = DTOUtils.getDTO(entity);
        Request req = new Request.Builder().type(RequestType.SAVE_CONCURS).data(concursDTO).build();

        sendRequest(req);
        System.out.println("Request");
        System.out.println(req);

        Response response = readResponse();
        System.out.println("Raspuns");
        System.out.println(response);

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        ConcursDTO resultDTO = (ConcursDTO) response.data();
        System.out.println("=======================");
        return Optional.of(DTOUtils.getFromDTO(resultDTO));
    }

    public Optional<Concurs> findConcursProbaCategorie(String proba, String categorie) throws CCException {
        System.out.println("=======================Server-Proxy: ");
        System.out.println("findConcursProbaCategorie");
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.FIND_CONCURS_PROBA_CATEGORIE).data(new Object[]{proba, categorie}).build();

        sendRequest(req);
        System.out.println("Request");
        System.out.println(req);

        Response response = readResponse();
        System.out.println("Raspuns");
        System.out.println(response);

        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            //closeConnection();
            throw new CCException(err);
        }
        if (response.type() == ResponseType.NEED_CREATE_CONCURS) {
            System.out.println("NEED_CREATE_CONCURS");
            Request createReq = new Request.Builder().type(RequestType.CREATE_AND_SAVE_CONCURS).data(new Object[]{proba, categorie}).build();

            sendRequest(createReq);
            System.out.println("Request");
            System.out.println(createReq);

            Response createResponse = readResponse();
            System.out.println("Raspuns");
            System.out.println(createResponse);

            if (createResponse.type() == ResponseType.OK) {
                ConcursDTO concursDTO = (ConcursDTO) createResponse.data();
                Concurs concurs = new Concurs(concursDTO.getCategorie(), concursDTO.getProga());
                return Optional.of(concurs);
            } else {
                String err = createResponse.data().toString();
                //closeConnection();
                throw new CCException(err);
            }
        }
        ConcursDTO concursDTO = (ConcursDTO) response.data();
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

    private void sendRequest(Request request) throws CCException {
        try{
            output.writeObject(request);
            output.flush();
        }catch (IOException e){
            throw new CCException("Error sending object: " + e.toString());
        }
    }

    private Response readResponse(){
        Response response = null;
        try {
            response = qresponses.take();
            System.out.println("READ RESPONSE" + response);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection(){
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(Response response) {
        System.out.println("IN handleUpdate" + response);
        if(response.getType() == ResponseType.UPDATE){
            System.out.println("Inscriere Added UPDATING: ");
            try{
                client.inscriereReceives((Inscriere) response.getData());
            } catch (CCException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.UPDATE;
    }

    private class ReaderThread implements Runnable{
        @Override
        public void run() {
            while(!finished){
                try{
                    Object response = input.readObject();
                    System.out.println("Response recieved " + response);
                    if(isUpdate((Response) response)){
                        handleUpdate((Response) response);
                    }else{
                        try{
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    }
}