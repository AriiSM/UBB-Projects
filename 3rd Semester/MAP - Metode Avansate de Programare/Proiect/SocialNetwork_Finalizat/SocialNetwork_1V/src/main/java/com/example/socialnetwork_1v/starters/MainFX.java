package com.example.socialnetwork_1v.starters;

import com.example.socialnetwork_1v.HelloApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/view/primaPagina.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setWidth(667);
        primaryStage.setTitle("Hello there!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}