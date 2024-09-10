package com.example.socialnetwork_1v.controller.Organizat.ADMIN;

import com.example.socialnetwork_1v.controller.Organizat.MessageAlert;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminAddUtilizatorController {
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNume;
    @FXML
    private TextField txtPrenume;
    @FXML
    private TextField txtPrieteni;

    private FullDbUtilizatorService service;
    Stage dialogStage;
    Utilizator user;


    public void setService(FullDbUtilizatorService service, Stage stage, Utilizator u) {
        this.service = service;
        this.dialogStage = stage;
        this.user = u;
        if (null != u) {
            setFields(u);
        }
    }

    private void setFields(Utilizator user) {
        txtNume.setText(user.getLast_name());
        txtPrenume.setText(user.getFirst_name());
    }

    @FXML
    public void handleSave() {
        String nume = txtNume.getText();
        String prenume = txtPrenume.getText();

        Utilizator u = new Utilizator(nume, prenume);

        if (this.user == null)
            saveUser(u);
        else {
            u.setId(user.getId());
            updateUser(u);
        }
    }

    private void saveUser(Utilizator user) {
        try {
            Utilizator u = this.service.addUtilizator(user.getFirst_name(), user.getLast_name());
            if (u == null)
                dialogStage.close();
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Slavare user", "Utilizatorul a fost salvat");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

    private void updateUser(Utilizator user) {
        try {
            Utilizator u = this.service.updateUtilizator(user);
            if (u == null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare utilizator","Utilizatorul a fost modificat");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        dialogStage.close();
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

}
