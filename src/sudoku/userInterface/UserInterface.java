package sudoku.userInterface;

import sudoku.variables.GameState;
import sudoku.variables.Coordinates;
import sudoku.gameLogic.SudokuGame;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.HashMap;

/**
 * Obsługa okienka z planszą do gry
 *
 * Manages the window, and displays a pop up notification when the user completes the puzzle.
 */
public class UserInterface implements UserInterfaceContract.View, EventHandler<KeyEvent> {

//    okno w tle, tło dla aplikacji (z JavaFX)
    private final Stage stage;

//    div, container (z aplikacji JavaFX)
    private final Group root;

    // ZŁA PRAKTYKA: tworzenie zmiennej dla każdego pojedynczego pola na planszy (81 zmiennych)
    // DOBRA PRAKTYKA: HashMap i współrzędne x i y (przy pomocy funkcji HashMap wyciagamy i przechowujemy informację o współrzędnych z i y)
    // This HashMap stores the Hash Values (a unique identifier which is automatically generated;
    // see java.lang.object in the documentation) of each TextField by their Coordinates. When a SudokuGame
    //is given to the updateUI method, we iterate through it by X and Y coordinates and assign the values to the
    //appropriate TextField therein. This means we don't need to hold a reference variable for every god damn
    //text field in this app; which would be awful.
    //The Key (<Key, Value> -> <Coordinates, Integer>) will be the HashCode of a given InputField for ease of lookup
    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

//    Przekazywanie eventów (zdarzenia wywoływane przez uzytkownika)
    private UserInterfaceContract.EventListener listener;

    //  Rozmiar okna (Y - wysokość, X - szerokość)
    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;

    //  Odległość pomiędzy oknem a planszą
    private static final double BOARD_PADDING = 50;

    //  Rozmiar planszy; 576 = 64 units (rozmiar pojedynczego pola) razy 9
    private static final double BOARD_X_AND_Y = 576;

    //  Kolory tła (okna i planszy)
    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(230, 115, 159);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(241, 212, 212);
    private static final String SUDOKU = "SUDOKU";

    /**
     * Stage and Group are JavaFX specific classes for modifying the UI. Think of them as containers of various UI
     * components.
     *
     * A HashMap is a data structure which stores key/value pairs. Rather than creating a member variable for every
     * SudokuTextField object (all 81 of them), I instead store these references within a HashMap, and I retrieve
     * them by using their X and Y Coordinates as a "key" (a unique value used to look something up).
     *
     * @param stage
     */
    public UserInterface(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        this.textFieldCoordinates = new HashMap<>();
        initializeUserInterface();
    }


    @Override
    public void setListener(UserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

//    User Interface components helper methods
    public void initializeUserInterface() {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    /**
     * 1. Draw each TextField based on x and y values.
     * 2. As each TextField is drawn, add it's coordinates (x, y) based on it's Hash Value to
     * to the HashMap.
     *
     * @param root
     */
    private void drawTextFields(Group root) {
        // punkt, od którego rysowane są pola tekstowe (równy wartości paddingu)
        final int xOrigin = 50;
        final int yOrigin = 50;

        // o ile zwiększana jest wartość x i y po każdej iteracji
        final int xAndYDelta = 64;

        // W pętli następuje rysowanie pól i przypisywanie wartości do współrzędnych x i y

        // int x = xOrigin + xIndex * xAndYDelta;
        // x1 = 50 + 0 * 64 = 50
        // x2 = 50 + 1 * 64 = 114
        // x3 = 50 + 2 * 64 = 178
        // etc.

        // int y = yOrigin + yIndex * xAndYDelta;
        // y1 = 50 + 0 * 64 = 50
        // y2 = 50 + 1 * 64 = 114
        // y3 = 50 + 2 * 64 = 178
        // etc.

        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                int x = xOrigin + xIndex * xAndYDelta;
                int y = yOrigin + yIndex * xAndYDelta;
                //draw it
                SudokuTextField tile = new SudokuTextField(xIndex, yIndex);

                // stylowanie pól według parametrów metody styleSudokuTile
                styleSudokuTile(tile, x, y);

                //Note: Note that UserInterface implements EventHandler<ActionEvent> in the class declaration.
                //By passing "this" (which means the current instance of UserInterface), when an action occurs,
                //it will jump straight to "handle(ActionEvent actionEvent)" down below.
                // Nasłuchiwanie na zdarzenia wywołane przez użytkownika
                tile.setOnKeyPressed(this);

                // ustawienie wartości współrzędnych (przypisanie do klucza w hashmapie)
                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                root.getChildren().add(tile);
            }
        }
    }

    /**
     * Helper method for styling a sudoku tile number
     * @param tile
     * @param x
     * @param y
     */
    private void styleSudokuTile(SudokuTextField tile, double x, double y) {
        Font numberFont = new Font(32);
        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);

