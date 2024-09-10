package com.example.monitorizareangajati;

import com.example.monitorizareangajati.controler.LoginControler;
import com.example.monitorizareangajati.repository.IAngajatRepository;
import com.example.monitorizareangajati.repository.IAtribuieSarcinaRepository;
import com.example.monitorizareangajati.repository.ISarcinaRepository;
import com.example.monitorizareangajati.repository.ISefRepository;
import com.example.monitorizareangajati.repository.jdbc.AngajatDBRepository;
import com.example.monitorizareangajati.repository.jdbc.AtribuieSarcinaDBRepository;
import com.example.monitorizareangajati.repository.jdbc.SarcinaDBRepository;
import com.example.monitorizareangajati.repository.jdbc.SefDBRepository;
import com.example.monitorizareangajati.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainFx extends Application {
    private Service service;
    public static void main(String[] arg){launch();}
    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
            System.out.println("bd.config done!");
            System.out.println(props);
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }
        AngajatDBRepository angajatDBRepository = new AngajatDBRepository(props);
        SarcinaDBRepository sarcinaDBRepository = new SarcinaDBRepository(props);
        SefDBRepository sefDBRepository = new SefDBRepository(props);
        AtribuieSarcinaDBRepository atribuieSarcinaDBRepository = new AtribuieSarcinaDBRepository(props, angajatDBRepository,sarcinaDBRepository);
        service = new Service(angajatDBRepository,atribuieSarcinaDBRepository,sarcinaDBRepository,sefDBRepository);





        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            LoginControler loginControler = fxmlLoader.getController();
            loginControler.setDbLogin(service, primaryStage);
            primaryStage.setResizable(true);


            primaryStage.setWidth(667);
            primaryStage.setTitle("Autentificare");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            // Tratați eroarea aici, cum ar fi afișarea unui mesaj de eroare
        }
    }
}
