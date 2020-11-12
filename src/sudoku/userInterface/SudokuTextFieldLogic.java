package sudoku.userInterface;

import sudoku.variables.GameState;
import sudoku.variables.Message;
import sudoku.gameLogic.GameLogic;
import sudoku.gameLogic.Storage;
import sudoku.gameLogic.SudokuGame;

import java.io.IOException;

/**
 * Logika aktywności użytkownika (aktualizacja widoku po wypełnieniu pola tekstowego)
 */
public class SudokuTextFieldLogic implements UserInterfaceContract.EventListener {

    private Storage storage;
    private UserInterfaceContract.View view;

    public SudokuTextFieldLogic(Storage storage, UserInterfaceContract.View view) {
        this.storage = storage;
        this.view = view;
    }

    /**
     * KROK 1. Wyciągnij aktualny stan danych ze Storage
     * KROK 2. Zaktualizuj odpowiednio do wartości inputa
     * KROK 3. Zapisz rezultat w Storage
     * @param x współrzędna X wybranego inputa
     * @param y współrzędna Y wybranego inputa
     * @param input wprowadzona wartość (cyfra z zakresu 0-9)
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

            // Zaktualizuj widok
            view.updateSquare(x, y, input);

            // Jeśli gra jest zakończona, pokaż użytkownikowi okno dialogowe
            // z odpowiednim komunikatem w zależności od wyniku gry
            if (gameData.getGameState() == GameState.COMPLETE) {
                view.showDialog(Message.GAME_COMPLETE);
            } else if (gameData.getGameState() == GameState.INCORRECT) {
                view.showDialog(Message.GAME_INCORRECT);
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