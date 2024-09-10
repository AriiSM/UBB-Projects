//package com.example.socialnetwork_1v.controller;
//
//import com.example.socialnetwork_1v.controller.Organizat.AdminController;
//import com.example.socialnetwork_1v.domain.validators.PrietenieValidator;
//import com.example.socialnetwork_1v.domain.validators.UtilizatorValidator;
//import com.example.socialnetwork_1v.repository.database.*;
//import com.example.socialnetwork_1v.service.FullDbUtilizatorService;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.layout.AnchorPane;
//import javafx.stage.Modality;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//import static java.lang.Thread.sleep;
//
//public class StartController {
//
//    FullDbUtilizatorService fullDbUtilizatorService;
//
//
//
//    public void showUtilizatorController() throws IOException {
//        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
//        String username = "postgres";
//        String password = "qwertyuiop";
//
//        DbUtilizatoriRepository userRepository = new DbUtilizatoriRepository(url, username, password, new UtilizatorValidator());
//        DbPrieteniRepository prieteniRepository = new DbPrieteniRepository(url, username, password, new PrietenieValidator(userRepository));
//        DbMesajeRepository mesajeRepository = new DbMesajeRepository(url, username, password, userRepository);
//        DbInvitatiiRepository invitatiiRepository = new DbInvitatiiRepository(url, username, password, userRepository, prieteniRepository, mesajeRepository);
//        DbLoginRepository loginRepository = new DbLoginRepository(url, username,password);
//
//        fullDbUtilizatorService = new FullDbUtilizatorService(userRepository, prieteniRepository, mesajeRepository, invitatiiRepository,loginRepository);
//
//
//        //merge! :
//        //fullDbUtilizatorService.getAllUsers().forEach(System.out::println);
//
//        Stage primaryStage = new Stage();
//        primaryStage.setTitle("Edit Utilizator");
//        primaryStage.initModality(Modality.WINDOW_MODAL);
//
//        initView(primaryStage);
//        primaryStage.setWidth(667);
//        primaryStage.setTitle("Utilizatori");
//        primaryStage.show();
//
//    }
//
//    private void initView(Stage primaryStage) throws IOException {
//        FXMLLoader userLoader = new FXMLLoader();
//        userLoader.setLocation(getClass().getResource("/view/utilizator.fxml"));
//        AnchorPane UserLayout = userLoader.load();
//
//        AdminController utilizatorController = userLoader.getController();
//        utilizatorController.setDbUtilizator(fullDbUtilizatorService);
//        primaryStage.setResizable(true);
//        primaryStage.setScene(new Scene(UserLayout));
//    }
//}
