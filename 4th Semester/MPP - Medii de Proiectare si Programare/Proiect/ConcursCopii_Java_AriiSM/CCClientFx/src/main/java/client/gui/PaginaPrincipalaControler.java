package client.gui;

import client.Domain_Simplu.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.util.*;

import javafx.stage.Stage;
import server.CCException;
import server.ICCObserver;
import server.ICCServices;

import java.net.URL;

public class PaginaPrincipalaControler implements Initializable, ICCObserver {
    private ICCServices service;
    private final List<ICCObserver> observers = new ArrayList<>();
    private Stage stage;
    private Organizator organizator;
    //----------------------------------------------------------------------
    //Pentru Label
    @FXML
    Label lbl_nume;

    //-----------------------------------------------------------------------
    //Tabel afisare
    @FXML
    TableView<Concurs> tabel_afisare;
    @FXML
    TableColumn<Concurs, String> proba_column;
    @FXML
    TableColumn<Concurs, String> categorie_column;
    @FXML
    TableColumn<Concurs, String> inscrisi_column;

    //-----------------------------------------------------------------------
    //Pentru completarea campurilor din Table_Afisare
    ObservableList<Concurs> observableList = FXCollections.observableArrayList();

    //-----------------------------------------------------------------------
    //Tabel_Cautare
    @FXML
    TableView<Participant> tabel_cautare;
    @FXML
    TableColumn<Participant, String> nume_column;
    @FXML
    TableColumn<Participant, String> prenume_column;
    @FXML
    TableColumn<Participant, String> varsta_column;
    //-----------------------------------------------------------------------
    //Pentru butoane de la Cautare
    ObservableList<String> obsProba = FXCollections.observableArrayList();
    ObservableList<String> obsCategorie = FXCollections.observableArrayList();
    @FXML
    ComboBox<String> combo_proba;
    @FXML
    ComboBox<String> combo_categorie;

    //-----------------------------------------------------------------------
    //Pentru completarea campurilor din Table_Cautare
    ObservableList<Participant> participantObservableList = FXCollections.observableArrayList();

    //-----------------------------------------------------------------------
    //Pentru zona de Inscriere Participanti
    @FXML
    TextField tb_nume;
    @FXML
    TextField tb_prenume;
    @FXML
    TextField tb_varsta;
    @FXML
    ComboBox<String> combo_proba_Inscriere;

    public void setDbLogin(ICCServices service, Stage stage) {
        this.service = service;
        this.stage = stage;

    }

