package com.example.monitorizareangajati.controler;

import com.example.monitorizareangajati.domain.*;
import com.example.monitorizareangajati.service.Service;
import com.example.monitorizareangajati.utils.Observer;
import com.example.monitorizareangajati.utils.events.EventImp;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class AngajatControler implements Observer<EventImp>{
    private Service service;
    private Stage stage;
    private String numeAngajat;
    private String prenumeAngajat;
    Angajat angajat;
    //----------------------------------
    //Prezenta
    @FXML
    private Tab tabProductivitate;
    @FXML
    private Tab tabPrezenta;

    //----------------------------------
    //Tabel Productivitate
    @FXML
    private TableView<AtribuireSarcina> tabelSarcini;
    @FXML
    private TableColumn<AtribuireSarcina, String> columnSarcina;
    @FXML
    private TableColumn<AtribuireSarcina, String> columnStatus;
    private ObservableList<AtribuireSarcina> sarciniList = FXCollections.observableArrayList();

    public void setDb(Service service,String nume,String pernume ,Stage stage) {
        this.service = service;
        this.service.addObserver((Observer<EventImp>) this);
        this.stage = stage;

        this.numeAngajat = nume;
        this.prenumeAngajat = pernume;
        tabProductivitate.setDisable(true);

        stage.setOnCloseRequest(event -> handleWindowClose());

    }

    @FXML
    private void initialize() {
        columnSarcina.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSarcina().getDescriere()));
        columnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
    }

    private void initModel(){
        sarciniList.clear();

        Iterable<AtribuireSarcina> atribuiriSarcina = service.findAllAtribuireSarcinaAngajat(angajat.getId());

        for (AtribuireSarcina atribuireSarcina : atribuiriSarcina) {
                sarciniList.add(atribuireSarcina);
        }
        System.out.println(sarciniList);
        tabelSarcini.setItems(sarciniList);
    }

    private void handleWindowClose() {
        System.out.println(angajat);
        this.angajat.setStatus(StatusAngajat.ABSENT);
        service.updateAngajat(angajat);
        System.out.println(angajat);
    }

    @FXML
    public void handlePrezenta(){
        tabProductivitate.setDisable(false);
        tabProductivitate.getTabPane().getSelectionModel().select(tabProductivitate);
        tabPrezenta.getTabPane().getTabs().remove(tabPrezenta);

        this.angajat = service.findAngajatByNumePrenume(numeAngajat,prenumeAngajat);
        System.out.println(angajat);
        angajat.setStatus(StatusAngajat.PREZENT);
        service.updateAngajat(angajat);
        System.out.println(angajat);
    }

    @FXML
    public void handlePreiaSarcina(){
        AtribuireSarcina sarcinaSelectata = tabelSarcini.getSelectionModel().getSelectedItem();
        sarcinaSelectata.setStatus(StatusSarcina.DOING);
        service.updateAtribuieSarcina(sarcinaSelectata);
    }

    @FXML
    public void handleFinalizareSarcina(){
        AtribuireSarcina sarcinaSelectata = tabelSarcini.getSelectionModel().getSelectedItem();
        sarcinaSelectata.setStatus(StatusSarcina.DONE);
        service.updateAtribuieSarcina(sarcinaSelectata);
    }

    @Override
    public void update(EventImp eventImp) {
        initModel();
    }
}
