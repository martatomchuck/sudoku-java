package sudoku.gameLogic;

import java.io.*;

/**
 * Pobieranie stanu gry z pliku zapisanego na dysku
 */
public class LocalStorage implements Storage {

    // Zapisanie danych gry do pliku skąd system wyciągnie dane przy aktualizowaniu gry
    private static File GAME_DATA = new File(
              System.getProperty("user.home"),
            "gamedata.txt"
    );

    @Override
    public void updateGameData(SudokuGame game) throws IOException {
        try {

            // Sciąga dane z pliku (streaming data)
            FileOutputStream fileOutputStream = new FileOutputStream(GAME_DATA);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch (IOException e) {
            // Jeśli nie ma danych w storage to zwraca wyjątek
            throw new IOException("Unable to access Game Data");
        }
    }

    @Override
    public SudokuGame getGameData() throws IOException {

        FileInputStream fileInputStream =
                new FileInputStream(GAME_DATA);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        try {
            SudokuGame gameState = (SudokuGame) objectInputStream.readObject();
            objectInputStream.close();
            return gameState;
        } catch (ClassNotFoundException e) {
            throw new IOException("File Not Found");
        }
    }
}