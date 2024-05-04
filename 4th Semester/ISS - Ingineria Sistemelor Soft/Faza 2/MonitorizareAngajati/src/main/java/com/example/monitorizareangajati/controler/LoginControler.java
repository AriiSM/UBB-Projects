package com.example.monitorizareangajati.controler;

import com.example.monitorizareangajati.domain.Angajat;
import com.example.monitorizareangajati.domain.Sef;
import com.example.monitorizareangajati.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginControler {
    private Service service;
    private Stage stage;

    //------------------------------------------------
    //              TextFields pentru logare
    @FXML
    private TextField tb_parola;
    @FXML
    private TextField tb_username;

    public void setDbLogin(Service service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }

    @FXML
    private void handleLogin() throws IOException {
        String username = tb_username.getText();
        String parola = tb_parola.getText();

        if (username.isEmpty() || parola.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Completeaza", "Introdu username si parola!");
        } else {
            Sef sef = service.findAccountSef(parola, username);
            Angajat angajat = service.findAccountAngajat(parola, username);
            if (sef == null && angajat == null) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Nu Exista", "Creaza un cont!");
            } else if (sef == null) {
                startAccountAngajat(angajat.getNume(), angajat.getPrenume());
            } else {
                startAccountSef(sef.getNume(), sef.getPrenume());
            }
        }
    }

    private void startAccountAngajat(String nume, String prenume) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Aplicatie Angajat "+ nume+ " "+prenume);
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewAngajat(primaryStage, nume, prenume);
        primaryStage.setTitle("Aplicatie Angajat "+ nume+ " "+prenume);
        primaryStage.show();
    }

    private void initViewAngajat(Stage primaryStage, String nume, String prenume) throws IOException {
        FXMLLoader profilLoader = new FXMLLoader();
        profilLoader.setLocation(getClass().getResource("/view/angajat.fxml"));
        AnchorPane UserLayout = profilLoader.load();

        AngajatControler profilController = profilLoader.getController();
        profilController.setDb(service, nume, prenume, primaryStage);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(UserLayout));
    }

    @FXML
    private void handleClose(){
        stage.close();
    }

    private void startAccountSef(String nume, String prenume) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Aplicatie Sef "+ nume+" "+prenume);
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewSef(primaryStage, nume, prenume);
        primaryStage.setTitle("Aplicatie Sef "+ nume+" "+prenume);
        primaryStage.show();
    }

    private void initViewSef(Stage primaryStage, String nume, String prenume) throws IOException {
        FXMLLoader profilLoader = new FXMLLoader();
        profilLoader.setLocation(getClass().getResource("/view/sef.fxml"));
        AnchorPane UserLayout = profilLoader.load();

        SefControler profilController = profilLoader.getController();
        profilController.setDb(service, nume, prenume, primaryStage);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(UserLayout));
    }
}
