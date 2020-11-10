package sudoku.gameLogic;

public class SudokuTools {

    /**
     * Copy the values from one sudoku grid into another
     *
     * Note: O(n^2) Runtime Complexity
     * @param oldArray
     * @param newArray
     */
    public static void copySudokuArrayValues(int[][] oldArray, int[][] newArray) {
        for (int xIndex = 0; xIndex < SudokuGame.GRID; xIndex++){
            for (int yIndex = 0; yIndex < SudokuGame.GRID; yIndex++ ){
                newArray[xIndex][yIndex] = oldArray[xIndex][yIndex];
            }
        }
    }

    /**
     * Creates and returns a new Array with the same values as the inputted Array.
     *
     * @param oldArray
     */
    public static int[][] copyToNewArray(int[][] oldArray) {
        int[][] newArray = new int[SudokuGame.GRID][SudokuGame.GRID];
        for (int xIndex = 0; xIndex < SudokuGame.GRID; xIndex++){
            for (int yIndex = 0; yIndex < SudokuGame.GRID; yIndex++ ){
                newArray[xIndex][yIndex] = oldArray[xIndex][yIndex];
            }
        }

        return newArray;
    }
}