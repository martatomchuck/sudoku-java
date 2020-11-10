package sudoku.userInterface;

import sudoku.variables.GameState;
import sudoku.variables.Message;
import sudoku.gameLogic.GameLogic;
import sudoku.gameLogic.Storage;
import sudoku.gameLogic.SudokuGame;

import java.io.IOException;

/**
 * Since this is a single screen application, just one container (class) for the logic of the user interface is
 * necessary. Break these things up when building applications with more screens/features. Don't build God Classes!
 */

public class ControlLogic implements UserInterfaceContract.EventListener {

    private Storage storage;
    //Remember, this could be the real UserInterface, or it could be a test class
    //which implements the same interface!
    private UserInterfaceContract.View view;

    public ControlLogic(Storage storage, UserInterfaceContract.View view) {
        this.storage = storage;
        this.view = view;
    }

    /**
     * Use Case:
     * 1. Retrieve current "state" of the data from Storage
     * 2. Update it according to the input
     * 3. Write the result to Storage
     * @param x X coordinate of the selected input
     * @param y Y ...
     * @param input Which key was entered, One of:
     *  - Numbers 0-9
     *
     */
    @Override
    public void onSudokuInput(int x, int y, int input) {
        try {
            SudokuGame gameData = storage.getGameData();
            int[][] newGridState = gameData.getCopyOfGridState();
            newGridState[x][y] = input;

            gameData = new SudokuGame(
                    GameLogic.checkForCompletion(newGridState),
                    newGridState
            );

            storage.updateGameData(gameData);

            //either way, update the view
            view.updateSquare(x, y, input);

            // jeśli gra jest zakończona, pokaż użytkownikowi okno modalne w zależności od wyniku gry
            if (gameData.getGameState() == GameState.COMPLETE) {
                view.showDialog(Message.GAME_COMPLETE);
            } else if (gameData.getGameState() == GameState.WRONG) {
                view.showDialog(Message.GAME_WRONG);
            }
        } catch (IOException e) {
            e.printStackTrace();
            view.showError(Message.ERROR);
        }
    }

    @Override
    public void onDialogClick() {
        try {
            storage.updateGameData(
                    GameLogic.getNewGame()
            );

            view.updateBoard(storage.getGameData());
        } catch (IOException e) {
            view.showError(Message.ERROR);
        }
    }
}