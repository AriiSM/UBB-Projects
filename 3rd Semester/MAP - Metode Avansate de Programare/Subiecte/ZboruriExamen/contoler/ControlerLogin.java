package com.example.zboruri.contoler;

import com.example.zboruri.domain.Client;
import com.example.zboruri.service.Service;
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

    private Service service;

    @FXML
    Button btn_login;
    @FXML
    TextField txt_username;
    public void setService(Service service){
        this.service = service;
    }

    @FXML
    private void handleLogin(){
        String username1 = txt_username.getText();

        if(username1.isEmpty()){
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Completeaza", "Introdu username!");
        }else{
            Client client = service.findOneClientByUsername(username1);
            startClient(client);
        }
    }

    private void startClient(Client client){
        Stage primaryStage = new Stage();
        primaryStage.setTitle(client.getName());
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initViewClient(primaryStage,client);
        primaryStage.setWidth(667);
        primaryStage.show();
    }
    private void initViewClient(Stage primaryStage, Client client) {
        try {

            FXMLLoader loaderC = new FXMLLoader();
            loaderC.setLocation(getClass().getResource("/view/client.fxml"));
            AnchorPane root = loaderC.load();

            ControlerClient controlerClient = loaderC.getController();
            controlerClient.setService(service,client);

            primaryStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
