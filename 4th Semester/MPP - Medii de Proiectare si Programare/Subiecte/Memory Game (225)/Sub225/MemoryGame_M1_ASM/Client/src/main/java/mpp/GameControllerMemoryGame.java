package mpp;

import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import mpp.Exceptions.GenericException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class GameControllerMemoryGame implements IObserver {
    private IServices service;
    private Jucator jucator;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label scoreLabel;

    private Button[][] buttons = new Button[2][5];
    private String[][] values = new String[2][5];
    private boolean[][] revealed = new boolean[2][5];
    private Button firstButton = null;
    private Button secondButton = null;
    private int firstRow = -1;
    private int firstCol = -1;
    private int secondRow = -1;
    private int secondCol = -1;
    private int score = 0;
    private long durataStart = 0;
    private long durataEnd = 0;

    private String[] words = null;
    private ArrayList<String> valueList = new ArrayList<>();
    private ArrayList<String> configuratie = new ArrayList<>();


    @FXML
    private TableView<JucatoriGames> tabelClasament;
    @FXML
    private TableColumn<JucatoriGames, String> colJucatori;
    @FXML
    private TableColumn<JucatoriGames, String> colScor;
    @FXML
    private TableColumn<JucatoriGames, String> colDurata;

    ObservableList<JucatoriGames> jucatoriGamesObservableList = FXCollections.observableArrayList();

    public void setService(Jucator jucator, IServices service, Stage stage) {
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
        Iterable<JucatoriGames> jucatoriGamesIterable = null;
        try {
            jucatoriGamesIterable = service.findAllJucatoriGames();

        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        jucatoriGamesIterable.forEach(jucatoriGamesObservableList::add);
    }

    @FXML
    public void initialize() {
        tabelClasament.setItems(jucatoriGamesObservableList);
        colJucatori.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJucator().getUsername()));
        colScor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getPuncte().toString()));
        colDurata.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGame().getDurata().toString()));
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
        durataStart = Instant.now().getEpochSecond();
        words = service.find5Pairs();
        System.out.println("CUVINTELE " + Arrays.toString(words));
        initialize1();
    }

    public void initialize1() {
        for (String word : words) {
            valueList.add(word);
            valueList.add(word);
        }


        Collections.shuffle(valueList);
        configuratie = new ArrayList<>(valueList);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                values[i][j] = valueList.remove(0);
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(100, 100);
                final int row = i;
                final int col = j;
                gridPane.add(buttons[i][j], j, i);
                buttons[i][j].setOnAction(e -> handleButtonClick(row, col));
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        if (revealed[row][col]) {
            return;
        }

        if (firstButton == null) {
            firstButton = buttons[row][col];
            firstRow = row;
            firstCol = col;
            firstButton.setText(values[row][col]);
            revealed[row][col] = true;
        } else if (secondButton == null && (firstRow != row || firstCol != col)) {
            secondButton = buttons[row][col];
            secondRow = row;
            secondCol = col;
            secondButton.setText(values[row][col]);
            revealed[row][col] = true;

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> checkMatch());
            pause.play();
        }
    }

    private void checkMatch() {
        if (values[firstRow][firstCol].equals(values[secondRow][secondCol])) {
            score += 3;
        } else {
            score -= 2;
            firstButton.setText("");
            secondButton.setText("");
            revealed[firstRow][firstCol] = false;
            revealed[secondRow][secondCol] = false;
        }

        scoreLabel.setText("Score: " + score);
        firstButton = null;
        secondButton = null;
        firstRow = -1;
        firstCol = -1;
        secondRow = -1;
        secondCol = -1;

        checkGameCompletion();
    }

    private void checkGameCompletion() {
        boolean allRevealed = true;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                if (!revealed[i][j]) {
                    allRevealed = false;
                    break;
                }
            }
        }
        if (allRevealed) {
            durataEnd = Instant.now().getEpochSecond();
            showCompletionMessage();
        }
    }

    private void showCompletionMessage() {


        System.out.println("CONFIGURAREA" + configuratie.toString());
        Configurare configurare = new Configurare(-1, configuratie.toString());
        try {
            configurare = service.saveConfigurare(configurare).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        Integer durata = Math.toIntExact(durataEnd - durataStart);
        Game game = new Game(-1, score, durata, configurare);
        try {
            game = service.saveGame(game).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        JucatoriGames jucatoriGames = new JucatoriGames(-1, game, jucator);
        try {
            jucatoriGames = service.saveJucatorGame(jucatoriGames).get();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void handleResetGame() {

        resetGame();
        try {
            initModelJoc();
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetGame() {
        scoreLabel.setText("Congratulations! You've completed the game!");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                buttons[i][j].setText("");
                revealed[i][j] = false;
            }
        }
        score = 0;
        scoreLabel.setText("Score: " + score);

        ArrayList<String> valueList = new ArrayList<>();
        for (String word : words) {
            valueList.add(word);
            valueList.add(word);
        }
        Collections.shuffle(valueList);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                values[i][j] = valueList.remove(0);
                buttons[i][j].setText("");
            }
        }
    }


    @Override
    public void gameFinished(JucatoriGames game) {
        System.out.println("RECEIVED UPDATE");
        System.out.println(game);
        Platform.runLater(this::initModelTabel);
        //initModelTabel();
    }
}
