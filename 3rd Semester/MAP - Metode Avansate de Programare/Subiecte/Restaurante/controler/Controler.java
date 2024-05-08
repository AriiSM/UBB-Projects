package com.example.restaurante.controler;

import com.example.restaurante.domain.MenuItem;
import com.example.restaurante.domain.Order;
import com.example.restaurante.domain.Status;
import com.example.restaurante.domain.Table;
import com.example.restaurante.observer.Observer;
import com.example.restaurante.observer.event.ComandaChEvent;
import com.example.restaurante.observer.event.Event;
import com.example.restaurante.service.Service;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controler implements Observer<Event> {
    private Service service;
    private Table aceastaMasa;

    @FXML
    Label lbl_antreuri;
    @FXML
    TableView<MenuItem> tableAntreuri;
    @FXML
    TableColumn<MenuItem, String> columnAcategorie;
    @FXML
    TableColumn<MenuItem, String> columnAprice;


    @FXML
    Label lbl_FP;
    @FXML
    TableView<MenuItem> tableFelPrincipal;
    @FXML
    TableColumn<MenuItem, String> columnFPcategorie;
    @FXML
    TableColumn<MenuItem, String> columnFPprice;


    ObservableList<MenuItem> AntreuriObserver = FXCollections.observableArrayList();
    ObservableList<MenuItem> FelPrincipalObserver = FXCollections.observableArrayList();

    public void setService(Service service, Table table) {
        this.service = service;
        service.addObserver(this);
        this.aceastaMasa = table;
        initModel();
    }


    @FXML
    private void initialize() {
        columnAcategorie.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCategory()));
        columnAprice.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPrice().toString() +" "+cellData.getValue().getCurrency()));

        tableAntreuri.setItems(AntreuriObserver);

        columnFPcategorie.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getCategory()));
        columnFPprice.setCellValueFactory(cellData-> new SimpleStringProperty(cellData.getValue().getPrice().toString() +" "+cellData.getValue().getCurrency()));

        tableFelPrincipal.setItems(FelPrincipalObserver);

    }



    private void initModel() {
        String antreu = lbl_antreuri.getText();
        List<MenuItem> antreuriList = StreamSupport.stream(service.getMenuItemList().spliterator(), false)
                .filter(n->n.getCategory().equals(antreu))
                .map(n -> new MenuItem(n.getId(), n.getCategory(), n.getItem(), n.getPrice(), n.getCurrency()))
                .collect(Collectors.toList());
        AntreuriObserver.setAll(antreuriList);


        String fel_principal = lbl_FP.getText();
        List<MenuItem> felPrincipal_list = StreamSupport.stream(service.getMenuItemList().spliterator(), false)
                .filter(n->n.getCategory().equals(fel_principal))
                .map(n -> new MenuItem(n.getId(), n.getCategory(), n.getItem(), n.getPrice(), n.getCurrency()))
                .collect(Collectors.toList());

        FelPrincipalObserver.setAll(felPrincipal_list);

    }

    public Long createID() {
        do {
            boolean bool = true;
            Long id = new Random().nextLong();
            if (id < 0) {
                id *= -1;
            }
            for (Order u : service.getOrderList()) {
                if (id.equals(u.getId())) {
                    bool = false;
                    break;
                }
            }
            if (bool)
                return id;
        } while (true);
    }

    private List<MenuItem> convertStringToMenuItemList(String numeProduse) {
        // Split the string into an array of product names
        String[] produseArray = numeProduse.split(",");

        // Create a new MenuItem for each product name and collect them into a list
        List<MenuItem> produseList = new ArrayList<>();
        for (String produs : produseArray) {
            MenuItem menuItem = service.getMenuItemByName(produs.trim());
            if (menuItem != null) {
                produseList.add(menuItem);
            }
        }

        return produseList;
    }

    @FXML
    private void handlePlaceOrder(){
        String id_masa = aceastaMasa.getId().toString();

        ObservableList<MenuItem> selectedItems1 = tableAntreuri.getSelectionModel().getSelectedItems();
        ObservableList<MenuItem> selectedItems2 = tableFelPrincipal.getSelectionModel().getSelectedItems();
        List<MenuItem> selectedItems1List = new ArrayList<>(selectedItems1);
        List<MenuItem> selectedItems2List = new ArrayList<>(selectedItems2);
        selectedItems1List.addAll(selectedItems2List);

        String data = LocalDateTime.now().toString();

        Long along = createID();



        Order newOrder = new Order(along, aceastaMasa.getId(), selectedItems1List, LocalDateTime.now(), Status.PLACED);

        service.saveOrder(newOrder);

        service.notifyObserver(new ComandaChEvent(id_masa,data,selectedItems1List.toString()));
    }

    @Override
    public void update(Event event) {

    }
}

