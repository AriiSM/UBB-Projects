package com.example.socialnetwork_1v.controller.Organizat.UTILIZATOR;

import com.example.socialnetwork_1v.controller.Organizat.MessageAlert;
import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import com.example.socialnetwork_1v.utils.Observer;
import com.example.socialnetwork_1v.utils.events.EditUtilizatorEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class ProfilController implements Observer<EditUtilizatorEvent> {

    //---------Numele contului
    @FXML
    Label lblCONT;
    String idUtilizator;
    //---------Numele contului

    //-------Tabela de prieteni
    @FXML
    TableView<Prietenie> tabelPrietenii;
    @FXML
    TableColumn<Prietenie, String> columnNumePrieteni;
    //-------Tabela de prieteni


    private FullDbUtilizatorService service;
    ObservableList<Prietenie> observerFriend = FXCollections.observableArrayList();

    public void setDbProfil(FullDbUtilizatorService service, String cod) {
        this.service = service;
        this.service.addObserver(this);

        this.idUtilizator = cod;
        initModel();
    }

    @FXML
    public void initialize() {
        columnNumePrieteni.setCellValueFactory(cellData -> new SimpleStringProperty(service.findOneUser(cellData.getValue().getId().getRight()).getFirst_name()));
        tabelPrietenii.setItems(observerFriend);
    }

    private void initModel() {
        Utilizator user = service.findOneUser(Long.parseLong(idUtilizator));
        lblCONT.setText(user.getFirst_name() + " " + user.getLast_name());

        Iterable<Prietenie> friends = service.getAllFriends();
        List<Prietenie> rezultat = new ArrayList<>();
        for (var f : friends) {
            if (f.getId().getLeft().toString().equals(idUtilizator)) {
                rezultat.add(f);
            }
        }
        observerFriend.setAll(rezultat);
    }

    @Override
    public void update(EditUtilizatorEvent editUtilizatorEvent) {
        System.out.println(editUtilizatorEvent);
        initModel();
    }


    @FXML
    public void handdleCereri() {
        showCereriUtilizator();
    }

    public void showCereriUtilizator() {
        try {
            FXMLLoader cereriLoader = new FXMLLoader();
            cereriLoader.setLocation(getClass().getResource("/view/cereriUtilizator.fxml"));

            AnchorPane rootCereri = cereriLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Cereri");
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(rootCereri);
            stage.setScene(scene);

            CereriUtilizator cereriUtilizator = cereriLoader.getController();
            cereriUtilizator.setService(service, idUtilizator);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleMesaj() {
        try {
            FXMLLoader mesajeLoader = new FXMLLoader();
            mesajeLoader.setLocation(getClass().getResource("/view/mesajeUsers.fxml"));

            AnchorPane rootMesaje = mesajeLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Mesaje");
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(rootMesaje);
            stage.setScene(scene);

            MesajeController cereriUtilizator = mesajeLoader.getController();

            Prietenie selected = tabelPrietenii.getSelectionModel().getSelectedItem();
            if (selected != null) {
                cereriUtilizator.setService(service, idUtilizator, selected.getId().getRight().toString());
                stage.show();
            } else
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "SELECT", "Selecteaza un utilizator!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
