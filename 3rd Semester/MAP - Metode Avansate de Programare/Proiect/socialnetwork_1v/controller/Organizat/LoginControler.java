package com.example.socialnetwork_1v.controller.Organizat;

import com.example.socialnetwork_1v.controller.Organizat.UTILIZATOR.ProfilController;
import com.example.socialnetwork_1v.criptare.AESEncryptionDecryptionExample;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class LoginControler {
    private FullDbUtilizatorService service;


    //-------------------------------------------
    //           TextFields pentru logare
    @FXML
    private TextField lblUsername;
    @FXML
    private TextField lblPassword;
    //-------------------------------------------

    public void setDbLogin(FullDbUtilizatorService service) {
        this.service = service;

    }

    @FXML
    private void handleLogin() throws Exception {
        String username = lblUsername.getText();
        String password = lblPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Completeaza", "Introdu username si parola!");
        } else {

            Optional<String[]> opt = service.findUsernamePass(username, password);

            if (opt.isEmpty()) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "NuExista", "Creaza un cont!");
            }
            //contul adminulul
            if (username.equals("admin") && password.equals("admin")) {
                startAdminWork();
            }
            //contul utilizatorilor
            else {
                String str = Arrays.toString(opt.get());
                str = str.substring(1, str.length() - 1);
                String[] cod = str.split(" ");

                startUtilizatorWork(username, cod[1]);
            }


        }
    }

    private void startUtilizatorWork(String usernameU, String cod) throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle(usernameU);
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewUtilizator(primaryStage, cod);
        primaryStage.setWidth(667);
        primaryStage.setTitle(usernameU);
        primaryStage.show();
    }

    private void initViewUtilizator(Stage primaryStage, String cod) throws IOException {
        FXMLLoader profilLoader = new FXMLLoader();
        profilLoader.setLocation(getClass().getResource("/view/profilUser.fxml"));
        AnchorPane UserLayout = profilLoader.load();

        ProfilController profilController = profilLoader.getController();
        profilController.setDbProfil(service, cod);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(UserLayout));
    }

    private void startAdminWork() throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Edit Utilizator");
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewAdmin(primaryStage);
        primaryStage.setWidth(667);
        primaryStage.setTitle("Utilizatori");
        primaryStage.show();
    }

    private void initViewAdmin(Stage primaryStage) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(getClass().getResource("/view/utilizator.fxml"));
        AnchorPane UserLayout = userLoader.load();

        AdminController utilizatorController = userLoader.getController();
        utilizatorController.setDbUtilizator(service);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(UserLayout));
    }


    @FXML
    private void handleCreareAccount() throws IOException {
        startCreareCont();
    }

    private void startCreareCont() throws IOException {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Creare");
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewCreareCont(primaryStage);
        primaryStage.setWidth(667);
        primaryStage.setTitle("CreateAccount");
        primaryStage.show();
    }

    private void initViewCreareCont(Stage primaryStage) throws IOException {
        FXMLLoader createLoader = new FXMLLoader();
        createLoader.setLocation(getClass().getResource("/view/creareCont.fxml"));
        AnchorPane UserLayout = createLoader.load();

        CreareContController creareContController = createLoader.getController();
        creareContController.setService(service,primaryStage);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(UserLayout));
    }
}
