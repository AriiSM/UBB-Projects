package com.example.socialnetwork_1v.controller.Organizat.ADMIN;

import com.example.socialnetwork_1v.controller.Organizat.MessageAlert;
import com.example.socialnetwork_1v.domain.Invitatii;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminInvitatiiController {
    private FullDbUtilizatorService service;
    Stage dialogStage;
    ObservableList<Invitatii> observableList = FXCollections.observableArrayList();


    @FXML
    TableView<Invitatii> tableView;
    @FXML
    TableColumn<Invitatii, String> tableColumnUser1;
    @FXML
    TableColumn<Invitatii, String> tableColumnUser2;
    @FXML
    TableColumn<Invitatii, String> tableColumnStatus;
    @FXML
    TableColumn<Invitatii, String> tableColumnData;
    //-------------------end TableView fx:id------------------

    @FXML
    TextField txtUser1;
    @FXML
    TextField txtUser2;


    // ------------- pt pagini--------------
    @FXML
    private TextField lblPgInvitatii;

    private int pageSizeInvitatii = 5;
    private int currentPageInvitatii = 0;
    private int totalNrOfElemsInvitatii = 0;

    @FXML
    Button prevButtonInvitatii;
    @FXML
    Button nextButtonInvitatii;

    @FXML
    public void handleNextInvitatii(ActionEvent ev) {
        currentPageInvitatii++;
        initModel();
    }

    @FXML
    public void handlePrevInvitatii(ActionEvent ev) {
        currentPageInvitatii--;
        initModel();
    }

    public void setService(FullDbUtilizatorService dbInvites, Stage stace) {
        this.service = dbInvites;
        this.dialogStage = stace;
        initModel();
    }

    private void initModel() {
//        Iterable<Invitatii> invitatiis = service.getAllInvitatii();
//        List<Invitatii> invitatiiList = StreamSupport.stream(invitatiis.spliterator(), false).toList();
//        observableList.setAll(invitatiiList);

        Page<Invitatii> pageInvitatii = service.findAllOnInvitatiiPage(new Pageable(currentPageInvitatii, pageSizeInvitatii));
        int maxPageInvitatii = (int) Math.ceil((double) pageInvitatii.getTotalNrOfElems() / pageSizeInvitatii) - 1;

        if (currentPageInvitatii > maxPageInvitatii) {
            currentPageInvitatii = maxPageInvitatii;

            pageInvitatii = service.findAllOnInvitatiiPage(new Pageable(currentPageInvitatii, pageSizeInvitatii));
        }

        observableList.setAll(StreamSupport.stream(pageInvitatii.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNrOfElemsInvitatii = pageInvitatii.getTotalNrOfElems();

        prevButtonInvitatii.setDisable(currentPageInvitatii == 0);
        nextButtonInvitatii.setDisable((currentPageInvitatii + 1) * pageSizeInvitatii >= totalNrOfElemsInvitatii);
    }

    @FXML
    public void initialize() {
        tableColumnUser1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFromInvite().getFirst_name()));
        tableColumnUser2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getToInvite().getFirst_name()));
        tableColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        tableColumnData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataString()));

        tableView.setItems(observableList);

        lblPgInvitatii.textProperty().addListener(o -> updatePageInvitatii());
    }

    private void updatePageInvitatii() {
        if (lblPgInvitatii.getText().isEmpty() || Integer.parseInt(lblPgInvitatii.getText()) == 0){
            initModel();
        }else{
            this.pageSizeInvitatii = Integer.parseInt(lblPgInvitatii.getText());
            this.currentPageInvitatii = 0;
            initModel();
        }
    }

    private List<Invitatii> getInvitatiiList() {
        return StreamSupport.stream(service.getAllInvitatii().spliterator(), false)
                .map(n -> new Invitatii(n.getId(), n.getFromInvite(), n.getToInvite(), n.getDateInvite(), n.getStatus()))
                .collect(Collectors.toList());
    }

    @FXML
    private void handleRefresh(ActionEvent ev) {
        List<Invitatii> invitatiiList = getInvitatiiList();
        List<Invitatii> elimina = invitatiiList
                .stream()
                .filter(o -> !o.getStatus().equals("pending"))
                .toList();

        invitatiiList = invitatiiList
                .stream()
                .filter(o -> o.getStatus().equals("pending"))
                .toList();


        if (!elimina.isEmpty()) {

            for (var i : elimina) {
                service.deleteInvitatie(i);
            }
        }
        observableList.setAll(invitatiiList);

    }

    @FXML
    private void handleRespingere(ActionEvent ev) {
        Invitatii selected = tableView.getSelectionModel().getSelectedItem();
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
    private void handleAcceptInvitatie(ActionEvent ev) throws Exception {
        Invitatii selected = tableView.getSelectionModel().getSelectedItem();
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
    private void handleAddInvitatie() {
        String user1 = txtUser1.getText();//prenume+nume
        String[] prenumeNumeFrom = user1.split(" ");
        Utilizator userFrom = service.findByNumePrenume(prenumeNumeFrom[1], prenumeNumeFrom[0]);


        String user2 = txtUser2.getText();
        String[] prenumeNumeTo = user2.split(" ");
        Utilizator userTo = service.findByNumePrenume(prenumeNumeTo[1], prenumeNumeTo[0]);

        LocalDateTime ldt = LocalDateTime.now();

        Invitatii invitatii = new Invitatii(service.createID(), userFrom, userTo, ldt, "pending");
        saveInvitatie(invitatii);
    }

    private void saveInvitatie(Invitatii invitatii) {
        try {
            Invitatii i = this.service.addInvitatie(invitatii);
            if (i == null) {
                dialogStage.close();
            }
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Slavare invitatiei", "Invitatia a fost salvat");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
        dialogStage.close();
    }

}
