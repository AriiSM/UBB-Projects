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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainController implements IObserver {

    private IServices service;
    private Persoana jucator;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label lblStatus;

    private final Button[][] buttons = new Button[3][3];
    private String[][] values = new String[3][3];

    private final boolean[][] revealed = new boolean[3][3];
    private int score = 0;
    private long durataStart = 0;
    private long durataEnd = 0;
    int occupied = 0;


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
        values = new String[3][3];
        durataStart = Instant.now().getEpochSecond();
        initializeGrid();
    }


    private void initializeGrid() {
        gridPane.getChildren().clear();

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                values[i][j] = "";
                revealed[i][j] = false;
            }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
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

        values[row][col] = "X";
        buttons[row][col].setText("X");
        occupied++;
        revealed[row][col] = true;

        boolean matchFound = checkMatch();

        if (!matchFound && occupied < 9) {
            Random random = new Random();
            int i = random.nextInt(3);
            int j = random.nextInt(3);

            String box = values[i][j];

            while (box.equals("X") || box.equals("0")) {
                i = random.nextInt(3);
                j = random.nextInt(3);
                box = values[i][j];
            }
            values[i][j] = "0";
            buttons[i][j].setText("0");
            revealed[i][j] = true;
            occupied++;

            checkMatch();
        }
    }

    private boolean checkMatch() {
        //COLOANE
        for (int i = 0; i < 3; i++) {
            if (values[i][0].equals(values[i][1]) && values[i][1].equals(values[i][2]) && values[i][0].equals("X")) {
                score += 10;
                durataEnd = Instant.now().getEpochSecond();
                lblStatus.setText("You won!");
                showCompletionMessage();
                return true;
            } else if (values[i][0].equals(values[i][1]) && values[i][1].equals(values[i][2]) && values[i][0].equals("0")) {
                score -= 10;
                durataEnd = Instant.now().getEpochSecond();
                lblStatus.setText("Computer won!");
                showCompletionMessage();
                return true;
            }
        }

        //LINII
        for (int i = 0; i < 3; i++) {
            if (values[0][i].equals(values[1][i]) && values[1][i].equals(values[2][i]) && values[0][i].equals("X")) {
                score += 10;
                durataEnd = Instant.now().getEpochSecond();
                lblStatus.setText("You won!");
                showCompletionMessage();
                return true;
            } else if (values[0][i].equals(values[1][i]) && values[1][i].equals(values[2][i]) && values[0][i].equals("0")) {
                score -= 10;
                durataEnd = Instant.now().getEpochSecond();
                lblStatus.setText("Computer won!");
                showCompletionMessage();
                return true;
            }
        }

        //DIAGONALE
        if (values[0][0].equals(values[1][1]) && values[1][1].equals(values[2][2]) && values[0][0].equals("X")) {
            score += 10;
            durataEnd = Instant.now().getEpochSecond();
            lblStatus.setText("You won!");
            showCompletionMessage();
            return true;
        } else if (values[0][0].equals(values[1][1]) && values[1][1].equals(values[2][2]) && values[0][0].equals("0")) {
            score -= 10;
            durataEnd = Instant.now().getEpochSecond();
            lblStatus.setText("Computer won!");
            showCompletionMessage();
            return true;
        } else if (values[0][2].equals(values[1][1]) && values[1][1].equals(values[2][0]) && values[0][2].equals("X")) {
            score += 10;
            durataEnd = Instant.now().getEpochSecond();
            lblStatus.setText("You won!");
            showCompletionMessage();
            return true;
        } else if (values[0][2].equals(values[1][1]) && values[1][1].equals(values[2][0]) && values[0][2].equals("0")) {
            score -= 10;
            durataEnd = Instant.now().getEpochSecond();
            lblStatus.setText("Computer won!");
            showCompletionMessage();
            return true;

        } else if (occupied == 9) {
            durataEnd = Instant.now().getEpochSecond();
            score += 5;
            lblStatus.setText("Draw!");
            showCompletionMessage();
        }
        return false;
    }


    private void showCompletionMessage() {
        int durata = Math.toIntExact(durataEnd - durataStart);
        String coef00 = values[0][0];
        String coef01 = values[0][1];
        String coef02 = values[0][2];
        String coef10 = values[1][0];
        String coef11 = values[1][1];
        String coef12 = values[1][2];
        String coef20 = values[2][0];
        String coef21 = values[2][1];
        String coef22 = values[2][2];

        Configurare config = new Configurare(-1, coef00, coef01, coef02, coef10, coef11, coef12, coef20, coef21, coef22);
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
