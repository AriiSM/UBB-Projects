package com.example.socialnetwork_1v.controller.Organizat.UTILIZATOR;

import com.example.socialnetwork_1v.controller.Organizat.MessageAlert;
import com.example.socialnetwork_1v.domain.Invitatii;
import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class CereriUtilizator {

    //------Tabel Users--------
    @FXML
    TableView<Utilizator> tableUsers;
    @FXML
    TableColumn<Utilizator, String> numeUsers;
    @FXML
    TableColumn<Utilizator, String> prenumeUsers;
    //------Tabel Users--------

    //--------Tabel cereri-------
    @FXML
    TableView<Invitatii> tableCereri;
    @FXML
    TableColumn<Invitatii, String> numeCereri;
    @FXML
    TableColumn<Invitatii, String> statusCereri;
    @FXML
    TableColumn<Invitatii, String> dataCereri;
    //--------Tabel cereri-------


    private FullDbUtilizatorService service;
    private String idUtilizator;
    ObservableList<Invitatii> observableCereri = FXCollections.observableArrayList();
    ObservableList<Utilizator> observerUsers = FXCollections.observableArrayList();


    public void setService(FullDbUtilizatorService service, String cod) {
        this.service = service;
        this.idUtilizator = cod;
        initModel();
    }

    @FXML
    public void initialize() {
        numeCereri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFromInvite().getFirst_name() + " " + cellData.getValue().getFromInvite().getLast_name()));
        statusCereri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        dataCereri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataString()));

        tableCereri.setItems(observableCereri);

        numeUsers.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLast_name()));
        prenumeUsers.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirst_name()));

        tableUsers.setItems(observerUsers);
    }

    private void initModel() {
        Iterable<Invitatii> invitatiis = service.getAllInvitatii();
        List<Invitatii> rezultat = new ArrayList<>();

        for (Invitatii invitatii : invitatiis) {
            if (invitatii.getToInvite().getId().toString().equals(idUtilizator)) {
                rezultat.add(invitatii);
            }
        }
        observableCereri.setAll(rezultat);

        //prietenii
        Iterable<Prietenie> friends = service.getAllFriends();
        List<Prietenie> rezultatF = new ArrayList<>();
        for (var f : friends) {
            if (f.getId().getLeft().toString().equals(idUtilizator)) {
                rezultatF.add(f);
            }
        }

        Iterable<Utilizator> users = service.getAllUsers();
        //transform din lista nemodificabila in lista modificabila
        List<Utilizator> listaUtilizatori = new ArrayList<>(StreamSupport.stream(users.spliterator(), false).toList());
        List<Utilizator> listaModificabila = new ArrayList<>(listaUtilizatori);

        listaModificabila.removeIf(user -> rezultatF.stream().anyMatch(prietenie -> prietenie.getId().getRight().equals(user.getId())));
        Utilizator u = service.findOneUser(Long.parseLong(idUtilizator));
        listaModificabila.removeIf(user -> user.getId() == Long.parseLong(idUtilizator));
        observerUsers.setAll(listaModificabila);

    }

    @FXML
    private void handleAccepta() throws Exception {
        Invitatii selected = tableCereri.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("approved");
            Invitatii i = this.service.updateInvitatie(selected);

            String user1Nume = selected.getFromInvite().getLast_name();
            String user1Prenume = selected.getFromInvite().getFirst_name();

            String user2Nume = selected.getToInvite().getLast_name();
            String user2Prenume = selected.getToInvite().getFirst_name();

            this.service.adaugaPrietenie(user1Prenume, user1Nume, user2Prenume, user2Nume, selected.getDateInvite());
            this.service.adaugaPrietenie(user2Prenume, user2Nume, user1Prenume, user1Nume, selected.getDateInvite());

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Accept", "S-a creat prietenia!");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici o invitatie!");
        }
        initModel();
    }

    @FXML
    private void handleRespinge() {
        Invitatii selected = tableCereri.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("rejected");
            Invitatii i = this.service.updateInvitatie(selected);
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Accept", "S-a creat prietenia!");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici o invitatie!");
        }
        initModel();
    }


    @FXML
    private void handleRefresh() {
        initModel();
    }


    @FXML
    private void handleAdd(){
        LocalDateTime ldt = LocalDateTime.now();
        //from
        Utilizator u = service.findOneUser(Long.parseLong(idUtilizator));

        //to
        Utilizator selected = tableUsers.getSelectionModel().getSelectedItem();
        Invitatii invitatii = new Invitatii(service.createID(), u, selected, ldt, "pending");
        saveInvitatie(invitatii);
    }
    private void saveInvitatie(Invitatii invitatii) {
        try {
            Invitatii i = this.service.addInvitatie(invitatii);

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Slavare invitatiei", "Invitatia a fost salvat");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
