package com.example.taximetrie.controler;

import com.example.taximetrie.domain.Persoana;
import com.example.taximetrie.domain.Sofer;
import com.example.taximetrie.observer.Observer;
import com.example.taximetrie.observer.event.ClientChEvent;
import com.example.taximetrie.observer.event.Event;
import com.example.taximetrie.observer.event.TaximetristChEvent;
import com.example.taximetrie.service.ServiceFullDb;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.util.Objects;

public class ControlerClient implements Observer<Event> {
    private ServiceFullDb service;
    private Persoana persoana;



    @FXML
    private TextField txt_locatie;
    @FXML
    private Label timpMaxim;
    @FXML
    private Label indicatorMasina;

    public void setService(ServiceFullDb serviceFullDb,Persoana persoana) {
        this.service = serviceFullDb;
        this.persoana = persoana;
        service.addObserver(this);
    }

    @FXML
    private void handleCauta(){
        String locatie = txt_locatie.getText();
        service.notifyObserver(new TaximetristChEvent(persoana.getId(), persoana.getName(),locatie));
    }

    //Creez Eventul pentru Taximetrist
    @FXML
    private void handleCancel(){
        Sofer sofer = service.findOneSoferByIndicator(indicatorMasina.getText());
        service.notifyObserver(new TaximetristChEvent(sofer.getId(),"CANCEL",txt_locatie.getText(),persoana.getName()));
    }

    @FXML
    private void handleAccept(){
        Sofer sofer = service.findOneSoferByIndicator(indicatorMasina.getText());

        service.saveComanda(persoana.getId(), sofer.getId(), LocalDateTime.now());
        service.notifyObserver(new TaximetristChEvent(sofer.getId(),"ACCEPT",txt_locatie.getText(),persoana.getName()));
    }



    //Ceea ce se afiseaza cu ceea ce primesc de la Taximetrist
    @Override
    public void update(Event event) {
        if (event instanceof ClientChEvent) {
            ClientChEvent clientChangeEvent = (ClientChEvent) event;
            if (clientChangeEvent.getTimpMaxim() != null && clientChangeEvent.getIndicativMasina() != null && Objects.equals(clientChangeEvent.getClientId(), persoana.getId())) {
                timpMaxim.setText(clientChangeEvent.getTimpMaxim());
                indicatorMasina.setText(clientChangeEvent.getIndicativMasina());
            }
        }
    }
}
