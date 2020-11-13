package sudoku.userInterface;

import sudoku.gameLogic.SudokuGame;

/**
 * Interfejs dla widoku aplikacji i nasłuchiwania zdarzeń
 */
public interface UserInterfaceContract {

    interface EventListener {
        void onSudokuInput(int x, int y, int input);
        void onDialogClick();
    }

    // Widok aplikacji
    interface View {
        void setListener(UserInterfaceContract.EventListener listener);
        // Zaktualizuj każdy kwadrat po wpisaniu wartości przez użytkownika
        void updateSquare(int x, int y, int input);

        // Zaktualizuj całą planszę (pierwsze uruchomienie programu / po zakończeniu gry)
        void updateBoard(SudokuGame game);
        void showDialog(String message);
        void showError(String message);
    }
}