        // szerokość i wysokość pojedynczego pola 64 x 64 units
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }


    /**
     * In order to draw the various lines that make up the Sudoku grid, we use a starting x and y offset
     * value (remember, x grows positively from left to right, and y grows positively from top to bottom).
     * Each square is meant to be 64x64 units, so we add that number each time a
     * @param root
     */
    private void drawGridLines(Group root) {
        // Rysowanie linii zaczyna się w punktach 114x i 114y (bo 50px - padding i 64px - pierwsze pole):
        int xAndY = 114;
        int index = 0;

        // Rysowanie linii w pętli for (łącznie 8 linii)
        for (int i = 0; i < 8; i++) {
            int thickness;
            // Linie będące krawędziami dużych kwadratów (linia trzecia i szósta) są grubsze (thickness ustawiony na 4)
            if (index == 2 || index == 5) {
                thickness = 4;
            } else {
                thickness = 1;
            }

            // Do wyrysowania linii na planszy używam klasy Rectangle pochodzącej z biblioteki JavaFX
            Rectangle verticalLine = getLine(
                    xAndY + 64 * index,
                    BOARD_PADDING,
                    BOARD_X_AND_Y,
                    thickness
                    );

            Rectangle horizontalLine = getLine(
                    BOARD_PADDING,
                    xAndY + 64 * index,
                    thickness,
                    BOARD_X_AND_Y
            );

            root.getChildren().addAll(
                    verticalLine,
                    horizontalLine
            );

            index++;
        }
    }

    /**
     * Convenience method to reduce repetitious code.
     *
     * X, Y, Height, Width,
     * @return A Rectangle to specification
     */
    public Rectangle getLine(double x, double y, double height, double width){
        Rectangle line = new Rectangle();

        line.setX(x);
        line.setY(y);

        line.setHeight(height);
        line.setWidth(width);

        line.setFill(Color.BLACK);
        return line;

    }

    /**
     * Ustawienie tła dla planszy
     * @param root
     */
    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WINDOW_X, WINDOW_Y);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    /**
     * Ustawienie parametrów planszy takich jak wysokość, szerokość, padding, kolor
     * @param root
     */
    private void drawSudokuBoard(Group root) {
        Rectangle boardBackground = new Rectangle();
        boardBackground.setX(BOARD_PADDING);
        boardBackground.setY(BOARD_PADDING);
        boardBackground.setWidth(BOARD_X_AND_Y);
        boardBackground.setHeight(BOARD_X_AND_Y);
        boardBackground.setFill(BOARD_BACKGROUND_COLOR);
        root.getChildren().add(boardBackground);
    }

    /**
     * Ustawienie tytułu "SUDOKU" w dolnej części okna, pod planszą
     * @param root
     */
    private void drawTitle(Group root) {
        Text title = new Text(235, 690, SUDOKU);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(45);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    /**
     * Za każdym razem, gdy użytkownik wpisuje wartość, interface użytkownika aktualizowany jest odpowiednio
     */
    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        String value = Integer.toString(input);

        if (value.equals("0")) { value = ""; }

        tile.textProperty().setValue(value);
    }

    // aktualizacja planszy o wartość wpisaną przez użytkownika
    @Override
    public void updateBoard(SudokuGame game) {
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                TextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));

                String value = Integer.toString(
                        game.getCopyOfGridState()[xIndex][yIndex]
                );

                if (value.equals("0")) value = "";
                tile.setText( value );

                //If a given tile has a non-zero value and the state of the game is GameState.NEW, then mark
                //the tile as read only. Otherwise, ensure that it is NOT read only.
                if (game.getGameState() == GameState.NEW){
                    if (value.equals("")) {
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                    } else {
                        tile.setStyle("-fx-opacity: 0.6;");
                        tile.setDisable(true);
                    }
                }
            }
        }
    }

    // Komunikat o wygranej na zakończenie gry
    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    // Komunikat o błędzie
    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getText().equals("0")
                    || event.getText().equals("1")
                    || event.getText().equals("2")
                    || event.getText().equals("3")
                    || event.getText().equals("4")
                    || event.getText().equals("5")
                    || event.getText().equals("6")
                    || event.getText().equals("7")
                    || event.getText().equals("8")
                    || event.getText().equals("9")
            ) { // ustawianie wartości wpisanej przez użytkownika
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
                // czyszczenie pola (wpisując 0 lub backspace)
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((TextField)event.getSource()).setText("");
            }
        }

        event.consume();
    }

    /**
     * @param value  expected to be an integer from 0-9, inclusive
     * @param source the textfield object that was clicked.
     */
    private void handleInput(int value, Object source) {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value
        );
    }
}
