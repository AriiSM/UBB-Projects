package com.example.monitorizareangajati.controler;

import com.example.monitorizareangajati.service.Service;
import javafx.stage.Stage;

public class SefControler {
    private Service service;
    private Stage stage;

    private String numeSef;
    private String prenumeSef;

    public void setDb(Service service,String nume,String pernume ,Stage stage) {
        this.service = service;
        this.stage = stage;

        this.numeSef = nume;
        this.prenumeSef = pernume;
    }
}
