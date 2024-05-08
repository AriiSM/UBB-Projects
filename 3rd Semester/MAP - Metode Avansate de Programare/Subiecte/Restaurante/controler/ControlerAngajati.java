package com.example.restaurante.controler;

import com.example.restaurante.domain.MenuItem;
import com.example.restaurante.domain.Order;
import com.example.restaurante.domain.Status;
import com.example.restaurante.observer.Observer;
import com.example.restaurante.observer.event.ComandaChEvent;
import com.example.restaurante.observer.event.Event;
import com.example.restaurante.service.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.GenericArrayType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControlerAngajati implements Observer<Event> {

    private Service service;
    @FXML
    TableView<Order> tabelStaff;
    @FXML
    TableColumn<Order, String> columnIdMasa;
    @FXML
    TableColumn<Order, String> columnData;
    @FXML
    TableColumn<Order, String> columnProduse;
    ObservableList<Order> AntreuriObserver = FXCollections.observableArrayList();


    public void setService(Service service) {
        this.service = service;
        service.addObserver(this);
    }


    private void initialize(){
        columnIdMasa.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getId_table().toString()));
        columnData.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getData().toString()));
        columnProduse.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getMenuItemList().toString()));

        tabelStaff.setItems(AntreuriObserver);
    }

    private void initModel(){
        List<Order> orderList = StreamSupport.stream(service.getOrderList().spliterator(), false)
                .map(n -> new Order(n.getId(), n.getId_table(),n.getMenuItemList(),n.getData(),n.getStatus()))
                .collect(Collectors.toList());

        Collections.sort(orderList, Comparator.comparing(Order::getData));

        AntreuriObserver.setAll(orderList);
    }




    @Override
    public void update(Event event) {
        initModel();
        initialize();
    }
}
