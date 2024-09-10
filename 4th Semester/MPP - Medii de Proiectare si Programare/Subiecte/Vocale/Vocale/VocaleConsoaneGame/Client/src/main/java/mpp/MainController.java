package mpp;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mpp.Exceptions.GenericException;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainController implements IObserver {

    private IServices service;
    private Persoana jucator;


    @FXML
    public TableView<JucatorGame> tabelClasament;
    @FXML
    public TableColumn<JucatorGame, String> colUsername;
    @FXML
    public TableColumn<JucatorGame, String> colTime;
    @FXML
    public TableColumn<JucatorGame, String> colGhicite;
    @FXML
    public TableColumn<JucatorGame, String> colPuncte;

    ObservableList<JucatorGame> jucatoriGamesObservableList = FXCollections.observableArrayList();


    @FXML
    public Label lblCuvant;

    String cuvant = null;
    char[] mascaChange = null;
    ArrayList<Character> litereGhicite = new ArrayList<>();
    String masca = null;

    @FXML
    public TextField txtLitera;

    Integer attemps;
    Integer score;
    String oraStart;


    public void setService(Persoana jucator, IServices service, Stage stage) {
        this.jucator = jucator;
        this.service = service;
        initModelTabel();
    }

    private void initModelTabel() {
        jucatoriGamesObservableList.clear();
        populareTabel();
        initialize();
        initializareCuvant();
    }

    private void populareTabel() {
        Iterable<JucatorGame> jucatoriGamesIterable = null;
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
        colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPersoana().getUsername()));
        colPuncte.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getScore().toString()));
        colGhicite.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getGame().getNoIncercari())));
        colTime.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getGame().getOraStart())));
    }

    private void initializareCuvant() {
        attemps = 0;
        score = 0;
        LocalDateTime ora = LocalDateTime.now();
        oraStart = String.valueOf(ora.getHour());
        try {
            cuvant = service.findOneCuvant();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        masca = "";
        for (int i = 0; i < cuvant.length(); i++) {
            char litera = cuvant.charAt(i);
            if (litera == 'a' || litera == 'e' || litera == 'i' || litera == 'o' || litera == 'u') {
                masca += 'V';
            } else if (Character.isLetter(litera)) {
                masca += 'C';
            }
        }
        mascaChange = masca.toCharArray();
        lblCuvant.setText(masca);
    }

    @FXML
    public void handleIncearca() {
        attemps++;
        boolean g = false;
        char litera = txtLitera.getText().charAt(0);
        for (int i = 0; i < cuvant.length(); i++) {
            char litera1 = cuvant.charAt(i);
            if (litera1 == litera) {
                score++;
                g = true;
                mascaChange[i] = litera;
            }
        }
        if (g) {
            litereGhicite.add(litera);
        }

        lblCuvant.setText(new String(mascaChange));


        if ((attemps == 4) || (new String(mascaChange).equals(cuvant))) {
            finalizareJoc();
        }
    }

    private void finalizareJoc() {

        Configurare configurare = new Configurare(-1, new String(masca), cuvant);
        try {
            configurare = service.saveConfigurare(configurare);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        Game game = new Game(-1, configurare, score, oraStart, attemps, litereGhicite.toString());
        try {
            game = service.saveGame(game);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        JucatorGame jucatorGame = new JucatorGame(-1, jucator, game);
        try {
            service.saveJucatorGame(jucatorGame);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gameFinished(JucatorGame game) {
        System.out.println("RECEIVED UPDATE");
        System.out.println(game);
        Platform.runLater(this::initModelTabel);
    }
}
