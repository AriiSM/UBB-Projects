package server.protocol_RPC;

import client.Domain_Simplu.*;
import server.CCException;
import server.ICCObserver;
import server.ICCServices;
import server.dto.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CCClientRpcWorker implements Runnable, ICCObserver {
    private final ICCServices server;
    private final Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public CCClientRpcWorker(ICCServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (CCException e) {
                throw new RuntimeException(e);
            }
        }
        closeConnection();
    }

    private void closeConnection() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e);
        }
    }

    public void inscriereReceives(Inscriere inscriere) throws CCException{
        Response response = new Response(ResponseType.UPDATE, inscriere);
        System.out.println("Inscriere added: " + inscriere);
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) throws CCException {
        Response response = null;
        if (request.type() == RequestType.LOGIN) {
            System.out.println("Login request ..." + request.type());

            OrganizatorDTO udto = (OrganizatorDTO) request.data();
            Organizator user = DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                return okResponse;
            } catch (CCException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            OrganizatorDTO udto = (OrganizatorDTO) request.data();
            Organizator user = DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected = false;
                return okResponse;

            } catch (CCException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.INSCRIERE_PARTICIPANT) {
            System.out.println("SendMessageRequest ...");
            InscriereDTO inscriereDTO = (InscriereDTO) request.data();
            Inscriere inscriere = DTOUtils.getFromDTO(inscriereDTO);
            return okResponse;
        }

        if (request.type() == RequestType.FIND_ACCOUNT_ORGANIZATOR) {
            System.out.println("Finding account ...");
            OrganizatorDTO organizatorDTO = (OrganizatorDTO) request.data();
            Organizator organizator = DTOUtils.getFromDTO(organizatorDTO);
            Organizator foundOrganizatorOptional = server.findAccountOrganizator(organizator.getParola(), organizator.getLastName(), organizator.getFirstName());
            if (foundOrganizatorOptional != null) {
                OrganizatorDTO foundOrganizatorDTO = DTOUtils.getDTO(foundOrganizatorOptional);
                response = new Response.Builder().type(ResponseType.OK).data(foundOrganizatorDTO).build();
                System.out.println("Found!");
            } else {
                response = new Response.Builder().type(ResponseType.ERROR).data("Organizator not found").build();
                System.out.println("Not found :(");
            }
        }

        if (request.type() == RequestType.FIND_PARTICIPANT_NUME_PRENUME_VARSTA) {
            System.out.println("Find participant request ...");
            Object[] data = (Object[]) request.data();
            String nume = (String) data[0];
            String prenume = (String) data[1];
            Integer varsta = (Integer) data[2];
            Optional<Participant> participant = server.findParticipantNumePrenumeVarsta(nume, prenume, varsta);
            if (participant.isPresent()) {
                ParticipantDTO participantDTO = DTOUtils.getDTO(participant.get());
                response = new Response.Builder().type(ResponseType.OK).data(participantDTO).build();
            } else {
                response = new Response.Builder().type(ResponseType.NEED_CREATE_PARTICIPANT).data("Participant not found. NEED TO CREATE ONE").build();
            }
        }

        if (request.type() == RequestType.CREATE_AND_SAVE_PARTICIPANT) {
            System.out.println("Create and save participant request ...");
            Object[] data = (Object[]) request.data();
            String nume = (String) data[0];
            String prenume = (String) data[1];
            Integer varsta = (Integer) data[2];
            Participant participant = new Participant(nume, prenume, varsta);
            Optional<Participant> savedParticipant = server.saveParticipant(participant);
            if (savedParticipant.isPresent()) {
                ParticipantDTO participantDTO = DTOUtils.getDTO(savedParticipant.get());
                response = new Response.Builder().type(ResponseType.OK).data(participantDTO).build();
            } else {
                response = new Response.Builder().type(ResponseType.ERROR).data("Could not save participant").build();
            }
        }

        if (request.type() == RequestType.SAVE_PARTICIPANT) {
            System.out.println("Save participant request ...");
            ParticipantDTO participantDTO = (ParticipantDTO) request.data();
            Participant participant = DTOUtils.getFromDTO(participantDTO);
            Optional<Participant> savedParticipant = server.saveParticipant(participant);
            if (savedParticipant.isPresent()) {
                ParticipantDTO savedParticipantDTO = DTOUtils.getDTO(savedParticipant.get());
                response = new Response.Builder().type(ResponseType.OK).data(savedParticipantDTO).build();
            } else {
                response = new Response.Builder().type(ResponseType.ERROR).data("Could not save participant").build();
            }
        }

        if (request.type() == RequestType.FIND_ALL_PARTICIPANTS) {
            System.out.println("Find all participants request ...");
            Iterable<Participant> participants = server.findAllParticipant();
            List<ParticipantDTO> participantDTOs = new ArrayList<>();
            for (Participant participant : participants) {
                participantDTOs.add(DTOUtils.getDTO(participant));
            }
            response = new Response.Builder().type(ResponseType.OK).data(participantDTOs).build();
        }


        if (request.type() == RequestType.FILTER_PROBA_CATEGORIE_PARTICIPANT) {
            System.out.println("Filter participants request ...");
            Object[] data = (Object[]) request.data();
            String proba = (String) data[0];
            String categorie = (String) data[1];
            List<Participant> participants = server.filterProbaCategorieParticipant(proba, categorie);
            List<ParticipantDTO> participantDTOs = new ArrayList<>();
            for (Participant participant : participants) {
                participantDTOs.add(DTOUtils.getDTO(participant));
            }
            response = new Response.Builder().type(ResponseType.OK).data(participantDTOs).build();
        }

        if (request.type() == RequestType.NUMAR_PROBE_PENTRU_PARTICIPANT) {
            System.out.println("Number of probes for participant request ...");
            Integer id = (Integer) request.data();
            Integer numarProbe = server.numarProbePentruParticipantParticipant(id);
            response = new Response.Builder().type(ResponseType.OK).data(numarProbe).build();
        }

        if (request.type() == RequestType.SAVE_INSCRIERE) {
            System.out.println("Save inscriere request ...");

            InscriereDTO inscriereDTO = (InscriereDTO) request.data();
            Inscriere inscriere = DTOUtils.getFromDTO(inscriereDTO);
            Optional<Inscriere> savedInscriere = server.saveInscriere(inscriere);
            if (savedInscriere.isPresent()) {
                InscriereDTO resultDTO = DTOUtils.getDTO(savedInscriere.get());
                response = new Response.Builder().type(ResponseType.SAVE_INSCRIERE).data(resultDTO).build();
            } else {
                response = new Response.Builder().type(ResponseType.ERROR).data("Inscriere not saved").build();
            }
        }

        if (request.type() == RequestType.FIND_CONCURS_PROBA_CATEGORIE) {
            System.out.println("Find concurs request ...");
            Object[] data = (Object[]) request.data();
            String proba = (String) data[0];
            String categorie = (String) data[1];
            Optional<Concurs> concurs = server.findConcursProbaCategorie(proba, categorie);
            if (concurs.isPresent()) {
                ConcursDTO concursDTO = DTOUtils.getDTO(concurs.get());
                response = new Response.Builder().type(ResponseType.OK).data(concursDTO).build();
            } else {
                response = new Response.Builder().type(ResponseType.NEED_CREATE_CONCURS).data("Concurs not found. NEED TO CREATE ONE").build();
            }
        }

        if (request.type() == RequestType.CREATE_AND_SAVE_CONCURS) {
            System.out.println("Create and save concurs request ...");
            Object[] data = (Object[]) request.data();
            String proba = (String) data[0];
            String categorie = (String) data[1];
            Concurs concurs = new Concurs(Categorie.valueOf(categorie), Proba.valueOf(proba));
            Optional<Concurs> savedConcurs = server.saveConcurs(concurs);
            if (savedConcurs.isPresent()) {
                ConcursDTO concursDTO = DTOUtils.getDTO(savedConcurs.get());
                response = new Response.Builder().type(ResponseType.OK).data(concursDTO).build();
            } else {
                response = new Response.Builder().type(ResponseType.ERROR).data("Could not save concurs").build();
            }
        }

        if (request.type() == RequestType.SAVE_CONCURS) {
            System.out.println("Save concurs request ...");
            ConcursDTO concursDTO = (ConcursDTO) request.data();
            Concurs concurs = DTOUtils.getFromDTO(concursDTO);
            Optional<Concurs> savedConcurs = server.saveConcurs(concurs);
            if (savedConcurs.isPresent()) {
                ConcursDTO savedConcursDTO = DTOUtils.getDTO(savedConcurs.get());
                response = new Response.Builder().type(ResponseType.OK).data(savedConcursDTO).build();
            } else {
                response = new Response.Builder().type(ResponseType.ERROR).data("Could not save concurs").build();
            }
        }


        if (request.type() == RequestType.FIND_INSCRIERE_PERS_CONC) {
            System.out.println("FindInscrierePersConc request ...");
            Object[] data = (Object[]) request.data();
            Integer id_participant = (Integer) data[0];
            Integer id_concurs = (Integer) data[1];
            try {
                Optional<Inscriere> inscriere = server.findInscrierePersConc(id_participant, id_concurs);
                if (inscriere.isPresent()) {
                    InscriereDTO inscriereDTO = DTOUtils.getDTO(inscriere.get());
                    return new Response.Builder().type(ResponseType.OK).data(inscriereDTO).build();
                } else {
                    return new Response.Builder().type(ResponseType.ERROR).data("Inscriere not found").build();
                }
            } catch (CCException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }
}