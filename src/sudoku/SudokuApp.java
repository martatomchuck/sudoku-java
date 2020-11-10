package sudoku;

import sudoku.userInterface.UserInterfaceContract;
import sudoku.userInterface.UserInterface;
import sudoku.gameLogic.BuildLogic;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This class is the Root Container (the thing which attends to all of the primary objects which must communicate when
 * the program is running (a running program is called a "process").
 */
public class SudokuApp extends Application {
    private UserInterfaceContract.View uiImpl;

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Get SudokuGame object for a new game
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