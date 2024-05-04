package com.example.monitorizareangajati.controler;

import com.example.monitorizareangajati.service.Service;
import javafx.stage.Stage;

public class AngajatControler {
    private Service service;
    private Stage stage;
    private String numeAngajat;
    private String prenumeAngajat;

    public void setDb(Service service,String nume,String pernume ,Stage stage) {
        this.service = service;
        this.stage = stage;

        this.numeAngajat = nume;
        this.prenumeAngajat = pernume;
    }
}
