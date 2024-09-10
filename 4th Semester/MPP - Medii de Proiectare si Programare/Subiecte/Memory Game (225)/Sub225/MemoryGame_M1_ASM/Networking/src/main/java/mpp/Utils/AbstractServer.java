package mpp.Utils;

import mpp.Exceptions.ExceptionType;
import mpp.Exceptions.GenericException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket server = null;

    public AbstractServer(int port) {this.port=port;}

    public void start() throws GenericException {
        try{
            server = new ServerSocket(port);
            while(true) {
                System.out.println("Waiting for clients...");
                Socket client = server.accept();
                System.out.println("Cleint connected...");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new GenericException(ExceptionType.SERVER_EXCEPTION, "Starting server error " + e);
        }
    }

    protected abstract void processRequest(Socket client);

    public void stop() throws GenericException{
        try{
            server.close();
        }catch (IOException e){
            throw new GenericException(ExceptionType.SERVER_EXCEPTION, "Closing server error " + e);
        }
    }
}
