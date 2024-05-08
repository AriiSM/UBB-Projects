package com.example.taximetrie.controler;

import com.example.taximetrie.domain.Comanda;
import com.example.taximetrie.domain.Persoana;
import com.example.taximetrie.domain.Sofer;
import com.example.taximetrie.observer.Observer;
import com.example.taximetrie.observer.event.ClientChEvent;
import com.example.taximetrie.observer.event.Event;
import com.example.taximetrie.observer.event.TaximetristChEvent;
import com.example.taximetrie.repository.pagini.Page;
import com.example.taximetrie.repository.pagini.Pageable;
import com.example.taximetrie.service.ServiceFullDb;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControlerTaximetrist implements Observer<Event> {
    ServiceFullDb service;
    private Long id_taxi;

    @FXML
    private DatePicker data1;
    @FXML
    private Button btn_finel;


    @FXML
    private TableView<Persoana> tabelClienti;
    @FXML
    private TableColumn<Persoana, String> columnUsername;
    @FXML
    private TableColumn<Persoana, String> columnnume;
    @FXML
    private TextField txt_pagina;
    @FXML
    private Button btn_prev;
    @FXML
    private Button btn_next;
    private int pageSize = 5;
    private int currentPage = 0;
    private int totalNrOfElemsFriend = 0;
    ObservableList<Persoana> observablePersoana = FXCollections.observableArrayList();
    ObservableList<Persoana> observabledCereri = FXCollections.observableArrayList();


    @FXML
    private ListView comenzi;
    private Sofer currentSofer;
    private Long selectedClientId;
    @FXML
    private TextField timpMaxim;


    public void setService(ServiceFullDb serviceFullDb, Long id, Sofer taximetrist) {
        this.service = serviceFullDb;
        service.addObserver(this);
        this.currentSofer = taximetrist;
        this.id_taxi = id;
        initModel();
    }

    @FXML
    public void initialize() {
        columnUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        columnnume.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        tabelClienti.setItems(observablePersoana);
        txt_pagina.textProperty().addListener(o -> updatePage());

        //List<Persoana> clienti = service.findOneSofer(id_taxi).get().getListClienti();
        //columnCereri.setCellValueFactory((Callback<TableColumn.CellDataFeatures<Persoana, String>, ObservableValue<String>>) new SimpleStringProperty(service.findOneSofer(id_taxi).get().getListClienti().toString()));

    }

    private void initModel() {
        //initModelCereri();
        Page<Persoana> pageFriend = service.findAllPersoanaPage(new Pageable(currentPage, pageSize));
        int maxPageFriend = (int) Math.ceil((double) pageFriend.getTotalNrOfElems() / pageSize) - 1;

        if (currentPage > maxPageFriend) {
            currentPage = maxPageFriend;

            pageFriend = service.findAllPersoanaPage(new Pageable(currentPage, pageSize));
        }

        observablePersoana.setAll(StreamSupport.stream(pageFriend.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));

        totalNrOfElemsFriend = pageFriend.getTotalNrOfElems();

        btn_prev.setDisable(currentPage == 0);
        btn_next.setDisable((currentPage + 1) * pageSize >= totalNrOfElemsFriend);
    }


    private void updatePage() {
        if (txt_pagina.getText().isEmpty() || Integer.parseInt(txt_pagina.getText()) == 0) {
            initModel();
        } else {
            this.pageSize = Integer.parseInt(txt_pagina.getText());
            this.currentPage = 0;
            initModel();
        }
    }


    @FXML
    public void handlePrec(ActionEvent ev) {
        currentPage--;
        initModel();
    }

    @FXML
    public void handleNext(ActionEvent ev) {
        currentPage++;
        initModel();
    }

    @FXML
    public void handleComenziData() {
        LocalDate data1 = this.data1.getValue();
        List<Comanda> comandas = StreamSupport.stream(service.getComandaList().spliterator(), false)
                .filter(n -> n.getId_sofer().equals(id_taxi))
                .map(n -> new Comanda(n.getId(), n.getId_persoana(), n.getId_sofer(), n.getData()))
                .collect(Collectors.toList());

        List<Comanda> filtered = new ArrayList<>();
        for (Comanda c : comandas) {
            if (c.getData().getYear() == data1.getYear() && c.getData().getMonth() == data1.getMonth() && c.getData().getDayOfMonth() == data1.getDayOfMonth())
                filtered.add(c);
        }
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns", filtered.toString());
    }

    @FXML
    public void handleFinel() {
        List<Comanda> comandas = StreamSupport.stream(service.getComandaList().spliterator(), false)
                .filter(n -> n.getId_sofer().equals(id_taxi))
                .map(n -> new Comanda(n.getId(), n.getId_persoana(), n.getId_sofer(), n.getData()))
                .collect(Collectors.toList());


        Map<Long, Integer> dictionary = new HashMap<>();
        for (Comanda c : comandas) {
            if (dictionary.containsKey(c.getId_persoana())) {
                dictionary.put(c.getId_persoana(), dictionary.get(c.getId_persoana()) + 1);
            } else {
                dictionary.put(c.getId_persoana(), 1);
            }
        }
        int max = 0;
        Long id = 0L;
        for (var entry : dictionary.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                id = entry.getKey();
            }
        }

        Persoana persoana = service.findOnePersoana(id).get();
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns", persoana.toString());
    }


    @FXML
    public void handleMediaClientilor() {
        List<Comanda> comandas1 = StreamSupport.stream(service.getComandaList().spliterator(), false)
                .map(n -> new Comanda(n.getId(), n.getId_persoana(), n.getId_sofer(), n.getData()))
                .collect(Collectors.toList());


        LocalDateTime dateE = LocalDateTime.now();
        LocalDateTime dateS = dateE.minusMonths(3);

        List<Comanda> comandas2 = StreamSupport.stream(service.getComandaList().spliterator(), false)
                .filter(n -> n.getId_sofer().equals(id_taxi))
                .filter(n -> n.getData().isAfter(dateS))
                .filter(n -> n.getData().isBefore(dateE))
                .map(n -> new Comanda(n.getId(), n.getId_persoana(), n.getId_sofer(), n.getData()))
                .collect(Collectors.toList());

        Double media = (double) (comandas1.size() / comandas2.size());
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns", media.toString());

    }

    //Ceea ce trimmit spre Client
    @FXML
    private void handleOnorezComanda() {
        String timpMax = timpMaxim.getText();

        String nume_locatie = comenzi.getSelectionModel().getSelectedItems().toString();
        nume_locatie = nume_locatie.substring(1, nume_locatie.length() - 1);
        String[] nume = nume_locatie.split(" ");
        Long idClient = service.findOnePersoanaByNume(nume[0]).getId();

        service.notifyObserver(new ClientChEvent(timpMax, currentSofer.getIndicativMasina(), idClient));
        //comenzi.getItems().remove(nume_locatie);
    }

    //Ceea ce afisez de la client
    @Override
    public void update(Event event) {
        if (event instanceof TaximetristChEvent) {
            TaximetristChEvent taximetristChangeEvent = (TaximetristChEvent) event;

            if (taximetristChangeEvent.getNumeClient() != null && taximetristChangeEvent.getLocatie() != null && taximetristChangeEvent.getRaspuns() == null) {
                comenzi.getItems().add(taximetristChangeEvent.getNumeClient() + " " + taximetristChangeEvent.getLocatie());
                selectedClientId = taximetristChangeEvent.getClientId();
            } else if (taximetristChangeEvent.getRaspuns() != null && taximetristChangeEvent.getRaspuns().equals("ACCEPT")) {
                String itemToRemove = taximetristChangeEvent.getNumeClient() + " " + taximetristChangeEvent.getLocatie();
                comenzi.getItems().remove(itemToRemove);
            } else if (taximetristChangeEvent.getRaspuns() != null && taximetristChangeEvent.getSoferid().equals(currentSofer.getId())) {
                if (taximetristChangeEvent.getRaspuns().equals("CANCEL")) {
                    String itemToRemove = taximetristChangeEvent.getNumeClient() + " " + taximetristChangeEvent.getLocatie();
                    comenzi.getItems().remove(itemToRemove);
                }

            }
        }
    }
}

