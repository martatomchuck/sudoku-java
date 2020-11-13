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
     * Za każdy razem gdy zmienia się gridState, generowany jest nowy obiekt SudokuGame
     * @param gameState
     * @param gridState
     */
    public SudokuGame(GameState gameState, int[][] gridState) {
        this.gameState = gameState;
        this.gridState = gridState;
    }

    // Zwraca stan gry
    public GameState getGameState() {
        return gameState;
    }

    // Zwraca kopię planszy, żeby móc porównac z nową wersją po zmianie
    public int[][] getCopyOfGridState() {
        return SudokuTools.copyToNewArray(gridState);
    }

}