package com.example.zboruri.contoler;

import com.example.zboruri.domain.Client;
import com.example.zboruri.domain.Flight;
import com.example.zboruri.domain.Tiket;
import com.example.zboruri.observer.event.Event;
import com.example.zboruri.observer.event.FlightChEvent;
import com.example.zboruri.service.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControlerClient implements com.example.zboruri.observer.Observer<Event> {
    private Service service;
    private Client acestClient;
    //PRIMUL TABEL
    @FXML
    TableView<Tiket> tableTikets;
    @FXML
    TableColumn<Tiket, String> ColumnZboruri;
    @FXML
    TableColumn<Tiket, String> columnData;
    ObservableList<Tiket> tiketObservableList = FXCollections.observableArrayList();
    //==============================================================================================
    //AL DOILEA TABEL
    @FXML
    TableView<Tiket> tabelZbData;
    @FXML
    TableColumn<Tiket, String> columnZbData;
    @FXML
    TableColumn<Tiket, String> ColumnDDData;
    @FXML
    TableColumn<Tiket, String> columnUsername;
    ObservableList<Tiket> tiketObservableListData = FXCollections.observableArrayList();
    //==============================================================================================

    //AL TREILEA TABEL
    @FXML
    TableView<Flight> tabelZboruri;
    @FXML
    TableColumn<Flight, String> columnPlecare;
    @FXML
    TableColumn<Flight, String> columnSosire;
    @FXML
    TableColumn<Flight, String> columnLocuri;
    @FXML
    TableColumn<Flight, String> columnOcupate;
    @FXML
    ComboBox<String> comboFrom;
    @FXML
    ComboBox<String> comboTo;
    @FXML
    DatePicker dataFlight;

    ObservableList<Flight> flightObservableList = FXCollections.observableArrayList();
    private List<Flight> filteredFlightList = new ArrayList<>();

    public void setService(Service service, Client client) {
        this.service = service;
        service.addObserver(this);
        this.acestClient = client;
        initModel();
    }

    @FXML
    public void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ColumnZboruri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId_flight().toString()));
        columnData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurchaseTime().format(formatter)));
        tableTikets.setItems(tiketObservableList);

        columnZbData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId_flight().toString()));
        ColumnDDData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurchaseTime().format(formatter)));
        columnUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        tabelZbData.setItems(tiketObservableListData);

        columnPlecare.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime().format(formatter)));
        columnSosire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLandingTime().format(formatter)));
        columnLocuri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSeats().toString()));
        columnOcupate.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSeats() - service.getTicketsSoldForFlight(cellData.getValue().getId()))));

        tabelZboruri.setItems(flightObservableList);
    }


    private void initModelFiltrare() {
        flightObservableList.setAll(filteredFlightList);
        initialize();
    }

    private void initModel() {
        //pentru biletele achizitionate de acest utilizator
        List<Tiket> tiketList = StreamSupport.stream(service.getTiketList().spliterator(), false)
                .filter(n -> n.getUsername().equals(acestClient.getUsername()))
                .map(n -> new Tiket(n.getId(), n.getUsername(), n.getPurchaseTime(), n.getId_flight()))
                .collect(Collectors.toList());
        tiketObservableList.setAll(tiketList);

        //Pentru filtrarea biletelor de la 24 ianuaria 2024
        List<Tiket> tiketList1 = StreamSupport.stream(service.getTiketList().spliterator(), false)
                //.filter(n->n.getUsername().equals(acestClient.getUsername()))
                .filter(n -> n.getPurchaseTime().getYear() == 2024)
                .filter(n -> n.getPurchaseTime().getMonth() == Month.JANUARY)
                .filter(n -> n.getPurchaseTime().getDayOfMonth() == 24)
                .map(n -> new Tiket(n.getId(), n.getUsername(), n.getPurchaseTime(), n.getId_flight()))
                .collect(Collectors.toList());
        tiketObservableListData.setAll(tiketList1);


        List<Flight> flightList = StreamSupport.stream(service.getFlightList().spliterator(), false)
                .map(n -> new Flight(n.getId(), n.getFrom(), n.getTo(), n.getDepartureTime(), n.getLandingTime(), n.getSeats()))
                .collect(Collectors.toList());
        flightObservableList.setAll(flightList);

        List<String> fromList = flightList.stream()
                .map(Flight::getFrom)
                .collect(Collectors.toList());

        List<String> toList = flightList.stream()
                .map(Flight::getTo)
                .collect(Collectors.toList());

        List<String> uniqueFromList = new ArrayList<>(new HashSet<>(fromList));
        List<String> uniqueToList = new ArrayList<>(new HashSet<>(toList));

        comboFrom.setItems(FXCollections.observableArrayList(uniqueFromList));
        comboTo.setItems(FXCollections.observableArrayList(uniqueToList));
    }


    @FXML
    private void handleSearch() {
        LocalDateTime data = dataFlight.getValue().atStartOfDay();
        String plecare = comboFrom.getValue();
        String sosire = comboTo.getValue();

        filteredFlightList = StreamSupport.stream(service.getFlightList().spliterator(), false)
                .filter(n -> n.getFrom().equals(plecare))
                .filter(n -> n.getTo().equals(sosire))
                .filter(n -> n.getDepartureTime().getYear() == data.getYear() && n.getDepartureTime().getMonth() == data.getMonth() && n.getDepartureTime().getDayOfMonth() == data.getDayOfMonth())
                .map(n -> new Flight(n.getId(), n.getFrom(), n.getTo(), n.getDepartureTime(), n.getLandingTime(), n.getSeats()))
                .collect(Collectors.toList());
        initModelFiltrare();
    }

    @FXML
    private void handleCumpara() {
        Flight flight = tabelZboruri.getSelectionModel().getSelectedItem();
        service.saveTiket(acestClient.getUsername(), LocalDateTime.now(), flight.getId());
        Tiket tiket = StreamSupport.stream(service.getTiketList().spliterator(), false)
                .filter(n -> n.getUsername().equals(acestClient.getUsername()))
                .filter(n -> n.getId_flight().equals(flight.getId()))
                .map(n -> new Tiket(n.getId(), n.getUsername(), n.getPurchaseTime(), n.getId_flight()))
                .collect(Collectors.toList()).get(0);


        int libere = service.getTicketsSoldForFlight(tiket.getId_flight());
        int locuri = flight.getSeats();
        int disponibile = locuri - libere;
        if (disponibile < 0) {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Locuri disponibile = 0", "Alege alt zbor!");
        } else {
            service.notifyObserver(new FlightChEvent(acestClient.getId().toString(),libere, tiket.getId_flight()));
        }
    }


    @Override
    public void update(Event event) {
        if (event instanceof FlightChEvent) {
            FlightChEvent flightChEvent = (FlightChEvent) event;
            if(flightChEvent.getId_client().equals(acestClient.getId().toString())){
                initModelFiltrare();
                initialize();
            }else{
                initModel();
                initialize();
            }
        }

    }
}
