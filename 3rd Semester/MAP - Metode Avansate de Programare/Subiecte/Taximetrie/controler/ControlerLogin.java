package com.example.taximetrie.controler;

import com.example.taximetrie.domain.Persoana;
import com.example.taximetrie.domain.Sofer;
import com.example.taximetrie.service.ServiceFullDb;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControlerLogin {
    ServiceFullDb service;

    @FXML
    Button btn_login;
    @FXML
    TextField txt_username;


    public void setService(ServiceFullDb serviceFullDb) {
        this.service = serviceFullDb;
    }

    @FXML
    private void handleLogin() {
        String username = txt_username.getText();

        if(username.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Completeaza", "Introdu username!");
        }
        else{
            Sofer taximetrist = service.findOneSoferBYusername(username);
            Persoana persoana = service.findOnePersoanaBYusername(username);

            if(taximetrist != null){
                startTaximetristWork(taximetrist.getName(),taximetrist.getId(),taximetrist);
            }
            else if(persoana != null){
                startPersoanaWork(persoana.getName(),persoana);
            }
            else{
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "NuExista", "Username-ul nu exista!");

            }
        }
    }

    private void startTaximetristWork(String nume,Long id,Sofer taximetrist){
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Taximetrist: " + nume);
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewTaximetrist(primaryStage,id,taximetrist);
        primaryStage.setWidth(667);
        primaryStage.show();
    }

    private void startPersoanaWork(String nume, Persoana persoana){
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Client: " + nume);
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewClient(primaryStage,persoana);
        primaryStage.setWidth(667);
        primaryStage.show();
    }
    private void initViewClient(Stage primaryStage,Persoana persoana) {
        try {

            FXMLLoader loaderC = new FXMLLoader();
            loaderC.setLocation(getClass().getResource("/view/client.fxml"));
            AnchorPane root = loaderC.load();

            ControlerClient controlerClient = loaderC.getController();
            controlerClient.setService(service, persoana);

            primaryStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initViewTaximetrist(Stage primaryStage,Long id,Sofer taximetrist) {
        try {

            FXMLLoader loaderT = new FXMLLoader();
            loaderT.setLocation(getClass().getResource("/view/taximetrist.fxml"));
            AnchorPane root = loaderT.load();

            ControlerTaximetrist controlerTaximetrist = loaderT.getController();
            controlerTaximetrist.setService(service,id,taximetrist);

            primaryStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
