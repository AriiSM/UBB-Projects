package com.example.socialnetwork_1v.controller.Mess;

import com.example.socialnetwork_1v.domain.Message;
import com.example.socialnetwork_1v.domain.Utilizator;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

public class ChatController {
    private FullDbUtilizatorService service;
    private Utilizator user_app = null;
    private Utilizator user_comunicate = null;
    private ConversationView cv;
    ObservableList<Message> observerUserMessages = FXCollections.observableArrayList();


    public void setService(FullDbUtilizatorService dbMesaj, Utilizator userApp, Utilizator user_com) throws Exception {
        this.service = dbMesaj;
        this.user_app = userApp;
        this.user_comunicate = user_com;
        cv = new ConversationView(user_comunicate.getFirst_name() + " " + user_comunicate.getLast_name(), user_app.getFirst_name() + " " + user_app.getLast_name());
        vBox.getChildren().add(cv);
        initModel();
        loadMessage();
    }

    @FXML
    private VBox vBox = new VBox();

    @FXML
    public void initialize() {

    }

    public VBox getvBox() {
        return vBox;
    }

    private void initModel() throws Exception {
        //System.out.println(user_app.getFirst_name()+" "+user_app.getLast_name()+" "+user_comunicate.getFirst_name()+" "+user_comunicate.getLast_name());
        observerUserMessages.setAll(service.afisareConversatii(user_app.getFirst_name(), user_app.getLast_name(), user_comunicate.getFirst_name(), user_comunicate.getLast_name()));
    }

    public void loadMessage() {
        for (Message msg : observerUserMessages) {
            if (msg.getFrom().getId().toString().equals(user_app.getId().toString())) {
                cv.sendMessage(msg.getMesaj());
            } else {
                cv.receiveMessage(msg.getMesaj());
            }
        }
    }


}
