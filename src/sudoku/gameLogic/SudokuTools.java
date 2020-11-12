package sudoku.gameLogic;

public class SudokuTools {

    /**
     * Kopiowanie wartości z planszy
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
     * Tworzenie nowej planszy ze skopiowanych wartości
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