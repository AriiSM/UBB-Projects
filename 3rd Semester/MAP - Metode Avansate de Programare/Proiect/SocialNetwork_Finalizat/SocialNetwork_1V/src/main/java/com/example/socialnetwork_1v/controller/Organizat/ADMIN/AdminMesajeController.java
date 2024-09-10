package com.example.socialnetwork_1v.controller.Organizat.ADMIN;

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
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminMesajeController {

    private FullDbUtilizatorService service;
    Stage dialogStage;
    ObservableList<Message> observableList = FXCollections.observableArrayList();


    @FXML
    TableView<Message> tableView;
    @FXML
    TableColumn<Message, String> tableColumnFrom;
    @FXML
    TableColumn<Message, String> tableColumnTo;
    @FXML
    TableColumn<Message, String> tableColumnMesaj;
    @FXML
    TableColumn<Message, String> tableColumnData;
    @FXML
    TableColumn<Message, String> tableColumnReply;
    //-------------------end TableView fx:id------------------

    @FXML
    TextField lblTO;
    @FXML
    TextField lblFROM;
    @FXML
    TextField txtReply;
    //--------------- reply msg----------------

    @FXML
    private TextField txtFrom;
    @FXML
    private TextField txtTo;
    @FXML
    private TextField txtMesaj;
    //-------------------mesaj-----------


    public void setService(FullDbUtilizatorService dbMesaj, Stage stage) {
        this.service = dbMesaj;
        this.dialogStage = stage;
        //this.service.addObserver(this);
        initModel();
    }

    private void initModel() {
        //TODO
        observableList.setAll(StreamSupport.stream(service.getAllMesaje().spliterator(), false).toList());
    }

    @FXML
    public void initialize() {
        tableColumnMesaj.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMesaj()));
        tableColumnTo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getToPrenume().toString()));
        tableColumnFrom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom().getFirst_name()));
        tableColumnData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataString()));
        tableColumnReply.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdReply().get().toString()));
        tableView.setItems(observableList);

        //lblTO.textProperty().addListener(o -> handleFilter());
        //lblFROM.textProperty().addListener(o -> handleFilter());
    }

    private List<Message> getMesajeList() {
        return StreamSupport.stream(service.getAllMesaje().spliterator(), false)
                .map(n -> new Message(n.getId(), n.getMesaj(), n.getFrom(), n.getTo(), n.getDate(), n.getIdReply()))
                .collect(Collectors.toList());
    }

    @FXML
    private void handleFilter() {
        String numeTO = lblTO.getText();
        String numeFROM = lblFROM.getText();

        List<Message> multipleFilter = getMesajeList();

        if (!Objects.equals(numeTO, "") && !Objects.equals(numeFROM, "")) {
            multipleFilter = multipleFilter
                    .stream()
                    .filter(o -> (service.findOneUser(o.getTo().get(0).getId()).getFirst_name().startsWith(numeTO) &&
                            service.findOneUser(o.getFrom().getId()).getFirst_name().startsWith(numeFROM)) ||
                            (service.findOneUser(o.getTo().get(0).getId()).getFirst_name().startsWith(numeFROM) &&
                                    service.findOneUser(o.getFrom().getId()).getFirst_name().startsWith(numeTO))

                    )
                    .toList();
        }

        observableList.setAll(multipleFilter);
    }

    @FXML
    private void handleReply() {
        Message selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            MessageAlert.showErrorMessage(null, "NU ati selectat nici un mesaj");
        } else {
            String replyMesaj = txtReply.getText();
            List<Utilizator> all = new ArrayList<>();
            all.add(selected.getFrom());
            Message mesajNou = new Message(service.createID(), replyMesaj, selected.getTo().get(0), all, LocalDateTime.now(), Optional.ofNullable(selected.getId()));

            Message m = this.service.addMessage(mesajNou);
            if (m != null){
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Slavare mesajului", "Mesajul a fost salvat");
            }
        }
        initModel();
    }

    @FXML
    private void handleTrimite_Mesaj(){
        String from = txtFrom.getText();
        String[] prenumeNumeFrom = from.split(" ");
        Utilizator user1 = service.findByNumePrenume(prenumeNumeFrom[1], prenumeNumeFrom[0]);

        String to = txtTo.getText();
        String[] prenumeNumeTo = to.split(" ");
        Utilizator user2 = service.findByNumePrenume(prenumeNumeTo[1], prenumeNumeTo[0]);

        String mesaj = txtMesaj.getText();

        List<Utilizator> usersList = new ArrayList<>();
        usersList.add(user2);

        LocalDateTime ldt = LocalDateTime.now();

        Message message1 = new Message(service.createID(),mesaj, user1, usersList, ldt, Optional.empty());
        saveMesaj(message1);
        initModel();
    }


    private void saveMesaj(Message message) {
        try {
            Message m = this.service.addMessage(message);
            if (m == null) {
                dialogStage.close();
            }
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Slavare mesajului", "Mesajul a fost salvat");
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
