package com.example.socialnetwork_1v.controller.Mess;

import javafx.collections.FXCollections;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.w3c.dom.Node;

public class ConversationView extends VBox {
    private String conversationPartner;
    private String conversationUser;
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();

    private Label contactHeaderLeftUser;
    private ScrollPane messageScroller;
    private VBox messageContainer;
    private HBox inputContainer;

    public ConversationView(String conversationPartner, String conversationUser) {
        super(5);
        this.conversationPartner = conversationPartner;
        this.conversationUser = conversationUser;
        setupElements();
    }

    private void setupElements() {
        setupContactHeader();
        setupMessageDisplay();
        setupInputDisplay();
        getChildren().setAll(contactHeaderLeftUser, messageScroller, inputContainer);
        setPadding(new Insets(0));
    }

    private void setupContactHeader() {
        contactHeaderLeftUser = new Label(conversationPartner + "<------------" + conversationUser);
        contactHeaderLeftUser.setAlignment(Pos.TOP_RIGHT);
        contactHeaderLeftUser.setFont(Font.font("Comic Sans MS", 10));
    }

    private void setupMessageDisplay() {
        messageContainer = new VBox(5);
        //Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());

        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setPrefHeight(300);
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);

        //sa coboare jos cand un nou mesaj este trimis :
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    messageScroller.setVvalue(messageScroller.getVmax());
                }
            }
        });
    }

    private void setupInputDisplay(){
        inputContainer = new HBox(5);

        TextField userInput = new TextField();
        userInput.setPromptText("Enter message");

        Button sendMessageButton = new Button("Send");
        sendMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        sendMessageButton.setOnAction(event->{
            sendMessage(userInput.getText());
            userInput.setText("");
        });

        //pentru testarea scopului
        Button receiveMessageButton = new Button("Receive");
        receiveMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        receiveMessageButton.setOnAction(event->{
            receiveMessage(userInput.getText());
            userInput.setText("");
        });

        inputContainer.getChildren().setAll(userInput,sendMessageButton,receiveMessageButton);
    }

    public void sendMessage(String message){
        speechBubbles.add((Node) new SpeechBox(message, SpeechDirection.RIGHT));
    }

    public void receiveMessage(String message){
        speechBubbles.add((Node) new SpeechBox(message, SpeechDirection.LEFT));
    }

}
