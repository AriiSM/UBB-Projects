package mpp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mpp.ProtocolRPC.ServiceProxy;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {

    IServices service;
    private static final int defaultChatPort = 55556;
    private static final String defaultServer = "localhost";


    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();

        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        service = new ServiceProxy(serverIP, serverPort);

        initView(stage);
    }

    public void initView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Login.fxml"));
        AnchorPane mainLayout = fxmlLoader.load();
        stage.setScene(new Scene(mainLayout));

        LoginController loginViewController = fxmlLoader.getController();

        FXMLLoader cloader = new FXMLLoader(getClass().getClassLoader().getResource("game.fxml"));
        Parent croot = cloader.load();
        MainController chatCtrl = cloader.getController();

        loginViewController.setService(service, stage, chatCtrl, cloader, croot);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
