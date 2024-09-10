//package server.rpcprotocol;
//
//import client.Domain_Simplu.Inscriere;
//import client.Domain_Simplu.Organizator;
//import client.Domain_Simplu.Participant;
//import server.CCException;
//import server.ICCObserver;
//import server.ICCServices;
//import server.dto.DTOUtils;
//import server.dto.InscriereDTO;
//import server.dto.OrganizatorDTO;
//import server.dto.ParticipantDTO;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.net.Socket;
//import java.util.Optional;
//
//public class CCClientRpcReflectionWorker implements Runnable, ICCObserver {
//    private ICCServices server;
//    private Socket connection;
//
//    private ObjectInputStream input;
//    private ObjectOutputStream output;
//    private volatile boolean connected;
//    public CCClientRpcReflectionWorker(ICCServices server, Socket connection) {
//        this.server = server;
//        this.connection = connection;
//        try{
//            output=new ObjectOutputStream(connection.getOutputStream());
//            output.flush();
//            input=new ObjectInputStream(connection.getInputStream());
//            connected=true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void run() {
//        while(connected){
//            try {
//                Object request=input.readObject();
//                Response response=handleRequest((Request)request);
//                if (response!=null){
//                    sendResponse(response);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            input.close();
//            output.close();
//            connection.close();
//        } catch (IOException e) {
//            System.out.println("Error "+e);
//        }
//    }
//
//    public void inscriereReceives(Inscriere inscriere) throws CCException {
//        InscriereDTO mdto= DTOUtils.getDTO(inscriere);
//        Response resp=new Response.Builder().type(ResponseType.SAVE_INSCRIERE).data(mdto).build();
//        System.out.println("Message received  "+inscriere);
//        try {
//            sendResponse(resp);
//        } catch (IOException e) {
//            throw new CCException("Sending error: "+e);
//        }
//    }
//
//
//    private static Response okResponse = new Response.Builder().type(ResponseType.OK).build();
//
//    private Response handleRequest(Request request){
//        Response response=null;
//        String handlerName="handle"+(request).type();
//        System.out.println("HandlerName "+handlerName);
//        try {
//            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
//            response=(Response)method.invoke(this,request);
//            System.out.println("Method "+handlerName+ " invoked");
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        return response;
//    }
//
//
//    private Response handleLOGIN(Request request){
//        System.out.println("Login request ..."+request.type());
//        OrganizatorDTO udto=(OrganizatorDTO)request.data();
//        Organizator user=DTOUtils.getFromDTO(udto);
//        try {
//            server.login(user, this);
//            return okResponse;
//        } catch (CCException e) {
//            connected=false;
//            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
//        }
//    }
//
//    private Response handleLOGOUT(Request request){
//        System.out.println("Logout request...");
//        OrganizatorDTO udto=(OrganizatorDTO)request.data();
//        Organizator user=DTOUtils.getFromDTO(udto);
//        try {
//            server.logout(user, this);
//            connected=false;
//            return okResponse;
//
//        } catch (CCException e) {
//            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
//        }
//    }
//
////    private Response handleINSCRIERE_PARTICIPANT(Request request){
////        System.out.println("SendMessageRequest ...");
////        InscriereDTO inscriereDTO=(InscriereDTO)request.data();
////        Inscriere inscriere= DTOUtils.getFromDTO(inscriereDTO);
////        try {
////            return okResponse;
////        } catch (CCException e) {
////            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
////        }
////    }
//
//    private Response handleFIND_ACCOUNT_ORGANIZATOR(Request request) throws CCException {
//        System.out.println("Find Account Organizator request...");
//        OrganizatorDTO organizatorDTO = (OrganizatorDTO) request.data();
//        Organizator organizator = DTOUtils.getFromDTO(organizatorDTO);
//        Organizator foundOrganizatorOptional = server.findAccountOrganizator(organizator.getParola(),organizator.getLastName(),organizator.getFirstName());
//        if (foundOrganizatorOptional != null) {
//            OrganizatorDTO foundOrganizatorDTO = DTOUtils.getDTO(foundOrganizatorOptional);
//            return new Response.Builder().type(ResponseType.OK).data(foundOrganizatorDTO).build();
//        } else {
//            return new Response.Builder().type(ResponseType.ERROR).data("Organizator not found").build();
//        }
//    }
//
//    private Response handleFIND_PARTICIPANT_NUME_PRENUME_VARSTA(Request request) throws CCException {
//        System.out.println("Find participant request ...");
//        Object[] data = (Object[]) request.data();
//        String nume = (String) data[0];
//        String prenume = (String) data[1];
//        Integer varsta = (Integer) data[2];
//        Optional<Participant> participant = server.findParticipantNumePrenumeVarsta(nume, prenume, varsta);
//        Response response;
//        if (participant.isPresent()) {
//            ParticipantDTO participantDTO = DTOUtils.getDTO(participant.get());
//            response = new Response.Builder().type(ResponseType.OK).data(participantDTO).build();
//        } else {
//            response = new Response.Builder().type(ResponseType.ERROR).data("Participant not found").build();
//        }
//        return response;
//    }
//
//    private void sendResponse(Response response) throws IOException{
//        System.out.println("sending response "+response);
//        synchronized (output) {
//            output.writeObject(response);
//            output.flush();
//        }
//    }
//}