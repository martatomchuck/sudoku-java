package sudoku.variables;

/**
 * Klasa inicjująca treść komunikatów pokazywanych uzytkownikowi w przypadku:
 *
 * wygranej - GAME_COMPLETE
 * przegranej - GAME_INCORRECT
 * błędu aplikacji - ERROR
 */
public class Message {
    public static final String GAME_COMPLETE = "Congratulations, you have won! Do you want to play again?";
    public static final String GAME_INCORRECT = "Sorry, incorrect result. Do you want to play again?";
    public static final String ERROR = "An error has occurred";
}