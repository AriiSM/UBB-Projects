package com.example.zboruri.starter;

import com.example.zboruri.contoler.ControlerLogin;
import com.example.zboruri.repository.DbClient_REPO;
import com.example.zboruri.repository.DbFlight_REPO;
import com.example.zboruri.repository.DbTiket_REPO;
import com.example.zboruri.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Starter extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/Zboruri";
        String username = "postgres";
        String password = "qwertyuiop";

        DbClient_REPO client_repo = new DbClient_REPO(url, username, password);
        DbFlight_REPO flight_repo = new DbFlight_REPO(url, username, password);
        DbTiket_REPO tiket_repo = new DbTiket_REPO(url, username, password);

        Service service = new Service(client_repo,flight_repo,tiket_repo);

        primaryStage.setTitle("Login");
        initView(primaryStage, service);
        primaryStage.show();
    }

    private void initView(Stage primaryStage, Service service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login.fxml"));
            AnchorPane root = loader.load();

            ControlerLogin controlerLogin = loader.getController();
            controlerLogin.setService(service);

            primaryStage.setScene(new Scene(root));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