    public void addObserver(ICCObserver observer) {
        observers.add(observer);
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setOrganizator(Organizator organizator, String nume, String prenume) throws CCException {
        this.organizator = organizator;
        lbl_nume.setText(nume + " " + prenume);
        init();
    }

    public void init() {
        participantObservableList.clear();
        observableList.clear();
        obsCategorie.clear();
        obsProba.clear();

        populateTableAfisare();
        populareTabelCautare();
        initialize();
    }

    @FXML
    public void initialize() {
        tabel_afisare.setItems(observableList);
        proba_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProba().toString()));
        categorie_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie().toString()));
        inscrisi_column.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(String.valueOf(service.filterProbaCategorieParticipant(cellData.getValue().getProba().toString(), cellData.getValue().getCategorie().toString()).size()));
            } catch (CCException e) {
                throw new RuntimeException(e);
            }
        });


        tabel_cautare.setItems(participantObservableList);
        nume_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        prenume_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        varsta_column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAge().toString()));


        combo_categorie.setItems(obsCategorie);
        combo_proba.setItems(obsProba);

    }

    private void populateTableAfisare() {
        List<String> probe = Arrays.asList("DESEN", "CAUTAREA_UNEI_COMORI", "POEZIE");
        List<String> categorii = Arrays.asList("VARSTA_6_8", "VARSTA_9_11", "VARSTA_12_15");

        obsProba.setAll(probe);
        obsCategorie.setAll(categorii);
        combo_proba_Inscriere.setItems(obsProba);

        for (String proba : probe) {
            Proba probaObj = Proba.valueOf(proba);
            for (String categorie : categorii) {
                Categorie categorieObj = Categorie.valueOf(categorie);
                Concurs concurs = new Concurs(categorieObj, probaObj);
                observableList.add(concurs);
            }
        }
    }

    private void populareTabelCautare() {
        Iterable<Participant> participantIterable = null;
        try {
            participantIterable = service.findAllParticipant();
        } catch (CCException e) {
            throw new RuntimeException(e);
        }
        participantObservableList.clear();
        if (participantIterable != null) {
            for (Participant participant : participantIterable) {
                participantObservableList.add(participant);
            }
        }
    }

    @FXML
    private void handleCautare() throws CCException {
        String proba = combo_proba.getValue();
        String categorie = combo_categorie.getValue();

        List<Participant> filtrareParticipanti = service.filterProbaCategorieParticipant(proba, categorie);
        participantObservableList.setAll(filtrareParticipanti);
    }

    @FXML
    private void handleInscriere() {
        String nume = tb_nume.getText();
        String prenume = tb_prenume.getText();
        int varsta = Integer.parseInt(tb_varsta.getText());
        String proba = combo_proba_Inscriere.getValue();
        String categorie;

        if (6 <= varsta && varsta <= 8)
            categorie = "VARSTA_6_8";
        else if (9 <= varsta && varsta <= 11)
            categorie = "VARSTA_9_11";
        else if (12 <= varsta && varsta <= 15)
            categorie = "VARSTA_12_15";
        else {
            categorie = null;
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Nu exista posibilitatea de inscriere", "Varsta trebuie sa fie intre 6 si 15 ani!!");
            return;
        }

        if (nume == null || prenume == null) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Nume sau prenume!", "Asigura-te ca ai completat toate campurile!");
            return;
        }
        if (categorie == null || proba == null) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Proba sau varsta!", "Asigura-te ca ai completat toate campurile!");
            return;
        }

        Concurs concurs;
        try {
            concurs = service.findConcursProbaCategorie(proba, categorie).orElseGet(null);
        } catch (CCException e) {
            throw new RuntimeException(e);
        }

        Participant participant = null;
        try {
            participant = service.findParticipantNumePrenumeVarsta(nume, prenume, varsta).orElseGet(null);
        } catch (CCException e) {
            throw new RuntimeException(e);
        }

        // Daca participantul nu exista, il cream si il salvam
        if (participant == null) {
            participant = new Participant(nume, prenume, varsta);
            try {
                participant = service.saveParticipant(participant).orElse(null);
            } catch (CCException e) {
                throw new RuntimeException(e);
            }
        }

        Integer numarProbeParticipate = null;
        try {
            numarProbeParticipate = service.numarProbePentruParticipantParticipant(participant.getId());
        } catch (CCException e) {
            throw new RuntimeException(e);
        }

        if (numarProbeParticipate == -1) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "PROBLEMA!", "Problema tehnica (id Participant)");
            return;
        }

        if (numarProbeParticipate < 2) {
            // Participantul poate fi înscris la încă o probă
            Inscriere inscriere = new Inscriere(participant, concurs);
            try {
                inscriere = service.saveInscriere(inscriere).orElse(null);
                //Platform.runLater(this::init);
            } catch (CCException e) {

                throw new RuntimeException(e);
            }

            if (inscriere != null) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Felicitari!", "Inscriere efectuata cu succes");
            } else {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Eroare la salvarea inscrierii", "Nu s-a putut salva inscrierea!");
            }
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Prea multe inscrieri", "Acest participant este deja inscris la 2 probe");
        }


    }

    void logout() {
        try {
            service.logout(organizator, this);
            stage.close();
        } catch (CCException e) {
            System.out.println("Logout error " + e);
        }
    }

    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        logout();
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("END INIT!!!!!!!!!");
    }

    public void inscriereReceives(Inscriere inscriere) {
        // Curăță listele existente
//        observableList.clear();
//        participantObservableList.clear();
        System.out.println("SUNT IN OBSERVER" + inscriere);
        Platform.runLater(this::init);
        System.out.println("GENERAT!");
        //init();
    }

}