package client;

import client.gui.LoginControler;
import client.gui.PaginaPrincipalaControler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.ICCServices;
import server.protocol_Proto.CCServiceProxy_Proto;
import server.protocol_RPC.CCServicesRpcProxy;

import java.io.IOException;
import java.util.Properties;

public class StartClient_Proto extends Application {
    private static final int defaultChatPort = 55555;
    private static final String defaultServer = "localhost";


    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();

        try {
            clientProps.load(StartClient_RPC.class.getResourceAsStream("/chatclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatclient.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ICCServices server = new CCServiceProxy_Proto(serverIP, serverPort);


        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("view/login.fxml"));
        Parent root = loader.load();


        LoginControler ctrl =
                loader.getController();
        ctrl.setDbLogin(server, primaryStage);


        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("view/paginaPrincipala.fxml"));
        Parent croot = cloader.load();


        PaginaPrincipalaControler chatCtrl =
                cloader.getController();
        chatCtrl.setDbLogin(server,primaryStage);

        ctrl.setPController(chatCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("Autentificare");
        primaryStage.setScene(new Scene(root, 667, 600));
        primaryStage.show();
    }
}
