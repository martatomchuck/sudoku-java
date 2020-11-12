package sudoku.gameLogic;

import sudoku.userInterface.UserInterfaceContract;
import sudoku.userInterface.SudokuTextFieldLogic;

import java.io.IOException;

public class BuildLogic {

    /**
     * Klasa budująca obiekty bazujące na JavaFX, obsługa wyjątków
     */
    public static void build(UserInterfaceContract.View userInterface) throws IOException {
        SudokuGame initialState;
        Storage storage = new LocalStorage();

        try {
            // wyjątek zostanie rzucony jeśli w local storage nie będzie żadnych danych
            initialState = storage.getGameData();
        } catch (IOException e) {

            initialState = GameLogic.getNewGame();
            // wyjątek zostanie rzucony jeśli dane gry nie będą mogły zostać zaktualizowane
            storage.updateGameData(initialState);
        }

        UserInterfaceContract.EventListener uiLogic = new SudokuTextFieldLogic(storage, userInterface);
        userInterface.setListener(uiLogic);
        userInterface.updateBoard(initialState);
    }
}