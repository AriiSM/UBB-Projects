package com.example.taximetrie.starter;

import com.example.taximetrie.controler.ControlerLogin;
import com.example.taximetrie.repository.DbComanda_REPO;
import com.example.taximetrie.repository.DbPersoana_REPO;
import com.example.taximetrie.repository.DbSofer_REPO;
import com.example.taximetrie.service.ServiceFullDb;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Starter extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/Taximetrie";
        String username = "postgres";
        String password = "qwertyuiop";

        DbPersoana_REPO persoana_repo = new DbPersoana_REPO(url, username, password);
        DbSofer_REPO sofer_repo = new DbSofer_REPO(url, username, password);
        DbComanda_REPO comanda_repo = new DbComanda_REPO(url, username, password);

        ServiceFullDb service = new ServiceFullDb(persoana_repo,sofer_repo,comanda_repo);

        primaryStage.setTitle("Login");
        initView(primaryStage, service);
        primaryStage.show();
    }

    private void initView(Stage primaryStage, ServiceFullDb service) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/login.fxml"));
            AnchorPane root = loader.load();

            ControlerLogin controlerLogin = loader.getController();
            controlerLogin.setService(service);

            primaryStage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
