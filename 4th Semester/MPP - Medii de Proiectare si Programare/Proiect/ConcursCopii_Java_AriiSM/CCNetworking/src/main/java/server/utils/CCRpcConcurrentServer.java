package server.utils;

import server.ICCServices;
import server.protocol_RPC.CCClientRpcWorker;

import java.net.Socket;

public class CCRpcConcurrentServer extends AbsConcurrentServer{
    private ICCServices ccServer;
    public CCRpcConcurrentServer(int port, ICCServices ccServer) {
        super(port);
        this.ccServer = ccServer;
        System.out.println("ConcurentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        CCClientRpcWorker worker=new CCClientRpcWorker(ccServer, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
