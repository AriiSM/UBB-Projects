package com.example.socialnetwork_1v.controller.Organizat.UTILIZATOR;

import com.example.socialnetwork_1v.controller.Organizat.MessageAlert;
import com.example.socialnetwork_1v.domain.Message;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MesajeController {
    //------------Tabel mesaje-------
    @FXML
    TableView<Message> tableMesaje;
    @FXML
    TableColumn<Message, String> coloanaFrom;
    @FXML
    TableColumn<Message, String> coloanaTo;
    @FXML
    TableColumn<Message, String> coloanaMesaj;
    @FXML
    TableColumn<Message, String> coloanaReply;
    @FXML
    TableColumn<Message, String> coloadaData;
    //------------Tabel mesaje-------

    @FXML
    TextField lblMesaj;


    private FullDbUtilizatorService service;
    private String idUtilizator;
    private String chatterUser;
    ObservableList<Message> observerMesaje = FXCollections.observableArrayList();

    public void setService(FullDbUtilizatorService service, String cod1, String cod2) {
        this.service = service;
        this.idUtilizator = cod1;
        this.chatterUser = cod2;
        initModel();
    }

    @FXML
    public void initialize() {
        coloanaFrom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom().getFirst_name()));
        coloanaTo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getToPrenume().toString()));
        coloanaMesaj.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesaj()));
        coloanaReply.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdReply().get().toString()));
        coloadaData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataString()));

        tableMesaje.setItems(observerMesaje);
    }

    private List<Message> getMesajeList() {
        return StreamSupport.stream(service.getAllMesaje().spliterator(), false)
                .map(n -> new Message(n.getId(), n.getMesaj(), n.getFrom(), n.getTo(), n.getDate(), n.getIdReply()))
                .collect(Collectors.toList());
    }

    private void initModel() {
        List<Message> multipleFilter = getMesajeList();

        multipleFilter = multipleFilter
                .stream()
                .filter(o -> (o.getTo().get(0).getId().toString().equals(idUtilizator) &&
                        o.getFrom().getId().toString().equals(chatterUser)) ||
                        (o.getTo().get(0).getId().toString().equals(chatterUser) &&
                                o.getFrom().getId().toString().equals(idUtilizator))

                )
                .toList();

        observerMesaje.setAll(multipleFilter);
        //observerMesaje.setAll(StreamSupport.stream(service.getAllMesaje().spliterator(), false).toList());
    }

    @FXML
    private void handleTrimite(){
        Utilizator from = service.findOneUser(Long.parseLong(idUtilizator));
        List<Utilizator> toList = new ArrayList<>();
        Utilizator to = service.findOneUser(Long.parseLong(chatterUser));
        toList.add(to);
        LocalDateTime ldt = LocalDateTime.now();
        String mesaj = lblMesaj.getText();

        Message message = new Message(service.createID(),mesaj,from,toList,ldt, Optional.empty());
        saveMesaj(message);
        initModel();
    }
    private void saveMesaj(Message message) {
        try {
            Message m = this.service.addMessage(message);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    private void handleReply(){
        Message selected = tableMesaje.getSelectionModel().getSelectedItem();
        if (selected == null) {
            MessageAlert.showErrorMessage(null, "NU ati selectat nici un mesaj");
        } else {
            String replyMesaj = lblMesaj.getText();

            List<Utilizator> all = new ArrayList<>();
            all.add(selected.getFrom());

            Message mesajNou = new Message(service.createID(), replyMesaj, selected.getTo().get(0), all, LocalDateTime.now(), Optional.ofNullable(selected.getId()));
            Message m = this.service.addMessage(mesajNou);
        }
        initModel();

    }
}
