package mpp;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import mpp.Exceptions.GenericException;

import java.time.Instant;
import java.util.Random;

public class MainController implements IObserver {

    private IServices service;
    private Persoana jucator;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label lblStatus;

    private final Button[][] buttons = new Button[5][5];
    private String[][] values = new String[5][5];

    private final boolean[][] revealed = new boolean[5][5];
    private int score = 0;
    private long durataStart = 0;
    private long durataEnd = 0;
    int occupied = 0;
    private int pozitie = 0;


    @FXML
    private TableView<JucatorGame> tabelClasament;
    @FXML
    private TableColumn<JucatorGame, String> colUsername;
    @FXML
    private TableColumn<JucatorGame, String> colPuncte;
    @FXML
    private TableColumn<JucatorGame, String> colDurata;

    ObservableList<JucatorGame> jucatoriGamesObservableList = FXCollections.observableArrayList();

    public void setService(Persoana jucator, IServices service, Stage stage) {
        this.jucator = jucator;
        this.service = service;
        initModelTabel();
    }

    private void initModelTabel() {
        jucatoriGamesObservableList.clear();
        populareTabel();
        initialize();
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
        colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJucator().getUsername()));
        colPuncte.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getScor().toString()));
        colDurata.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getGame().getDurata())));
    }


    @FXML
    public void handlePlay() {
        try {
            initModelJoc();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }

    private void initModelJoc() throws GenericException {
        lblStatus.setText("Status");
        occupied = 0;
        score = 0;
        pozitie = 0;
        values = new String[5][5];
        durataStart = Instant.now().getEpochSecond();
        initializeGrid();
        initializeGropi();
    }


    private void initializeGrid() {
        gridPane.getChildren().clear();

        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++) {
                values[i][j] = "O";
                revealed[i][j] = false;
            }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(100, 100);
                final int row = i;
                final int col = j;
                gridPane.add(buttons[i][j], j, i);
                buttons[i][j].setOnAction(e -> handleButtonClick(row, col));

            }
        }
    }

    private void initializeGropi() {
        Random random = new Random();
        for (int col = 0; col < 5; col++) {
            int row = random.nextInt(5);
            while (!values[row][col].equals("O")) {
                row = random.nextInt(5);
            }
            buttons[row][col].setText("GROAPA");
            values[row][col] = "GROAPA";
        }

        int i = random.nextInt(5);
        int j = random.nextInt(5);

        int nr = 0;

        while (!values[i][j].equals("GROAPA") && nr == 0) {
            values[i][j] = "GROAPA";
            buttons[i][j].setText("GROAPA");
            i = random.nextInt(5);
            j = random.nextInt(5);
            nr++;
        }

    }


    private void handleButtonClick(int row, int col) {
        if (row == pozitie && !values[row][col].equals("GROAPA")) {
            lblStatus.setText("Avanseaza");
            pozitie++;
            score += 10;
            if (pozitie == 5) {

                finalGame();
                lblStatus.setText("Felicitari! Ai castigat!");
            }
        } else if (row < pozitie) {
            lblStatus.setText("Nu poti avansa decat o linie pe rand");

        } else if (values[row][col].equals("GROAPA")) {
            lblStatus.setText("GROAPA");
            score -= 40;
            finalGame();
        }
    }


    private void finalGame() {
        durataEnd = Instant.now().getEpochSecond();
        int durata = Math.toIntExact(durataEnd - durataStart);

        String configurare = "";
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                configurare += values[i][j] + " ";
            }
        }

        Configurare config = new Configurare(-1, configurare);
        try {
            config = service.saveConfiguratie(config).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        Game game = new Game(-1, config, score, durata);
        try {
            game = service.saveGame(game).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        JucatorGame jucatorGame = new JucatorGame(-1, jucator, game);
        try {
            jucatorGame = service.saveJucatorGame(jucatorGame).get();
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
