package mpp;

import mpp.Exceptions.GenericException;
import mpp.Repo.*;
import mpp.RepoORM.RepoConfigurareHibernate;
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
        RepoPlayer repoPlayer = new RepoPlayer(serverProps);
        RepoCuvinte repoCuvinte = new RepoCuvinte(serverProps);

        //JDBC DECLARATION
        RepoConfigurare repoConfigurare = new RepoConfigurare(serverProps);
        //HIBERNATE DECLARATION
        RepoConfigurareHibernate HIBERNATE = new RepoConfigurareHibernate();

        RepoGame repoGame = new RepoGame(serverProps, HIBERNATE);
        RepoJucatorGame repoJucatorGame = new RepoJucatorGame(serverProps, repoGame, repoPlayer);


        IServices servicesImplementation = new ServiceImplementation(repoPlayer, repoCuvinte, repoGame, repoJucatorGame, HIBERNATE);

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
