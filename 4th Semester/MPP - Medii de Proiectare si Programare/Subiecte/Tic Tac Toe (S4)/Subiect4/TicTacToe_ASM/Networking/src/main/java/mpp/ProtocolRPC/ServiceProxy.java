package mpp.ProtocolRPC;

import mpp.*;
import mpp.Comunication.Request;
import mpp.Comunication.RequestType;
import mpp.Comunication.Response;
import mpp.Comunication.ResponseType;
import mpp.Exceptions.ExceptionType;
import mpp.Exceptions.GenericException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceProxy implements IServices {
    private String host;
    private int port;

    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Socket connection;
    private BlockingQueue<Response> qresponses;

    private volatile boolean finished;

    public ServiceProxy(String host, int port) {
        this.host = host;
        this.port = port;
        this.qresponses = new LinkedBlockingQueue<>();
    }

    private Response readResponse() throws GenericException {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void sendRequest(Request request) throws GenericException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new GenericException(ExceptionType.OTHER, "Error sending object: " + e.toString());
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private boolean isUpdate(Response response){
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

    private void handleUpdate(Response response) {
        if(response.getType() == ResponseType.UPDATE){
            System.out.println("New game finished UPDATING...");
            try{
                System.out.println("Client game finished " + response.getData());
                client.gameFinished((JucatorGame) response.getData());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    private void initializeConnection() throws GenericException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            ;
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Persoana login(String username, IObserver client) throws GenericException {
        initializeConnection();
        Request request = new Request(RequestType.LOGIN, username);
        sendRequest(request);
        Response response = readResponse();

        if (response.getType() == ResponseType.OK) {
            this.client = client;
            return (Persoana) response.getData();
        }
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getData().toString();
            closeConnection();

        }

        throw new GenericException(ExceptionType.OTHER, "Login Error");
    }

    @Override
    public Persoana findJucator(String username) throws GenericException {
        initializeConnection();
        Request resuqest = new Request(RequestType.FIND_JUCATOR, username);
        try {
            sendRequest(resuqest);
            Response response = readResponse();
            if (response.getType() == ResponseType.OK) {
                return (Persoana) response.getData();
            }
        } catch (GenericException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Configurare> saveConfiguratie(Configurare conf) throws GenericException {
        Request request = new Request(RequestType.SAVE_CONFIGURATIE, conf);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            return Optional.of((Configurare) response.getData());
        }
        return Optional.empty();
    }

    @Override
    public Optional<Game> saveGame(Game game) throws GenericException {
        Request request = new Request(RequestType.SAVE_GAME, game);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            return Optional.of((Game) response.getData());
        }
        return Optional.empty();
    }

    @Override
    public Optional<JucatorGame> saveJucatorGame(JucatorGame jucatorGame) throws GenericException {
        Request request = new Request(RequestType.SAVE_JUCATOR_GAME, jucatorGame);
        sendRequest(request);
        Response response = readResponse();
        if(response.getType() == ResponseType.OK){
            return Optional.of((JucatorGame) response.getData());
        }
        return Optional.empty();
    }

    @Override
    public Iterable<JucatorGame> findAllJucatoriGames() throws GenericException {
        Request request = new Request(RequestType.FIND_ALL_JUCATOR_GAMES, "get all jucatori games");
        sendRequest(request);
        Response response = readResponse();
        if (response.getType() == ResponseType.OK) {
            return (Iterable<JucatorGame>) response.getData();
        }
        return null;
    }
}
