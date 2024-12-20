package mpp.ProtocolRPC;

import mpp.*;
import mpp.Comunication.Request;
import mpp.Comunication.Response;
import mpp.Comunication.ResponseType;
import mpp.Exceptions.GenericException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ClientWorker implements Runnable, IObserver {
    private IServices service;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private volatile boolean connected;

    public ClientWorker(IServices service, Socket connection) {
        this.service = service;
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
            } catch (Exception e) {
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

    private void sendResponse(Response response) throws IOException {
        System.out.println("Sending response " + response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }


    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + request.getType();
        System.out.println(" HandleName " + handlerName);

        try {
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println(" Method " + handlerName + " invoked ");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return response;
    }

    private Response handleFIND_JUCATOR(Request request) {
        String username = (String) request.getData();
        Jucator jucator = null;
        try {
            jucator = service.findJucator(username);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        Response response = null;
        if (jucator != null) {
            response = new Response(ResponseType.OK, jucator);
        } else {
            response = new Response(ResponseType.ERROR, "Jucatorul nu exista");
        }


        return response;
    }

    private Response handleLOGIN(Request request) {
        String username = (String) request.getData();
        Jucator jucator = null;
        try {
            jucator = service.login(username, this);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        Response response = new Response(ResponseType.OK, jucator);

        return response;
    }

    private Response handleFIND_5_PAIRS(Request request) {
        String[] pairs = null;
        try {
            pairs = service.find5Pairs();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        Response response = null;
        if (pairs != null) {
            response = new Response(ResponseType.OK, pairs);
        } else {
            response = new Response(ResponseType.ERROR, pairs);
        }

        return response;
    }

    private Response handleSAVE_GAME(Request request) {
        Game game = (Game) request.getData();
        try {
            Game game1 = service.saveGame(game).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        Response response = null;
        if (game != null) {

            response = new Response(ResponseType.OK, game);
        } else {
            response = new Response(ResponseType.ERROR, game);
        }


        return response;
    }

    private Response handleFIND_ALL_JUCATORI_GAMES(Request request) {
        Iterable<JucatoriGames> jucatoriGames = null;
        try {
            jucatoriGames = service.findAllJucatoriGames();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        Response response = null;
        if (jucatoriGames != null) {
            response = new Response(ResponseType.OK, jucatoriGames);
        } else {
            response = new Response(ResponseType.ERROR, jucatoriGames);
        }


        return response;
    }

    private Response handleSAVE_JUCATOR_GAME(Request request) {
        JucatoriGames jucatoriGames = (JucatoriGames) request.getData();
        JucatoriGames jg = null;
        try {
            jg = service.saveJucatorGame(jucatoriGames).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        Response response = null;
        if (jg != null) {
            response = new Response(ResponseType.OK, jucatoriGames);
        } else {
            response = new Response(ResponseType.ERROR, jucatoriGames);
        }

        return response;
    }

    private Response handleSAVE_CONFIGURARE(Request request) {
        Configurare configurare = (Configurare) request.getData();
        Configurare c = null;
        try {
            c = service.saveConfigurare(configurare).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        Response response = null;
        if (c != null) {
            response = new Response(ResponseType.OK, configurare);
        } else {
            response = new Response(ResponseType.ERROR, configurare);
        }

        return response;
    }

    @Override
    public void gameFinished(JucatoriGames game) {
        Response response = new Response(ResponseType.UPDATE, game);
        System.out.println("Worker - gameFinished " + response);
        try {
            sendResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
