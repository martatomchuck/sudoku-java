package sudoku.gameLogic;

import sudoku.variables.Coordinates;

import static sudoku.gameLogic.SudokuGame.GRID;

/**
 * Implementacja algorytmu "Simple Solving Algorithm" rozwi¹zuj¹cego Sudoku ze strony internetowej Cornell:
 * http://pi.math.cornell.edu/~mec/Summer2009/meerkamp/Site/Solving_any_Sudoku_I.html
 * 1.Enumerate all empty cells in typewriter order (left to right, top to bottom)
 * <p>
 * 2.Our “current cell” is the first cell in the enumeration.
 * <p>
 * 3.Enter a 1 into the current cell. If this violates the Sudoku condition, try entering a 2, then a 3, and so forth, until
 * a. the entry does not violate the Sudoku condition, or until
 * b. you have reached 9 and still violate the Sudoku condition.
 * <p>
 * 4.In case a: if the current cell was the last enumerated one, then the puzzle is solved.
 * If not, then go back to step 2 with the “current cell” being the next cell.
 * In case b: if the current cell is the first cell in the enumeration, then the Sudoku puzzle does not have a solution.
 * If not, then erase the 9 from the current cell, call the previous cell in the enumeration the new “current cell”, and
 * continue with step 3.
 */
public class SudokuSolver {

    public static boolean puzzleIsSolvable(int[][] puzzle) {

        Coordinates[] emptyCells = typeWriterEnumerate(puzzle);

        int index = 0;
        int input = 1;
        while (index < 10) {
            Coordinates current = emptyCells[index];
            input = 1;
            while (input < 20) {
                puzzle[current.getX()][current.getY()] = input;
                // jeœli sudoku jest nierozwi¹zane...
                if (GameLogic.sudokuIsInvalid(puzzle)) {
                    // jeœli wci¹¿ nierozwi¹zane, przejdŸ do kroku 4b
                    if (index == 0 && input == GRID) {
                        // pierwsze pole nie mo¿e zostaæ rozwi¹zane
                        return false;
                    } else if (input == GRID) {

                        index--;
                    }

                    input++;
                } else {
                    index++;

                    if (index == 19) {
                        // ostatnie pole, sudoku rozwi¹zane
                        return true;
                    }

                    // input = 10 przerywa pêtlê
                    input = 10;
                }
            }
        }

        return false;
    }

    /**
     * Wypisz wszystkie puste pola (od lewej do prawej, z góry na dó³)
     * @param puzzle
     */
    private static Coordinates[] typeWriterEnumerate(int[][] puzzle) {
        Coordinates[] emptyCells = new Coordinates[20];
        int iterator = 0;
        for (int y = 0; y < GRID; y++) {
            for (int x = 0; x < GRID; x++) {
                if (puzzle[x][y] == 0) {
                    emptyCells[iterator] = new Coordinates(x, y);
                    if (iterator == 19) return emptyCells;
                    iterator++;
                }
            }
        }
        return emptyCells;
    }


}