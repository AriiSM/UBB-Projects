package server.protocol_Proto;

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

public class CCClientWorker_Proto implements Runnable, ICCObserver {
    private final ICCServices server;
    private final Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public CCClientWorker_Proto(ICCServices server, Socket connection) {
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
                //Object request = input.readObject();
                Protobuf.Request request = Protobuf.Request.parseDelimitedFrom(input);
                Protobuf.Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
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

    @Override
    public void inscriereReceives(Inscriere inscriere) throws CCException {
       Protobuf.Response response = Utils_Proto.updateInscriere(inscriere);
        System.out.println("Inscriere added: " + inscriere);
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Protobuf.Response handleRequest(Protobuf.Request request) throws CCException {
        Protobuf.Response response = null;
        if (request.getType() == Protobuf.Request.RequestType.LOGIN) {
            System.out.println("Login request ..." + request.getType());

            OrganizatorDTO udto = Utils_Proto.convertOrganizatorPB_ToOrganizatorDTO(request.getOrg());
            Organizator user = DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.OK).build();
                return response;
            } catch (CCException e) {
                connected = false;
                return Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage(e.getMessage()).build();
            }
        }
        if (request.getType() == Protobuf.Request.RequestType.LOGOUT) {
            System.out.println("Logout request");
            // LogoutRequest logReq=(LogoutRequest)request;
            OrganizatorDTO udto = Utils_Proto.convertOrganizatorPB_ToOrganizatorDTO(request.getOrg());
            Organizator user = DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected = false;
                return Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.OK).build();

            } catch (CCException e) {
                return Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage(e.getMessage()).build();
            }
        }
        if (request.getType() == Protobuf.Request.RequestType.INSCRIERE_PARTICIPANT) {
            System.out.println("SendMessageRequest ...");
            InscriereDTO inscriereDTO = Utils_Proto.convertInscrierePB_toInscriereDTO(request.getInscriereFull());
            Inscriere inscriere = DTOUtils.getFromDTO(inscriereDTO);
            return Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.OK).build();


        }

        if (request.getType() == Protobuf.Request.RequestType.FIND_ACCOUNT_ORGANIZATOR) {
            System.out.println("Finding account ...");
            OrganizatorDTO organizatorDTO = Utils_Proto.convertOrganizatorPB_ToOrganizatorDTO(request.getOrg());
            Organizator organizator = DTOUtils.getFromDTO(organizatorDTO);
            Organizator foundOrganizatorOptional = server.findAccountOrganizator(organizator.getParola(), organizator.getLastName(), organizator.getFirstName());
            if (foundOrganizatorOptional != null) {
                OrganizatorDTO foundOrganizatorDTO = DTOUtils.getDTO(foundOrganizatorOptional);
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.OK)
                        .setOrg(Utils_Proto.convertOrganizatorDTO_ToProto(foundOrganizatorDTO))
                        .build();
                System.out.println("Found!");
            } else {

                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage("Organizator not found").build();
                System.out.println("Not found :(");
            }
        }

        if (request.getType() == Protobuf.Request.RequestType.FIND_PARTICIPANT_NUME_PRENUME_VARSTA) {
            System.out.println("Find participant request ...");
            List<String> data = Utils_Proto.convertParticipantFaraIdPB_ToList(request.getParticipantFaraId());
            String nume = data.get(0);
            String prenume = data.get(1);
            Integer varsta = Integer.valueOf(data.get(2));
            Optional<Participant> participant = server.findParticipantNumePrenumeVarsta(nume, prenume, varsta);
            if (participant.isPresent()) {
                ParticipantDTO participantDTO = DTOUtils.getDTO(participant.get());
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.OK)
                        .setParticipant(Utils_Proto.convertParticipantDTO_ToProto(participantDTO))
                        .build();
            } else {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.NEED_CREATE_PARTICIPANT).setMessage("Participant not found. NEED TO CREATE ONE").build();
            }
        }

        if (request.getType() == Protobuf.Request.RequestType.CREATE_AND_SAVE_PARTICIPANT) {
            System.out.println("Create and save participant request ...");
            List<String> data = Utils_Proto.convertParticipantFaraIdPB_ToList(request.getParticipantFaraId());
            String nume = data.get(0);
            String prenume = data.get(1);
            Integer varsta = Integer.valueOf(data.get(2));
            Participant participant = new Participant(nume, prenume, varsta);
            Optional<Participant> savedParticipant = server.saveParticipant(participant);
            if (savedParticipant.isPresent()) {
                ParticipantDTO participantDTO = DTOUtils.getDTO(savedParticipant.get());
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.OK)
                        .setParticipant(Utils_Proto.convertParticipantDTO_ToProto(participantDTO))
                        .build();
            } else {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage("Could not save participant").build();
            }
        }

        if (request.getType() == Protobuf.Request.RequestType.SAVE_PARTICIPANT) {
            System.out.println("Save participant request ...");
            ParticipantDTO participantDTO = Utils_Proto.convertParticipantPB_ToParticipantDTO(request.getParticipant());
            Participant participant = DTOUtils.getFromDTO(participantDTO);
            Optional<Participant> savedParticipant = server.saveParticipant(participant);
            if (savedParticipant.isPresent()) {
                ParticipantDTO savedParticipantDTO = DTOUtils.getDTO(savedParticipant.get());
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.OK)
                        .setParticipant(Utils_Proto.convertParticipantDTO_ToProto(savedParticipantDTO))
                        .build();
            } else {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage("Could not save participant").build();
            }
        }

        if (request.getType() == Protobuf.Request.RequestType.FIND_ALL_PARTICIPANTS) {
            System.out.println("Find all participants request ...");
            Iterable<Participant> participants = server.findAllParticipant();
            List<ParticipantDTO> participantDTOs = new ArrayList<>();
            for (Participant participant : participants) {
                participantDTOs.add(DTOUtils.getDTO(participant));
            }
            return Utils_Proto.FIND_ALL_PARTICIPANTS(participantDTOs);
        }


        if (request.getType() == Protobuf.Request.RequestType.FILTER_PROBA_CATEGORIE_PARTICIPANT) {
            System.out.println("Filter participants request ...");
            List<String> data = Utils_Proto.convertConcursFaraIdPB_ToList(request.getConcursFaraId());
            String proba = data.get(0);
            String categorie = data.get(1);
            List<Participant> participants = server.filterProbaCategorieParticipant(proba, categorie);
            List<ParticipantDTO> participantDTOs = new ArrayList<>();
            for (Participant participant : participants) {
                participantDTOs.add(DTOUtils.getDTO(participant));
            }
            return Utils_Proto.FIND_ALL_PARTICIPANTS(participantDTOs);
        }

        if (request.getType() == Protobuf.Request.RequestType.NUMAR_PROBE_PENTRU_PARTICIPANT) {
            System.out.println("Number of probes for participant request ...");
            Integer id = request.getNumarProbePentruParticipanti();
            Integer numarProbe = server.numarProbePentruParticipantParticipant(id);
            response = Protobuf.Response.newBuilder()
                    .setType(Protobuf.Response.ResponseType.OK)
                    .setNumarProbePentruParticipanti(numarProbe)
                    .build();
        }

        if (request.getType() == Protobuf.Request.RequestType.SAVE_INSCRIERE) {
            System.out.println("Save inscriere request ...");

            InscriereDTO inscriereDTO = Utils_Proto.convertInscrierePB_toInscriereDTO(request.getInscriereFull());

            Inscriere inscriere = DTOUtils.getFromDTO(inscriereDTO);
            Optional<Inscriere> savedInscriere = server.saveInscriere(inscriere);
            if (savedInscriere.isPresent()) {
                InscriereDTO resultDTO = DTOUtils.getDTO(savedInscriere.get());
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.SAVE_INSCRIERE)
                        .setInscriereFull(Utils_Proto.convertInscriereDTO_ToInscriereFullPB(resultDTO))
                        .build();

            } else {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage("Inscriere not saved").build();
            }
        }

        if (request.getType() == Protobuf.Request.RequestType.FIND_CONCURS_PROBA_CATEGORIE) {
            System.out.println("Find concurs request ...");
            List<String> data = Utils_Proto.convertConcursFaraIdPB_ToList(request.getConcursFaraId());
            String proba = data.get(0);
            String categorie = data.get(1);
            Optional<Concurs> concurs = server.findConcursProbaCategorie(proba, categorie);
            if (concurs.isPresent()) {
                ConcursDTO concursDTO = DTOUtils.getDTO(concurs.get());
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.OK)
                        .setConcurs(Utils_Proto.convertConcursDTO_ToProto(concursDTO))
                        .build();
            } else {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.NEED_CREATE_CONCURS).build();
            }
        }

        if (request.getType() == Protobuf.Request.RequestType.CREATE_AND_SAVE_CONCURS) {
            System.out.println("Create and save concurs request ...");
            List<String> data = Utils_Proto.convertConcursFaraIdPB_ToList(request.getConcursFaraId());
            String proba = data.get(0);
            String categorie = data.get(1);
            Concurs concurs = new Concurs(Categorie.valueOf(categorie), Proba.valueOf(proba));
            Optional<Concurs> savedConcurs = server.saveConcurs(concurs);
            if (savedConcurs.isPresent()) {
                ConcursDTO concursDTO = DTOUtils.getDTO(savedConcurs.get());
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.OK)
                        .setConcurs(Utils_Proto.convertConcursDTO_ToProto(concursDTO)) // Convertim ConcursDTO la formatul protobuf
                        .build();
            } else {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage("Could not save concurs").build();
            }
        }

        if (request.getType() == Protobuf.Request.RequestType.SAVE_CONCURS) {
            System.out.println("Save concurs request ...");
            ConcursDTO concursDTO = Utils_Proto.convertConcursPB_ToConcursDTO(request.getConcurs());
            Concurs concurs = DTOUtils.getFromDTO(concursDTO);
            Optional<Concurs> savedConcurs = server.saveConcurs(concurs);
            if (savedConcurs.isPresent()) {
                ConcursDTO savedConcursDTO = DTOUtils.getDTO(savedConcurs.get());
                response = Protobuf.Response.newBuilder()
                        .setType(Protobuf.Response.ResponseType.OK)
                        .setConcurs(Utils_Proto.convertConcursDTO_ToProto(savedConcursDTO)) // Convertim ConcursDTO la formatul protobuf
                        .build();
            } else {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage("Could not save concurs").build();
            }
        }


        if (request.getType() == Protobuf.Request.RequestType.FIND_INSCRIERE_PERS_CONC) {
            System.out.println("FindInscrierePersConc request ...");
            List<String> data = Utils_Proto.convertInscriereFaraId_ToList(request.getInscriereFaraId());
            Integer id_participant = Integer.valueOf(data.get(0));
            Integer id_concurs = Integer.valueOf(data.get(1));
            try {
                Optional<Inscriere> inscriere = server.findInscrierePersConc(id_participant, id_concurs);
                if (inscriere.isPresent()) {
                    InscriereDTO inscriereDTO = DTOUtils.getDTO(inscriere.get());
                    response = Protobuf.Response.newBuilder()
                            .setType(Protobuf.Response.ResponseType.OK)
                            .setInscriereFull(Utils_Proto.convertInscriereDTO_ToInscriereFullPB1(inscriereDTO)) // Convertim ConcursDTO la formatul protobuf
                            .build();
                } else {
                    response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).setMessage("Inscriere not found").build();
                }
            } catch (CCException e) {
                response = Protobuf.Response.newBuilder().setType(Protobuf.Response.ResponseType.ERROR).build();
            }
        }
        return response;
    }

    private void sendResponse(Protobuf.Response response) throws IOException {
        System.out.println("sending response " + response);
        synchronized (output) {
            response.writeDelimitedTo(output);
            output.flush();
        }
    }
}
