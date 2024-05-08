package com.example.clienti_locatie_horel_rezervare.controler;

import com.example.clienti_locatie_horel_rezervare.domain.*;
import com.example.clienti_locatie_horel_rezervare.service.ServiceFullDB;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ControlerPersoana {
    private ServiceFullDB service;





    @FXML
    TableView<Persoana> tablePersoana;
    @FXML
    TableColumn<Persoana, Long> column_id;
    @FXML
    TableColumn<Persoana, String> column_nume;
    @FXML
    TableColumn<Persoana, String> column_prenume;


    @FXML
    private ComboBox<String> comboBox_participanti;
    @FXML
    private ComboBox<String> comboBox_drumetii;
    @FXML
    private ComboBox<String> comboBox_Dificultate;
    @FXML
    private ComboBox<String> comboBox_Durata;

    @FXML
    private DatePicker data1;
    @FXML
    private DatePicker data2;
    @FXML
    private TextField drumetieID;
    @FXML
    private TextField coordonatorID;
    @FXML
    private TextField drumID;

    public void setService(ServiceFullDB service) {
        this.service = service;
        initModel();
    }


    ObservableList<Persoana> persoanaObservableList = FXCollections.observableArrayList();
    ObservableList<String> options =
            FXCollections.observableArrayList(
                    Nivel.Incepator.toString(),
                    Nivel.Mediu.toString(),
                    Nivel.Avansat.toString()
            );



    @FXML
    public void initialize() {
        // Setează modul în care fiecare coloană obține datele pentru celule
        column_nume.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNume()));
        column_prenume.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenume()));

        // Setează sursa de date pentru tabel
        tablePersoana.setItems(persoanaObservableList);
    }



    private void initModel() {
        List<Persoana> persoanaList = StreamSupport.stream(service.getPersoanaList().spliterator(), false)
                .map(n -> new Persoana(n.getId(), n.getNume(), n.getPrenume()))
                .collect(Collectors.toList());

        persoanaObservableList.setAll(persoanaList);

        comboBox_participanti.setItems(options);
        comboBox_Dificultate.setItems(options);

        //pentru combo_box drumetii
        List<Drumetie> drumeties = StreamSupport.stream(service.getDrumetieList().spliterator(), false)
                .map(n -> new Drumetie(n.getId(), n.getId_Coordonator(), n.getParticipanti(), n.getNivel(), n.getDurata(), n.getData_Drumetie()))
                .collect(Collectors.toList());
        Integer max = 0;
        for (Drumetie d : drumeties) {
            if (d.getParticipanti().size() > max)
                max = d.getParticipanti().size();
        }
        ObservableList<String> options2 = FXCollections.observableArrayList();
        for (Drumetie d : drumeties) {
            options2.add(d.getDurata().toString());
        }
        ObservableList<String> options1 = FXCollections.observableArrayList();
        for (int i = 0; i <= max; i++)
            options1.add(String.valueOf(i));

        comboBox_drumetii.setItems(options1);
        comboBox_Durata.setItems(options2);

    }

    @FXML
    private void handleParticipantiNivel() {
        String nivel = comboBox_participanti.getValue();
        //System.out.println(nivel);

        List<Participant> participantList = StreamSupport.stream(service.getParticipantList().spliterator(), false)
                .filter(n -> n.getNivel().toString().equals(nivel))
                .map(n -> new Participant(n.getId(), n.getNume(), n.getPrenume(), n.getNivel()))
                .collect(Collectors.toList());



        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  participantList.toString());

    }

    @FXML
    private void handleDrumetii() {
        String nivel = comboBox_drumetii.getValue();

        List<Drumetie> drumetieList = StreamSupport.stream(service.getDrumetieList().spliterator(), false)
                .filter(n -> n.getParticipanti().size() > Integer.parseInt(nivel))
                .map(n -> new Drumetie(n.getId(), n.getId_Coordonator(), n.getParticipanti(), n.getNivel(), n.getDurata(), n.getData_Drumetie()))
                .collect(Collectors.toList());
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  drumetieList.toString());
    }

    @FXML
    private void handleDrumetiiCuprinse() {
        String dificultate = comboBox_Dificultate.getValue();
        String durata = comboBox_Durata.getValue();
        String data1 = this.data1.getValue().toString();
        String data2 = this.data2.getValue().toString();

        System.out.println(data1);
        System.out.println(data2);

        List<Drumetie> drumeties = StreamSupport.stream(service.getDrumetieList().spliterator(), false)
                .filter(n -> n.getNivel().toString().equals(dificultate))
                .filter(n -> n.getDurata().toString().equals(durata))
                .filter(n -> n.getData_Drumetie().toString().compareTo(data1) >= 0)
                .filter(n -> n.getData_Drumetie().toString().compareTo(data2) <= 0)
                .map(n -> new Drumetie(n.getId(), n.getId_Coordonator(), n.getParticipanti(), n.getNivel(), n.getDurata(), n.getData_Drumetie()))
                .collect(Collectors.toList());

        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  drumeties.toString());
    }

    @FXML
    private void handleRecenzie() {
        List<Drumetie> drumeties = StreamSupport.stream(service.getDrumetieList().spliterator(), false)
                .map(n -> new Drumetie(n.getId(), n.getId_Coordonator(), n.getParticipanti(), n.getNivel(), n.getDurata(), n.getData_Drumetie()))
                .collect(Collectors.toList());

        List<Drumetie> filtered = new ArrayList<>();
        for (Drumetie d : drumeties) {
            if (service.findOneCoordonator(d.getId_Coordonator()).getStelute() >= 4.5)
                filtered.add(d);
        }

        System.out.println("DID IT");
        System.out.println(filtered.toString());

        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  filtered.toString());
    }

    @FXML
    private void handleCoordonator() {
        List<Coordonator> coordonators = StreamSupport.stream(service.getCoordonatorList().spliterator(), false)
                .filter(n -> n.getStelute() >= 4)
                .map(n -> new Coordonator(n.getId(), n.getNume(), n.getPrenume(), n.getNivel(), n.getStelute(), n.getRecenzii()))
                .collect(Collectors.toList());
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  coordonators.toString());
    }

    @FXML
    private void handleNumeroasaDrumetie() {
        List<Drumetie> drumeties = StreamSupport.stream(service.getDrumetieList().spliterator(), false)
                .map(n -> new Drumetie(n.getId(), n.getId_Coordonator(), n.getParticipanti(), n.getNivel(), n.getDurata(), n.getData_Drumetie()))
                .collect(Collectors.toList());


        Integer max = 0;
        long id_max =0;
        for (Drumetie d : drumeties) {
            if (d.getParticipanti().size() > max){
                max = d.getParticipanti().size();
                id_max = d.getId();
            }
        }
        Drumetie drumetie=null;
        if(id_max != 0)
            drumetie = service.findOneDrumetie(id_max);

        if(drumetie != null)
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  drumetie.toString());
        else
            MessageAlert.showErrorMessage(null, "Nu exista drumetii");
    }

    @FXML
    private void handleCoordDrumRECENZIE(){
        Long cid = Long.valueOf(coordonatorID.getText());
        Long Did = Long.valueOf(drumetieID.getText());

        List<Drumetie> drumeties = StreamSupport.stream(service.getDrumetieList().spliterator(), false)
                .filter(n-> n.getId() == Did)
                .filter(n-> n.getId_Coordonator() == cid)
                .map(n -> new Drumetie(n.getId(), n.getId_Coordonator(), n.getParticipanti(), n.getNivel(), n.getDurata(), n.getData_Drumetie()))
                .collect(Collectors.toList());
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  drumeties.toString());
    }

    @FXML
    private void handleMedieParticipanti(){
        Long id = Long.valueOf(drumID.getText());
        List<Drumetie> drum =StreamSupport.stream(service.getDrumetieList().spliterator(), false)
                        .map(n -> new Drumetie(n.getId(), n.getId_Coordonator(), n.getParticipanti(), n.getNivel(), n.getDurata(), n.getData_Drumetie()))
                        .collect(Collectors.toList());
        Double medie = 0.0;
        Integer nr_drumetii =0;
        Integer nr_participanti =0;
        for(Drumetie d: drum){
            if(d.getId() == id){
                nr_drumetii++;
                nr_participanti += d.getParticipanti().size();
            }
        }
        medie = Double.valueOf(nr_participanti)/Double.valueOf(nr_drumetii);
        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",  medie.toString());

    }



    ObservableList<String> optionsA = FXCollections.observableArrayList(
                    Nivel.Incepator.toString(),
                    Nivel.Mediu.toString(),
                    Nivel.Avansat.toString()
               );

    @FXML
    private ComboBox<String> combo_aaa;


    @FXML
    private void handleAlegeNivel(){



    }
}



