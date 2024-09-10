package mpp;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mpp.Exceptions.GenericException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.stream.Collectors;

public class MainController implements IObserver {

    private IServices service;
    private Persoana jucator;

    @FXML
    private Label lblStatus;


    @FXML
    private Label lbCuv1;
    @FXML
    private Label lbCuv2;
    @FXML
    private Label lbcuv3;
    @FXML
    private Label lbcuv4;
    @FXML
    private Label lbLitere;

    @FXML
    private Label lbScore;

    String cuv1;
    String cuv2;
    String cuv3;
    String cuv4;

    @FXML
    private TextField txtCuvant;

    private String formattedDateTime = null;
    private LocalDateTime startTime;
    private Configurare cuvant;
    private Integer attemps;
    private Integer score;

    private Integer nrLitereGhicite;

    @FXML
    private TableView<JucatoriGames> tabelClasament;
    @FXML
    private TableColumn<JucatoriGames, String> colUsername;
    @FXML
    private TableColumn<JucatoriGames, String> colDataOra;
    @FXML
    private TableColumn<JucatoriGames, String> colPuncte;
    ObservableList<JucatoriGames> jucatoriGamesObservableList = FXCollections.observableArrayList();


    public void setService(Persoana jucator, IServices service, Stage stage) {
        this.jucator = jucator;
        this.service = service;
        initModelTabel();
        initializareCuvant();
    }

    private void initModelTabel() {
        jucatoriGamesObservableList.clear();
        populareTabel();
        initialize();

    }

    private void populareTabel() {
        Iterable<JucatoriGames> jucatoriGamesIterable = null;
        try {
            jucatoriGamesIterable = service.findAllJucatoriGames();
            System.out.println(jucatoriGamesIterable);

        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        jucatoriGamesIterable.forEach(jucatoriGamesObservableList::add);
    }


    @FXML
    public void initialize() {
        tabelClasament.setItems(jucatoriGamesObservableList);
        colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJucator().getUsername()));
        colDataOra.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getDurata()));
        colPuncte.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getGame().getPuncte())));

    }


    private void initializareCuvant() {
        attemps = 0;
        score = 0;
        nrLitereGhicite = 0;
        lblStatus.setText("Status");
        lbScore.setText("Score: " + score);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        startTime = LocalDateTime.now();
        formattedDateTime = startTime.format(formatter);


        try {
            cuvant = service.findOneCuvant();

        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        String cuvinte = cuvant.getCuvant();
        String litere = cuvant.getLitere();

        String[] cuvinteArray = cuvinte.split(" ");

        cuv1 = cuvinteArray[0];
        cuv2 = cuvinteArray[1];
        cuv3 = cuvinteArray[2];
        cuv4 = cuvinteArray[3];

        lbCuv1.setText("---");
        lbCuv2.setText("---");
        lbcuv3.setText("---");
        lbcuv4.setText("---");


        lbLitere.setText(litere);
    }

    private int compara(String proposedWord, String targetWord) {
        int score = 0;
        for (int i = 0; i < Math.min(proposedWord.length(), targetWord.length()); i++) {
            if (proposedWord.charAt(i) == targetWord.charAt(i)) {
                score++;
            }
        }
        return score;
    }

    @FXML
    private void handleIncearca() {
        attemps++;
        String cuvantPropus = txtCuvant.getText();
        if (cuvantPropus.equals(cuv1) && lbCuv1.getText().equals("---")) {
            nrLitereGhicite++;
            score += cuv1.length();
            lblStatus.setText("Cuvant ghicit!");
            lbCuv1.setText(cuv1);
            lbScore.setText("Score: " + score);
        } else if (cuvantPropus.equals(cuv2) && lbCuv2.getText().equals("---")) {
            nrLitereGhicite++;
            score += cuv2.length();
            lblStatus.setText("Cuvant ghicit!");
            lbCuv2.setText(cuv2);
            lbScore.setText("Score: " + score);
        } else if (cuvantPropus.equals(cuv3) && lbcuv3.getText().equals("---")) {
            nrLitereGhicite++;
            score += cuv3.length();
            lblStatus.setText("Cuvant ghicit!");
            lbcuv3.setText(cuv3);
            lbScore.setText("Score: " + score);
        } else if (cuvantPropus.equals(cuv4) && lbcuv4.getText().equals("---")) {
            nrLitereGhicite++;
            score += cuv4.length();
            lblStatus.setText("Cuvant ghicit!");
            lbcuv4.setText(cuv4);
            lbScore.setText("Score: " + score);
        } else if (cuvantPropus.equals(cuv1) || cuvantPropus.equals(cuv2) || cuvantPropus.equals(cuv3) || cuvantPropus.equals(cuv4)) {
            lblStatus.setText("Cuvant deja ghicit!");
        } else {
            int score1 = 0;
            ;
            int score2 = 0;
            ;
            int score3 = 0;
            ;
            int score4 = 0;
            ;
            lblStatus.setText("Mai incearca!");
            if (lbCuv1.getText().equals("---")) {
                score1 += compara(cuvantPropus, cuv1);
            }
            if (lbCuv2.getText().equals("---")) {
                score2 += compara(cuvantPropus, cuv2);
            }
            if (lbcuv3.getText().equals("---")) {
                score3 += compara(cuvantPropus, cuv3);
            }
            if (lbcuv4.getText().equals("---")) {
                score4 += compara(cuvantPropus, cuv4);
            }
            score += Math.max(score1, Math.max(score2, Math.max(score3, score4)));

            lbScore.setText("Score: " + score);
        }

        if (attemps == 4) {
            finalizareGame();
        }
    }


    private void finalizareGame() {
        lbCuv1.setText(cuv1);
        lbCuv2.setText(cuv2);
        lbcuv3.setText(cuv3);
        lbcuv4.setText(cuv4);


        Game game = new Game(1, score, formattedDateTime, cuvant, nrLitereGhicite);
        try {
            game = service.saveGame(game);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        JucatoriGames jucatoriGames = new JucatoriGames(1, game, jucator);
        try {
            service.saveJucatorGames(jucatoriGames);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gameFinished(JucatoriGames game) {
        System.out.println("RECEIVED UPDATE");
        System.out.println(game);
        Platform.runLater(this::initModelTabel);
    }
}
