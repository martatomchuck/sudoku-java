package sudoku.variables;

/**
 * Enum zawierający możliwe statusy gry:
 *
 * COMPLETE = gra zakończona powodzeniem (wygrana)
 * ACTIVE = gra aktywna (w toku, brak wyniku)
 * NEW = nowa gra
 * INCORRECT = gra zakończona niepowodzeniem (przegrana)
 */
public enum GameState {
    COMPLETE,
    ACTIVE,
    NEW,
    INCORRECT
}