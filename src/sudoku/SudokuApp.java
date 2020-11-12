package sudoku;

import sudoku.userInterface.UserInterfaceContract;
import sudoku.userInterface.UserInterface;
import sudoku.gameLogic.BuildLogic;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Klasa będą kontenerem dla całej aplikacji
 */
public class SudokuApp extends Application {
    private UserInterfaceContract.View uiImpl;

    @Override
    public void start(Stage primaryStage) throws IOException {
        uiImpl = new UserInterface(primaryStage);

        try {
            BuildLogic.build(uiImpl);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}