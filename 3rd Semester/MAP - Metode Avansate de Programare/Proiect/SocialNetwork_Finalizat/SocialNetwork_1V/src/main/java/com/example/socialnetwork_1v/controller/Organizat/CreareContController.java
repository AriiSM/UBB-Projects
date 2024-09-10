package com.example.socialnetwork_1v.controller.Organizat;

import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.domain.Login;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreareContController {
    Stage dialogStage;
    @FXML
    TextField lblNume;
    @FXML
    TextField lblPrenume;
    @FXML
    TextField lblUsername;
    @FXML
    TextField lblParola;

    private FullDbUtilizatorService service;

    public void setService(FullDbUtilizatorService service, Stage stage) {
        this.service = service;
        this.dialogStage = stage;
    }

    @FXML
    public void handleSave() throws Exception {
        String nume = lblNume.getText();
        String prenume = lblPrenume.getText();
        String username = lblUsername.getText();
        String parword = lblParola.getText();


        if (nume == null || prenume == null) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Atentie", "Completeaza spatiile de la nume si prenume");
        } else {
            Utilizator u = this.service.addUtilizator(prenume, nume);
            if (username == null || parword == null) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Atentie", "Completeaza spatiile de la username si parola");
            }else{
                this.service.saveCont(u.getId(),username,parword);
            }
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Succes", "Cont creat!");
        dialogStage.close();
    }

}
