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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController implements IObserver {

    private IServices service;
    private Persoana jucator;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label scoreLabel;

    private Button[][] buttons = new Button[4][4];
    private boolean[][] revealed = new boolean[4][4];
    private int score = 0;
    private String formattedDateTime = null;
    private LocalDateTime startTime;
    private String clueWord = "Ariana";
    private int clueRow;
    private int clueCol;
    private int attempts = 0;
    private List<String> gameConfiguration = new ArrayList<>(16);

    @FXML
    private TableView<JucatorGame> tabelClasament;
    @FXML
    private TableColumn<JucatorGame, String> colUsername;
    @FXML
    private TableColumn<JucatorGame, String> colStartTime;
    @FXML
    private TableColumn<JucatorGame, String> colIncercari;
    @FXML
    private TableColumn<JucatorGame, String> colCuvant;


    ObservableList<JucatorGame> jucatoriGamesObservableList = FXCollections.observableArrayList();
    ;

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
        colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPersoana().getUsername()));
        colStartTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getOraStart()));
        colIncercari.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getGame().getNrIncercari())));
        colCuvant.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getCuvant()));
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        startTime = LocalDateTime.now();
        formattedDateTime = startTime.format(formatter);

        Random random = new Random();
        clueRow = random.nextInt(4);
        clueCol = random.nextInt(4);
        System.out.println("Pozitia indicelui: (" + clueRow + ", " + clueCol + ")");
        attempts = 0;
        initializeGrid();
    }

    private void initializeGrid() {
        gridPane.getChildren().clear();
        gameConfiguration.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(100, 100);
                final int row = i;
                final int col = j;
                gridPane.add(buttons[i][j], j, i);
                buttons[i][j].setOnAction(e -> handleButtonClick(row, col));

                if (row == clueRow && col == clueCol) {
                    int s = i * 4 + j;
                    gameConfiguration.add(clueWord + " " + s);
                }
            }
        }
        scoreLabel.setText("Scor: " + score);
    }

    private void handleButtonClick(int row, int col) {
        if (revealed[row][col]) {
            return;
        }

        attempts++;
        if (row == clueRow && col == clueCol) {
            buttons[row][col].setText(clueWord);
            revealed[row][col] = true;
            score += 10; // Add points for correct guess
            scoreLabel.setText("Scor: " + score);
            showCompletionMessage();
        } else {
            revealed[row][col] = true;
            score -= 1;
            double distance = calculateEuclideanDistance(row, col, clueRow, clueCol);
            scoreLabel.setText("Scor: " + score + " - Distanta: " + distance);

            if (attempts >= 4) {
                scoreLabel.setText("Scor: " + score + " - Ai pierdut! Numarul maxim de incercari a fost atins.(4)");
                failedGame();
            }
        }
    }

    private double calculateEuclideanDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private void failedGame() {
        Configuratie configuratie = new Configuratie(-1, gameConfiguration.toString());
        try {
            configuratie = service.saveConfiguratie(configuratie);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        Game game = new Game(-1, configuratie, formattedDateTime, 10, "0", score);
        try {
            game = service.saveGame(game);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        JucatorGame jucatoriGames = new JucatorGame(-1, jucator, game);
        try {
            jucatoriGames = service.saveJucatoriGames(jucatoriGames);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }

    private void showCompletionMessage() {
        scoreLabel.setText("Scor: " + score + "  Ai castigat!");
        Configuratie configuratie = new Configuratie(-1, gameConfiguration.toString());
        try {
            configuratie = service.saveConfiguratie(configuratie);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        Game game = new Game(-1, configuratie, formattedDateTime, attempts, clueWord, score);
        try {
            game = service.saveGame(game);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        JucatorGame jucatoriGames = new JucatorGame(-1, jucator, game);
        try {
            jucatoriGames = service.saveJucatoriGames(jucatoriGames);
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
