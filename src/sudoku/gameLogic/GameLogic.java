package sudoku.gameLogic;

import sudoku.variables.GameState;
import sudoku.variables.Rows;

import java.util.*;

import static sudoku.gameLogic.SudokuGame.GRID;

/**
 * Klasa zawierająca statyczne funkcje używane do walidacji gry i zmiany stanu gry (ACTIVE, COMPLETE, INCORRECT)
 */
public class GameLogic {

  public static SudokuGame getNewGame() {
      return new SudokuGame(
              GameState.NEW,
              GameGenerator.getNewGameGrid()
      );
  }

  /**
   * Sprawdzenie czy gra jest ukończona
   * 1. wszystkie pola są wypełnione
   * 2. plansza jest wypełniona poprawnie
   *
   * @param grid
   * @return GameState.Active, GameState.Complete, GameState.Incorrect w zależności od wyniku gry
   * <p>
   * Zasady:
   * - Cyfry w jednym rzędzie nie mogą się powtarzać, e.g.:
   * - [0, 0] == [0-8, 1] not allowed
   * - [0, 0] == [3, 4] allowed
   * - Cyfry w jednej kolumnie nie mogą się powtarzać, e.g.:
   * - [0-8, 1] == [0, 0] not allowed
   * - [0, 0] == [3, 4] allowed
   * - Cyfry w jednym kwadracie nie mogą się powtarzać, e.g.:
   * - [0, 0] == [1, 2] not allowed
   * - [0, 0] == [3, 4] allowed
   */
  public static GameState checkForCompletion(int[][] grid) {
      if (!tilesAreNotFilled(grid) && sudokuIsInvalid(grid)) return GameState.INCORRECT;
      if (sudokuIsInvalid(grid)) return GameState.ACTIVE;
      if (tilesAreNotFilled(grid)) return GameState.ACTIVE;
      return GameState.COMPLETE;
  }

  /**
   * Przechodzi po wszystkich polach i sprawdza czy jest jakieś pole z wartością 0 (puste)
   * GRID = GRID
   * @param grid
   */
  public static boolean tilesAreNotFilled(int[][] grid) {
      for (int xIndex = 0; xIndex < GRID; xIndex++) {
          for (int yIndex = 0; yIndex < GRID; yIndex++) {
              if (grid[xIndex][yIndex] == 0) return true;
          }
      }
      return false;
  }

  /**
   * Sprawdza czy sudoku jest wypełnione poprawnie
   * @param grid
   */
  public static boolean sudokuIsInvalid(int[][] grid) {
      if (rowsAreInvalid(grid)) return true;
      if (columnsAreInvalid(grid)) return true;
      if (squaresAreInvalid(grid)) return true;
      else return false;
  }


  /**
   * "Square", czyli kwadrat, jest wycienkiem planszy złożonym z pól 3x3
   * Na całej planszy jest 9 takich kwadratów
   * <p>
   * Przykładowy kwadrat:
   * [0][0], [1][0], [2][0]
   * [0][1], [1][1], [2][1]
   * [0][2], [1][2], [2][2]
   * <p>
   * Zakresy:
   * [0][0] - [2][2], [3][0] - [5][2], [6][0] - [8][2]
   * <p>
   * [0][3] - [2][2], [3][3] - [5][5], [6][3] - [8][5]
   * <p>
   * [0][6] - [2][2], [3][0] - [5][2], [6][0] - [8][8]
   *
   * @param grid kopia planszy SudokuGame
   */
  public static boolean squaresAreInvalid(int[][] grid) {
      // 3 kwadraty górne
      if (rowOfSquaresIsInvalid(Rows.TOP, grid)) return true;

      // 3 kwadraty środkowe
      if (rowOfSquaresIsInvalid(Rows.MIDDLE, grid)) return true;

      // 3 kwadraty dolne
      if (rowOfSquaresIsInvalid(Rows.BOTTOM, grid)) return true;

      return false;
  }

  private static boolean rowOfSquaresIsInvalid(Rows value, int[][] grid) {
      switch (value) {
          case TOP:
              //x FIRST = 0
              if (squareIsInvalid(0, 0, grid)) return true;
              //x SECOND = 3
              if (squareIsInvalid(0, 3, grid)) return true;
              //x THIRD = 6
              if (squareIsInvalid(0, 6, grid)) return true;

              //W innym wypadku kwadrat (square) jest poprawny
              return false;

          case MIDDLE:
              if (squareIsInvalid(3, 0, grid)) return true;
              if (squareIsInvalid(3, 3, grid)) return true;
              if (squareIsInvalid(3, 6, grid)) return true;
              return false;

          case BOTTOM:
              if (squareIsInvalid(6, 0, grid)) return true;
              if (squareIsInvalid(6, 3, grid)) return true;
              if (squareIsInvalid(6, 6, grid)) return true;
              return false;

          default:
              return false;
      }
  }

  public static boolean squareIsInvalid(int yIndex, int xIndex, int[][] grid) {
      int yIndexEnd = yIndex + 3;
      int xIndexEnd = xIndex + 3;

      List<Integer> square = new ArrayList<>();

      while (yIndex < yIndexEnd) {

          while (xIndex < xIndexEnd) {
              square.add(
                      grid[xIndex][yIndex]
              );
              xIndex++;
          }

          // Zresetuj x do pierwotnej wartości
          xIndex -= 3;

          yIndex++;
      }

      // Jeśli kwadraty się powtarzają, zwróć true
      if (collectionHasRepeats(square)) return true;
      return false;
  }

  public static boolean columnsAreInvalid(int[][] grid) {
      for (int xIndex = 0; xIndex < GRID; xIndex++) {
          List<Integer> row = new ArrayList<>();
          for (int yIndex = 0; yIndex < GRID; yIndex++) {
              row.add(grid[xIndex][yIndex]);
          }

          if (collectionHasRepeats(row)) return true;
      }

      return false;
  }

  public static boolean rowsAreInvalid(int[][] grid) {
      for (int yIndex = 0; yIndex < GRID; yIndex++) {
          List<Integer> row = new ArrayList<>();
          for (int xIndex = 0; xIndex < GRID; xIndex++) {
              row.add(grid[xIndex][yIndex]);
          }

          if (collectionHasRepeats(row)) return true;
      }

      return false;
  }

  public static boolean collectionHasRepeats(List<Integer> collection) {
      // Jeśli Collections.frequency zwraca wartość większość niż 1, kwadrat jest wypełniony niepoprawnie
      // (i.e. a wartość niezerowa jest powtórzona w kwadracie)
      for (int index = 1; index <= GRID; index++) {
          if (Collections.frequency(collection, index) > 1) return true;
      }

      return false;
  }
}
