package mpp.Utils;

import mpp.IServices;
import mpp.ProtocolRPC.ClientWorker;

import java.net.Socket;

public class ConcurentServerRPC extends AbstractConcurentServer{
    private IServices server;

    public ConcurentServerRPC(int port, IServices server) {
        super(port);
        this.server = server;
        System.out.println("ConcurentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientWorker workerRpc = new ClientWorker(server, client);

        Thread tw = new Thread(workerRpc);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping Server ...");
    }
}
