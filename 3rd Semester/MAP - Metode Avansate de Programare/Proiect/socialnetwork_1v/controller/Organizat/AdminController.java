package com.example.socialnetwork_1v.controller.Organizat;

import com.example.socialnetwork_1v.controller.Mess.ChatController;
import com.example.socialnetwork_1v.controller.Mess.ChatManager;
import com.example.socialnetwork_1v.controller.Organizat.ADMIN.AdminAddUtilizatorController;
import com.example.socialnetwork_1v.controller.Organizat.ADMIN.AdminInvitatiiController;
import com.example.socialnetwork_1v.controller.Organizat.ADMIN.AdminMesajeController;
import com.example.socialnetwork_1v.domain.Prietenie;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.repository.database.paging.Page;
import com.example.socialnetwork_1v.repository.database.paging.Pageable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import com.example.socialnetwork_1v.utils.Observer;
import com.example.socialnetwork_1v.utils.events.EditUtilizatorEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminController implements Observer<EditUtilizatorEvent> {
    FullDbUtilizatorService service;
    ObservableList<Utilizator> observableUser = FXCollections.observableArrayList();
    ObservableList<Prietenie> observableListPrietenie = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator, String> last_name;
    @FXML
    TableColumn<Utilizator, String> first_name;
    @FXML
    TableColumn<Utilizator, String> friends;
//   -------pt tableview UTILIZATORI------


    @FXML
    TableView<Prietenie> tableViewPrietenie;
    @FXML
    TableColumn<Prietenie, String> tableColumnUser1;
    @FXML
    TableColumn<Prietenie, String> tableColumnUser2;
    @FXML
    TableColumn<Prietenie, String> tableColumnDate;
// --------------pt tableview PRIETENIE---------------


    @FXML
    private TextField lblPgUser;
    @FXML
    private TextField lblPgFriend;

    private int pageSizeUser = 5;
    private int currentPageUser = 0;
    private int pageSizeFriend = 5;
    private int currentPageFriend = 0;
    private int totalNrOfElemsUser = 0;
    private int totalNrOfElemsFriend = 0;

    @FXML
    Button prevButtonUser;
    @FXML
    Button nextButtonUser;
    @FXML
    Button nextButtonFriend;
    @FXML
    Button prevButtonFriend;

    public void setDbUtilizator(FullDbUtilizatorService dbUtilizator) {
        this.service = dbUtilizator;
        this.service.addObserver(this);
        initModel();
    }


    @FXML
    public void initialize() {
        last_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLast_name()));
        first_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirst_name()));
        //friends.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFriendsPrenume().toString()));

        tableColumnUser1.setCellValueFactory(cellData -> new SimpleStringProperty(service.findOneUser(cellData.getValue().getId().getLeft()).getFirst_name()));
        tableColumnUser2.setCellValueFactory(cellData -> new SimpleStringProperty(service.findOneUser(cellData.getValue().getId().getRight()).getFirst_name()));
        tableColumnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataString()));

        tableView.setItems(observableUser);
        tableViewPrietenie.setItems(observableListPrietenie);

        lblPgUser.textProperty().addListener(o -> updatePageUser());
        lblPgFriend.textProperty().addListener(o -> updatePageFriend());
    }

    @FXML
    public void handlePrecUser(ActionEvent ev) {
        currentPageUser--;
        initModelUser();
    }

    @FXML
    public void handleNextUser(ActionEvent ev) {
        currentPageUser++;
        initModelUser();
    }

    @FXML
    public void handlePrecFriend(ActionEvent ev) {
        currentPageFriend--;
        initModelFriends();
    }

    public void handleNextFriend(ActionEvent ev) {
        currentPageFriend++;
        initModelFriends();
    }


    private void updatePageFriend() {
        if (lblPgFriend.getText().isEmpty() || Integer.parseInt(lblPgFriend.getText()) == 0) {
            initModelFriends();
        } else {
            this.pageSizeFriend = Integer.parseInt(lblPgFriend.getText());
            this.currentPageFriend = 0;
            initModelFriends();
        }
    }

    private void updatePageUser() {
        if (lblPgUser.getText().isEmpty() || Integer.parseInt(lblPgUser.getText()) == 0) {
            initModelUser();
        } else {
            this.pageSizeUser = Integer.parseInt(lblPgUser.getText());
            this.currentPageUser = 0;
            initModelUser();
        }
    }

    private void initModelFriends() {
        Page<Prietenie> pageFriend = service.findAllFriendsPage(new Pageable(currentPageFriend, pageSizeFriend));
        int maxPageFriend = (int) Math.ceil((double) pageFriend.getTotalNrOfElems() / pageSizeFriend) - 1;

        if (currentPageFriend > maxPageFriend) {
            currentPageFriend = maxPageFriend;

            pageFriend = service.findAllFriendsPage(new Pageable(currentPageFriend, pageSizeFriend));
        }

        observableListPrietenie.setAll(StreamSupport.stream(pageFriend.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));
        totalNrOfElemsFriend = pageFriend.getTotalNrOfElems();

        prevButtonFriend.setDisable(currentPageFriend == 0);
        nextButtonFriend.setDisable((currentPageFriend + 1) * pageSizeFriend >= totalNrOfElemsFriend);
    }

    private void initModelUser() {
        Page<Utilizator> pageUsers = service.findAllOnePage(new Pageable(currentPageUser, pageSizeUser));

        int maxPageUser = (int) Math.ceil((double) pageUsers.getTotalNrOfElems() / pageSizeUser) - 1;

        if (currentPageUser > maxPageUser) {
            currentPageUser = maxPageUser;

            pageUsers = service.findAllOnePage(new Pageable(currentPageUser, pageSizeUser));
        }

        observableUser.setAll(StreamSupport.stream(pageUsers.getElementsOnPage().spliterator(),
                false).collect(Collectors.toList()));

        totalNrOfElemsUser = pageUsers.getTotalNrOfElems();

        prevButtonUser.setDisable(currentPageUser == 0);
        nextButtonUser.setDisable((currentPageUser + 1) * pageSizeUser >= totalNrOfElemsUser);
    }

    private void initModel() {
        initModelUser();
        initModelFriends();
    }


    @Override
    public void update(EditUtilizatorEvent editUtilizatorEvent) {
        initModel();
    }


    @FXML
    public void handleDelete(ActionEvent ev) throws Exception {
        Utilizator selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.stergeUtilizator(selected.getFirst_name(), selected.getLast_name());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Studentul a fost sters cu succes!");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student!");
        }
    }

    @FXML
    public void handleUpdate(ActionEvent ev) throws IOException {
        Utilizator selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showAddEditUtilizator(selected);
        } else {
            MessageAlert.showErrorMessage(null, "NU ati selectat nici un student");
        }
    }


    @FXML
    public void handleAdd(ActionEvent ev) throws IOException {
        showAddEditUtilizator(null);

    }

    @FXML
    public void handleMesaje(ActionEvent e) throws Exception {
        //cel tabelat
        showMesajeUtilizator();

        //incercarea de chat
        //showChatUsers();
    }

    @FXML
    private TextField textFieldUser1;

    @FXML
    private TextField textFieldUser2;

    public void showChatUsers() throws Exception {
        //nume + prenume
        String txtuser1 = textFieldUser1.getText();
        String[] txtListUser1 = txtuser1.split(" ");

        String txtuser2 = textFieldUser2.getText();
        String[] txtListUser2 = txtuser2.split(" ");

        if (txtuser1.isEmpty() || txtuser2.isEmpty()) {
            MessageAlert.showErrorMessage(null, "Completați ambele câmpuri pentru utilizatori!");
            return;
        }

        Utilizator user1 = service.findByNumePrenume(txtListUser1[0], txtListUser1[1]);
        Utilizator user2 = service.findByNumePrenume(txtListUser2[0], txtListUser2[1]);


        if (user1 == null || user2 == null) {
            MessageAlert.showErrorMessage(null, "Unul sau ambii utilizatori nu există!");
            return;
        }
        service.sendUseri(user1, user2);
        ChatController messageController = new ChatController();

        messageController.setService(service, user1, user2);

        // Afiseaza fereastra
        Stage conversationStage = new Stage();
        conversationStage.setTitle("Conversație cu " + txtListUser1[1] + " " + txtListUser2[1]);
        conversationStage.setScene(new Scene(new VBox(messageController.getvBox()), 400, 400));
        conversationStage.show();
        ChatManager.openWindow(conversationStage, messageController);

        conversationStage.setOnCloseRequest(event -> {
            // La închiderea ferestrei, apelează metoda pentru închiderea ferestrei în ChatManager
            ChatManager.closeWindow(conversationStage, messageController);
        });
    }

    public void showMesajeUtilizator() throws IOException {
        try {
            FXMLLoader loaderm = new FXMLLoader();
            loaderm.setLocation(getClass().getResource("/view/message.fxml"));

            AnchorPane rootm = loaderm.load();

            Stage stage = new Stage();
            stage.setTitle("Mesaje");
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(rootm);
            stage.setScene(scene);

            AdminMesajeController mesajeUtilizatorController = loaderm.getController();
            mesajeUtilizatorController.setService(service, stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleInvite(ActionEvent e) {
        showInvites();
    }


    public void showAddEditUtilizator(Utilizator user) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/editUtilizator.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Utilizator");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            AdminAddUtilizatorController editAddUtilizatorController = loader.getController();
            editAddUtilizatorController.setService(service, dialogStage, user);

            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showInvites() {
        try {
            FXMLLoader loardeInvite = new FXMLLoader();
            loardeInvite.setLocation(getClass().getResource("/view/invitatii.fxml"));

            AnchorPane rootInvite = loardeInvite.load();

            Stage stage = new Stage();
            stage.setTitle("Mesaje");
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(rootInvite);
            stage.setScene(scene);

            AdminInvitatiiController mesajeUtilizatorController = loardeInvite.getController();
            mesajeUtilizatorController.setService(service, stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
