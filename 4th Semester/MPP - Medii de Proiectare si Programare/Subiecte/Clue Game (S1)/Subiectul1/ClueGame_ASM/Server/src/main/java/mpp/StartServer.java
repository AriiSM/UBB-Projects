package mpp;

import mpp.Exceptions.GenericException;
import mpp.Repo.RepoConfiguratie;
import mpp.Repo.RepoGame;
import mpp.Repo.RepoJucatorGame;
import mpp.Repo.RepoPlayer;
import mpp.RepoORM.RepoConfiguratieHibernate;
import mpp.Utils.AbstractServer;
import mpp.Utils.ConcurentServerRPC;

import java.io.IOException;
import java.util.Properties;

public class StartServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties " + e);
            return;
        }
        RepoPlayer playerRepo = new RepoPlayer(serverProps);
        //JDBC DECLARATION
        RepoConfiguratie configuratieRepo = new RepoConfiguratie(serverProps);
        //HIBERNATE DECLARATION
        RepoConfiguratieHibernate HIBERNATE = new RepoConfiguratieHibernate();


        RepoGame gameRepo = new RepoGame(serverProps, playerRepo, HIBERNATE);
        RepoJucatorGame jucatorGameRepo = new RepoJucatorGame(serverProps, playerRepo, gameRepo);



        IServices servicesImplementation = new ServiceImplementation(playerRepo, configuratieRepo, gameRepo, jucatorGameRepo);


        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new ConcurentServerRPC(serverPort, servicesImplementation);
        try {
            server.start();
        } catch (GenericException e) {
            System.err.println("Error starting server " + e.getMessage());
        } finally {
            try {
                server.stop();
            } catch (GenericException e) {
                System.err.println("Error stopping server " + e.getMessage());
            }
        }
    }
}
