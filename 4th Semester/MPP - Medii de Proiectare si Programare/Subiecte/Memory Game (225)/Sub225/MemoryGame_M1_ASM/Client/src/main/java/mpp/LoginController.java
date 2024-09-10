package mpp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mpp.Exceptions.GenericException;
import javafx.scene.control.TextField;

public class LoginController implements IObserver{
    IServices service;
    Stage stage;

    private Jucator jucatorCurent;
    private GameControllerMemoryGame gameController;
    private FXMLLoader cloader;
    private Parent croot;

    @FXML
    public TextField tbUsername;

    public void setService(IServices service, Stage stage, GameControllerMemoryGame gameController, FXMLLoader cloader, Parent croot) {
        this.service = service;
        this.stage = stage;
        this.gameController = gameController;
        this.cloader = cloader;
        this.croot = croot;
    }


    @FXML
    public void handleLogin() {
        String username = tbUsername.getText();
        Jucator jucator = null;
        try {
            jucator = service.findJucator(username);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        jucatorCurent = jucator;
        try {
            service.login(username, gameController);

            // Use the already loaded FXML and controller
            stage.setScene(new Scene((AnchorPane) croot));

            // No need to get the controller again, use the already set one
            gameController.setService(jucatorCurent, service, stage);
            stage.show();

        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void gameFinished(JucatoriGames game) {
        gameController.gameFinished(game);
    }
}
