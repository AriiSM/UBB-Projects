package client.gui;


import client.Domain_Simplu.Organizator;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.CCException;
import server.ICCServices;


public class LoginControler{

    private ICCServices service;
    private PaginaPrincipalaControler pCtrl;
    private Stage stage;
    private Organizator organizatorCrt;
    //------------------------------------------------
    //              TextFields pentru logare
    @FXML
    private TextField tb_parola;
    @FXML
    private TextField tb_nume_prenume;
    Parent mainCCParent;

    public void setDbLogin(ICCServices service, Stage stage) {
        this.service = service;
        this.stage = stage;
    }

    public void setParent(Parent p) {
        mainCCParent = p;
    }

    @FXML
    private void handleLogin() throws CCException {
        //Parent root;
        String[] nume = tb_nume_prenume.getText().split(" ");
        String passwd = tb_parola.getText();
        Organizator org = service.findAccountOrganizator(passwd, nume[0], nume[1]);
        organizatorCrt = org;


        try {
            service.login(organizatorCrt, pCtrl);
            Stage stage = pCtrl.getStage();
            stage.setTitle("Cont personal");
            stage.setScene(new Scene(mainCCParent));

            stage.setOnCloseRequest(event -> {
                pCtrl.logout();
                System.exit(0);
            });

            stage.show();
            pCtrl.setOrganizator(organizatorCrt, nume[0], nume[1]);
            //((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        } catch (CCException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }

    public void setPController(PaginaPrincipalaControler pCtr) {
        this.pCtrl = pCtr;
    }

    @FXML
    private void handleClose() {
        System.exit(0);
        stage.close();
    }

}
