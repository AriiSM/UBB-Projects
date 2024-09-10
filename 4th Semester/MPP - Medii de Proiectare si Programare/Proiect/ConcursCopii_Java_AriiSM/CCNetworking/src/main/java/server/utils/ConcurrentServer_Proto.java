package server.utils;

import server.ICCServices;
import server.protocol_Proto.CCClientWorker_Proto;

import java.net.Socket;

public class ConcurrentServer_Proto extends AbsConcurrentServer{
    private ICCServices service;

    public ConcurrentServer_Proto(int port, ICCServices service){
        super(port);
        this.service = service;
        System.out.println("ConcurrentServer_Proto");
    }

    @Override
    protected Thread createWorker(Socket client) {
        CCClientWorker_Proto workerProto = new CCClientWorker_Proto(service, client);

        Thread tw = new Thread(workerProto);
        return tw;
    }
}
