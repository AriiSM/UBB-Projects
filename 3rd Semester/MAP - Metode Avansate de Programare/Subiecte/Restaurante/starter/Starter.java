package com.example.restaurante.starter;

import com.example.restaurante.controler.Controler;
import com.example.restaurante.controler.ControlerAngajati;
import com.example.restaurante.repository.DbMenuItem_REPO;
import com.example.restaurante.repository.DbOrder_REPO;
import com.example.restaurante.repository.DbTable_REPO;
import com.example.restaurante.service.Service;
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
        String url = "jdbc:postgresql://localhost:5432/Restaurant";
        String username = "postgres";
        String password = "qwertyuiop";

        DbTable_REPO table_repo = new DbTable_REPO(url, username, password);
        DbMenuItem_REPO menuItem_repo = new DbMenuItem_REPO(url, username, password);
        DbOrder_REPO order_repo = new DbOrder_REPO(url, username, password);

        Service service = new Service(table_repo,menuItem_repo,order_repo);

        initView(primaryStage, service);
        primaryStage.show();
    }


    private void initView(Stage primaryStage, Service service) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/angajati.fxml"));
        AnchorPane root1 = loader.load();

        primaryStage.setTitle("Staff");
        ControlerAngajati controlerLogin = loader.getController();
        controlerLogin.setService(service);

        primaryStage.setScene(new Scene(root1));

        service.getTable().forEach(table->{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/table.fxml"));
            try {
                AnchorPane root = fxmlLoader.load();
                Controler tableController = fxmlLoader.getController();
                tableController.setService(service,table);
                Stage centreWindowStage = new Stage();
                Scene scene = new Scene(root);
                centreWindowStage.setTitle("Table "+ table.getId());
                centreWindowStage.setScene(scene);
                centreWindowStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
}
