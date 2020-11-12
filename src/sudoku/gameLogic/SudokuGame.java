package sudoku.gameLogic;

import sudoku.variables.GameState;

import java.io.Serializable;

/**
 * Gra Sudoku zbudowana jest w oparciu o tablicę dwuwymiarową, złożoną z 9 wierszy i 9 kolumn (zawiera 9x9 = 81 pól)
 */

// Klasa implementuje Serializable - do zapisywania na dysku współrzędnych planszy
public class SudokuGame implements Serializable {
    // Enum ze stanem gry
    private final GameState gameState;
    // Aktualna plansza e formie dwuwymiarowej tablicy
    private final int[][] gridState;


    // Plansza składa się z 9 kwadratów; współrzędne siatki, na której zbudowana jest plansza
    // oznaczone są literami x i y (oznaczonymi od 0 do 8)
    public static final int GRID = 9;

    /**
     * I suppose that the most fundamental states required to represent a sudoku game, are an active state and a
     * complete state. The game will start in Active state, and when a Complete state is achieved (based on GameLogic),
     * then a special UI screen will be displayed by the user interface.
     *
     * To avoid Shared Mutable State (Shared change-able data), which causes many problems, I have decided to make this
     * class Immutable (meaning that once I created an instance of it, the values may only be read via getGameState()
     * and getGridState() functions, a.k.a. methods. Each time the gridState changes, a new SudokuGame object is created
     * by taking the old one, applying some functions to each, and generated a new one.
     *
     * @param gameState I have decided to make the initial potential states of the game to be an ENUM (a set of custom
     *                  constant values which I give legible names to), one of:
     *                  - GameState.Complete
     *                  - GameState.Active
     *
     * @param gridState The state of the sudoku game. If certain conditions are met (all locations in the gridstate
     *                  are filled in with the proper value), GameLogic must change gameState.
     *                  Examples:
     *                  - gridState[1,1] Top left square
     *                  - gridState[3,9] 3rd from the left, bottom row
     *                  - gridState[9,9] Bottom right square
     */
    public SudokuGame(GameState gameState, int[][] gridState) {
        this.gameState = gameState;
        this.gridState = gridState;
    }

//    Zwraca stan gry
    public GameState getGameState() {
        return gameState;
    }

//    Zwraca kopię planszy, żeby móc porównac z nową wersją po zmianie
    public int[][] getCopyOfGridState() {
        return SudokuTools.copyToNewArray(gridState);
    }

}