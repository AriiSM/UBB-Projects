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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControlerClient implements com.example.zboruri.observer.Observer<Event> {
    private Service service;
    private Client acestClient;

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

    private int pageSize = 5;
    private int currentPage = 0;
    private int totalNrOfElemsFriend = 0;
    ObservableList<Flight> flightObservableList = FXCollections.observableArrayList();

    @FXML
    Button btn_prev;
    @FXML
    Button btn_next;
    @FXML
    private TextField txt_pagina;
    private List<Flight> filteredFlightList = new ArrayList<>();

    public void setService(Service service, Client client) {
        this.service = service;
        service.addObserver(this);
        this.acestClient = client;
        initModel();
    }

    @FXML
    public void initialize() {
        columnPlecare.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartureTime().toString()));
        columnSosire.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLandingTime().toString()));
        columnLocuri.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSeats().toString()));
        columnOcupate.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getSeats()- service.getTicketsSoldForFlight(cellData.getValue().getId()))));

        tabelZboruri.setItems(flightObservableList);
        txt_pagina.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int newPageSize = Integer.parseInt(newValue);
                if (newPageSize > 0) {
                    this.pageSize = newPageSize;
                    this.currentPage = 0;
                    initModelPaginat(flightObservableList);
                } else {
                    System.out.println("Page size must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid page size. Please enter a valid integer.");
            }
        });
    }

    private void initModelPaginat(List<Flight> lista_pt_paginare) {
        if (lista_pt_paginare != null && !lista_pt_paginare.isEmpty()) {
            int maxPageFriend = (int) Math.ceil((double) lista_pt_paginare.size() / pageSize) - 1;

            if (currentPage > maxPageFriend) {
                currentPage = maxPageFriend;
            }

            int start = currentPage * pageSize;
            int end = Math.min(start + pageSize, lista_pt_paginare.size());
            List<Flight> pageContent = lista_pt_paginare.subList(start, end);

            if (flightObservableList != null) {
                flightObservableList.setAll(pageContent);
            } else {
                flightObservableList = FXCollections.observableArrayList(pageContent);
            }

            totalNrOfElemsFriend = lista_pt_paginare.size();

            btn_prev.setDisable(currentPage == 0);
            btn_next.setDisable((currentPage + 1) * pageSize >= totalNrOfElemsFriend);
        }
    }

    private void initModel() {
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
        initModelPaginat(filteredFlightList);
    }


    @FXML
    private void handleCumpara() {
        Flight flight = tabelZboruri.getSelectionModel().getSelectedItem();
        service.saveTiket(acestClient.getUsername(), LocalDateTime.now(), flight.getId());
        Tiket tiket = StreamSupport.stream(service.getTiketList().spliterator(), false)
                .filter(n-> n.getUsername().equals(acestClient.getUsername()))
                .filter(n->n.getId_flight().equals(flight.getId()))
                .map(n -> new Tiket(n.getId(),n.getUsername(),n.getPurchaseTime(),n.getId_flight()))
                .collect(Collectors.toList()).get(0);
        int libere = service.getTicketsSoldForFlight(tiket.getId_flight());
        service.notifyObserver(new FlightChEvent(libere,tiket.getId_flight()));
    }

    @FXML
    public void handlePrec(ActionEvent ev) {
        currentPage--;
        initModelPaginat(filteredFlightList);
    }

    @FXML
    public void handleNext(ActionEvent ev) {
        currentPage++;
        initModelPaginat(filteredFlightList);
    }

    @Override
    public void update(Event event) {
        initialize();
        initModelPaginat(filteredFlightList);
    }
}
