package com.example.socialnetwork_1v.controller.Organizat;

import com.example.socialnetwork_1v.domain.validators.PrietenieValidator;
import com.example.socialnetwork_1v.domain.validators.UtilizatorValidator;
import com.example.socialnetwork_1v.repository.database.*;
import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimaPg__Login_Controler {
    private FullDbUtilizatorService fullDbUtilizatorService;

    @FXML
    protected void handdleStart() throws IOException, InterruptedException {
        showLogin();
    }

    public void showLogin() throws IOException {
        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "qwertyuiop";

        DbUtilizatoriRepository userRepository = new DbUtilizatoriRepository(url, username, password, new UtilizatorValidator());
        DbPrieteniRepository prieteniRepository = new DbPrieteniRepository(url, username, password, new PrietenieValidator(userRepository));
        DbMesajeRepository mesajeRepository = new DbMesajeRepository(url, username, password, userRepository);
        DbInvitatiiRepository invitatiiRepository = new DbInvitatiiRepository(url, username, password, userRepository, prieteniRepository, mesajeRepository);
        DbLoginRepository loginRepository = new DbLoginRepository(url, username,password);

        fullDbUtilizatorService = new FullDbUtilizatorService(userRepository, prieteniRepository, mesajeRepository, invitatiiRepository,loginRepository);

        

        Stage primaryStage = new Stage();
        primaryStage.setTitle("Login");
        primaryStage.initModality(Modality.WINDOW_MODAL);

        initView(primaryStage);
        primaryStage.setWidth(667);
        primaryStage.setTitle("Utilizatori");
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("/view/login.fxml"));
        AnchorPane UserLayout = loginLoader.load();

        LoginControler loginControler = loginLoader.getController();
        loginControler.setDbLogin(fullDbUtilizatorService);
        primaryStage.setResizable(true);
        primaryStage.setScene(new Scene(UserLayout));
    }

}
