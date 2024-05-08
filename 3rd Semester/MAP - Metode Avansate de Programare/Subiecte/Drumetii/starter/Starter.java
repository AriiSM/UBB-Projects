package com.example.clienti_locatie_horel_rezervare.starter;

import com.example.clienti_locatie_horel_rezervare.controler.ControlerPersoana;
import com.example.clienti_locatie_horel_rezervare.repository.*;
import com.example.clienti_locatie_horel_rezervare.service.ServiceFullDB;
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

    public Starter() {
    }

    @Override
    public void start(Stage primaryStage) {
        String url = "jdbc:postgresql://localhost:5432/Drumetie";
        String username = "postgres";
        String password = "qwertyuiop";

        DbPersoana_REPO persoana_repo = new DbPersoana_REPO(url, username, password);
        DbParticipant_REPO participant_repo = new DbParticipant_REPO(url, username, password);
        DbSalvamont_REPO salvamont_repo = new DbSalvamont_REPO(url, username, password);
        DbCoordonator_REPO coordonator_repo = new DbCoordonator_REPO(url, username, password);
        DbRecenzie_REPO recenzie_repo = new DbRecenzie_REPO(url, username, password);
        DbDrumetie_REPO drumetie_repo = new DbDrumetie_REPO(url, username, password);

        ServiceFullDB service = new ServiceFullDB(persoana_repo,participant_repo,salvamont_repo,coordonator_repo,recenzie_repo,drumetie_repo);

        primaryStage.setTitle("HAHA");
        initView(primaryStage, service);
        primaryStage.show();
    }

    private void initView(Stage primaryStage, ServiceFullDB service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/prima.fxml"));
            AnchorPane root = loader.load();

            ControlerPersoana controlerPersoana = loader.getController();
            controlerPersoana.setService(service);

            primaryStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}