package mpp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mpp.Exceptions.GenericException;

public class LoginController{
    IServices service;
    Stage stage;
    private Persoana jucatorCurent;
    private MainController gameController;
    private FXMLLoader cloader;
    private Parent croot;

    @FXML
    public TextField tbUsername;



    public void setService(IServices service, Stage stage, MainController gameController, FXMLLoader cloader, Parent croot) {
        this.service = service;
        this.stage = stage;
        this.gameController = gameController;
        this.cloader = cloader;
        this.croot = croot;
    }


    @FXML
    public void handleLogin() {
        String username = tbUsername.getText();
        Persoana jucator = null;
        try {
            jucator = service.findJucator(username);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        jucatorCurent = jucator;
        try {
            service.login(username, gameController);
            stage.setScene(new Scene((AnchorPane) croot));

            gameController.setService(jucatorCurent, service, stage);
            stage.show();

        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }
}
