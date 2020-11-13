package sudoku.gameLogic;

import sudoku.variables.Coordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sudoku.gameLogic.SudokuGame.GRID;


class GameGenerator {
    public static int[][] getNewGameGrid() {
        return unsolveGame(getSolvedGame());
    }

    /**
     * 1. Wygeneruj nową tablicę dwuwymiarową 9x9
     * 2. Ułóż Sudoku - dla każdej wartości z przedziału 1..9, rozłóż tę wartość po tablicy 9 razy według wytycznych:
     * - z planszy wybierana jest losowa współrzędna; jeśli jest pusta - losowa wartość jest przypisywana
     * - losowa alokacja nie może generować niepoprawności wiersza, kolumny lub kwadratu
     */
    private static int[][] getSolvedGame() {
        // Generator liczb losowych
        Random random = new Random(System.currentTimeMillis());

        // Tworzymy nową planszę bazującą na zmiennej GRID = 9
        int[][] newGrid = new int[GRID][GRID];

        // Value oznacza możliwą wartość (od 1 do 9) do wpisania w danym polu
        // Każda wartość może być ulokowana maksymalnie 9 razy
        for (int value = 1; value <= GRID; value++) {

            // Liczba alokacji, czyli przypisania danej wartości
            int allocations = 0;

            // Jeśli zbyt dużo alokacji kończy się niepoprawnym rozwiązaniej gry
            // ostatnia alokacja ustawiana jest na 0 (pusta)
            int interrupt = 0;

            // śledzenie co zostało zalokowane w danej pętli
            List<Coordinates> allocTracker = new ArrayList<>();

            // Po 500 próbach alokacji plansza jest całkowicie resetowana i algorytm próbuje od nowa
            int attempts = 0;

            // Każda plansza przyjmuje maksymalnie 9 jedynek, 9 dwójek itd. (maksymalnie 9 alokacji)
            while (allocations < GRID) {

                // Liczba prób = 200; po 200 próbach zerujemy liczbę alokacji
                if (interrupt > 200) {
                    // Współrzędne, po każdej alokacji, przypisywane są do allocTrackera
                    allocTracker.forEach(coord -> {
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });

                    interrupt = 0;
                    allocations = 0;
                    allocTracker.clear();
                    attempts++;

                    // Czyszczenie całej gry po 500 nieudanych próbach alokacji
                    if (attempts > 500) {
                        clearArray(newGrid);
                        attempts = 0;
                        value = 1;
                    }
                }

                int xCoordinate = random.nextInt(GRID);
                int yCoordinate = random.nextInt(GRID);

                if (newGrid[xCoordinate][yCoordinate] == 0) {
                    newGrid[xCoordinate][yCoordinate] = value;

                    // Zeruje element i zaczyna od nowa
                    if (GameLogic.sudokuIsInvalid(newGrid)) {
                        newGrid[xCoordinate][yCoordinate] = 0;
                        interrupt++;
                    }
                    // W przeciwnym wypadku dodaje alokację do trackera
                    else {
                        allocTracker.add(new Coordinates(xCoordinate, yCoordinate));
                        allocations++;
                    }
                }
            }
        }
        return newGrid;
    }

    /**
     * 1. Skopiuj wartości z solvedGame do nowej tablicy
     * 2. Usuń 20 losowych wartości z nowej tablicy
     * 3. Przetestuj rozwiązywalność nowej tablicy
     * 4a. Jeśli rozwiązywalna -> zwróc nową tablicę
     * 4b. Jeśli nierozwiązywalna - > wróc do krok 1
     * @param solvedGame
     */
    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;

        int[][] solvableArray = new int[GRID][GRID];

        while (solvable == false){

            // Zresetuj stan
            SudokuTools.copySudokuArrayValues(solvedGame, solvableArray);

            // Usuń 20 elementów z wygenerowanej planszy (to będą pola puste wypełniane przez użytkownika)
            // Można w łatwy sposób zwiększyć poziom trudności gry zwiększając tę liczbę, np. do 60
            int index = 0;
            while (index < 20) {
                int xCoordinate = random.nextInt(GRID);
                int yCoordinate = random.nextInt(GRID);

                if (solvableArray[xCoordinate][yCoordinate] != 0) {
                    solvableArray[xCoordinate][yCoordinate] = 0;
                    index++;
                }
            }

            int[][] toBeSolved = new int[GRID][GRID];
            SudokuTools.copySudokuArrayValues(solvableArray, toBeSolved);
            // Sprawdź czy wynik jest rozwiązywalny
            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
        }
        return solvableArray;
    }

    // Czyszczenie pól na planszy (ustawienie wszystkich współrzędnych na 0)
    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < GRID; xIndex++) {
            for (int yIndex = 0; yIndex < GRID; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }
}