package com.example.monitorizareangajati.controler;

import com.example.monitorizareangajati.domain.Angajat;
import com.example.monitorizareangajati.domain.AtribuireSarcina;
import com.example.monitorizareangajati.domain.Sarcina;
import com.example.monitorizareangajati.domain.StatusSarcina;
import com.example.monitorizareangajati.service.Service;
import com.example.monitorizareangajati.utils.Observer;
import com.example.monitorizareangajati.utils.events.EventImp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class SefControler implements Observer<EventImp>{
    private Service service;
    private Stage stage;

    private String numeSef;
    private String prenumeSef;

    //----------------------------------
    //Tabelul Atribuire Sarcini
    private ObservableList<AtribuireSarcina> sarciniList = FXCollections.observableArrayList();
    @FXML
    private TableView<AtribuireSarcina> tabelAtribuireSarcini;

    @FXML
    private TableColumn<AtribuireSarcina, String> columnAngajat;

    @FXML
    private TableColumn<AtribuireSarcina, String> columnSarcina2;

    @FXML
    private TableColumn<AtribuireSarcina, String> columnStatusSarcina;

    @FXML
    private TextArea tb_SarcinaNoua;

    @FXML
    private TextField tb_AngajatNou;


    //----------------------------------
    //Tabelul Angajati
    private ObservableList<Angajat> angajats = FXCollections.observableArrayList();
    @FXML
    private TableView<Angajat> tabelAngajati;
    @FXML
    private TableColumn<Angajat, String> columnNume;
    @FXML
    private TableColumn<Angajat, String> columnStatus;
    @FXML
    private TextArea txt_descriere;


    public void setDb(Service service, String nume, String pernume, Stage stage) {
        this.service = service;
        this.service.addObserver((Observer<EventImp>) this);
        this.stage = stage;

        this.numeSef = nume;
        this.prenumeSef = pernume;

        initModel();
    }

    @FXML
    private void initialize() {
        columnAngajat.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAngajat().getNume() + " " + cellData.getValue().getAngajat().getPrenume()));
        columnSarcina2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSarcina().getDescriere()));
        columnStatusSarcina.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        columnNume.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNume() + " " + cellData.getValue().getPrenume()));
        columnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
    }

    private void initModel() {
        sarciniList.clear();
        angajats.clear();

        Iterable<AtribuireSarcina> atribuiriSarcina = service.findAllAtribuireSarcina();
        for (AtribuireSarcina atribuireSarcina : atribuiriSarcina) {
            sarciniList.add(atribuireSarcina);
        }
        tabelAtribuireSarcini.setItems(sarciniList);

        Iterable<Angajat> angajati = service.findAllAngajati();
        for (Angajat angajat : angajati) {
            angajats.add(angajat);
        }
        tabelAngajati.setItems(angajats);
    }

    @FXML
    private void handleTrimiteSarcina() {
        Angajat angajatSelectat = tabelAngajati.getSelectionModel().getSelectedItem();
        String descriereSarcina = txt_descriere.getText();

        if (angajatSelectat != null && descriereSarcina != null) {
            Sarcina sarcina = null;
            sarcina = service.findSarcinaByDescriere(descriereSarcina);
            if(sarcina == null){
                sarcina = new Sarcina(descriereSarcina);
                sarcina = service.saveSarcina(sarcina);

                AtribuireSarcina atribuireSarcina = new AtribuireSarcina(angajatSelectat, sarcina, StatusSarcina.TODO);
                service.saveAtribuireSarcina(atribuireSarcina);
            }else{
                AtribuireSarcina atribuireSarcina = new AtribuireSarcina(angajatSelectat, sarcina, StatusSarcina.TODO);
                service.saveAtribuireSarcina(atribuireSarcina);
            }

            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Sarcina trimisa", "Sarcina a fost trimisa cu succes!");
            initModel();
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Completeaza", "Selecteaza un angajat si completeaza descrierea sarcinii!");
        }
    }

    @FXML
    private void handleModificaSarcina(){
        AtribuireSarcina sarcinaSelectata = tabelAtribuireSarcini.getSelectionModel().getSelectedItem();
        String descriereSarcina = tb_SarcinaNoua.getText();
        String numePrenume = tb_AngajatNou.getText();


        if(!numePrenume.isEmpty() && !descriereSarcina.isEmpty()){
            String[] np = numePrenume.split(" ");
            System.out.println(numePrenume);
            System.out.println(np[0]+" "+np[1]);
            Angajat angajat = service.findAngajatByNumePrenume(np[0], np[1]);
            Sarcina sarcina = service.findSarcinaByDescriere(descriereSarcina);

            sarcinaSelectata.setAngajat(angajat);
            sarcinaSelectata.setSarcina(sarcina);
            sarcinaSelectata.setStatus(StatusSarcina.TODO);

            service.updateAtribuieSarcina(sarcinaSelectata);
        }else if(!numePrenume.isEmpty()){
            String[] np = numePrenume.split(" ");
            Angajat angajat = service.findAngajatByNumePrenume(np[0], np[1]);

            sarcinaSelectata.setAngajat(angajat);
            sarcinaSelectata.setStatus(StatusSarcina.TODO);

            service.updateAtribuieSarcina(sarcinaSelectata);
        }else if (!descriereSarcina.isEmpty()){
            Sarcina sarcina = service.findSarcinaByDescriere(descriereSarcina);

            sarcinaSelectata.setSarcina(sarcina);
            sarcinaSelectata.setStatus(StatusSarcina.TODO);

            service.updateAtribuieSarcina(sarcinaSelectata);
        }else{
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Fii atent", "Selecteaza un angajat si completeaza descrierea sarcinii sau nou angajat!");
        }
    }

    @FXML
    private void handleStergeSarcina(){
        AtribuireSarcina sarcinaSelectata = tabelAtribuireSarcini.getSelectionModel().getSelectedItem();
        service.deleteAtribuireSarcina(sarcinaSelectata);
    }

    @Override
    public void update(EventImp eventImp) {
        initModel();
    }
}
