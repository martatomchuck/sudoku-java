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
 * Wygląd i obsługa widoku z planszą do gry
 * Wywołanie okna modalnego z notyfikacją o ukończonej grze
 */
public class UserInterface implements UserInterfaceContract.View, EventHandler<KeyEvent> {

    // Okno w tle, tło dla aplikacji (z biblioteki JavaFX)
    private final Stage stage;

    // Container (z biblioteki JavaFX)
    private final Group root;

    // ZŁA PRAKTYKA: tworzenie zmiennej dla każdego pojedynczego pola na planszy (81 zmiennych)
    // DOBRA PRAKTYKA: HashMap i współrzędne x i y (przy pomocy funkcji HashMap współrzędne x i y mapowane są na odpowiednie pola tekstowe na planszy)
    //
    // <Key, Value> -> <Coordinates, Integer>
    private HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    // Przekazywanie eventów (zdarzenia wywoływane przez użytkownika)
    private UserInterfaceContract.EventListener listener;

    // Rozmiar okna (Y - wysokość, X - szerokość)
    private static final double WINDOW_Y = 732;
    private static final double WINDOW_X = 668;

    // Odległość pomiędzy oknem a planszą
    private static final double BOARD_PADDING = 50;

    // Rozmiar planszy; 576 = 64 units (rozmiar pojedynczego pola) razy 9
    private static final double BOARD_X_AND_Y = 576;

    // Kolory tła (okna i planszy)
    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(230, 115, 159);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(241, 212, 212);
    private static final String SUDOKU = "SUDOKU";

    /**
     * Stage i Group są klasami pochodzącymi z biblioteki JavaFX do modyfikowania UI (a la kontenery dla różnych komponentów UI).
     *
     * HashMap - struktura danych, która przechowuje pary klucz - wartość
     * Zamiast tworzyć zmienną dla każdego pola na planszy Sudoku (łącznie 81),
     * przechowuję w hasmapie referencje do każdego pola i wyciągam przy pomocy klucza - współrzędnych X i Y
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

    // Inicjalizacja UI
    public void initializeUserInterface() {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    /**
     * KROK 1. Rysowanie pól tekstowych (inputów), w które użytkownik będzie wpisywał swoje wartości
     * Pola tesktowe bazują na wartościach x i y
     * KROK 2. Po wyrysowaniu pól tekstowych metoda dodaje ich współrzędne x i y do HashMapy
     * @param root
     */
    private void drawTextFields(Group root) {
        // Punkt, od którego rysowane są pola tekstowe (równy wartości paddingu)
        final int xOrigin = 50;
        final int yOrigin = 50;

        // O tyle zwiększana jest wartość x i y po każdej iteracji
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

                // Generowanie pól
                SudokuTextField tile = new SudokuTextField(xIndex, yIndex);

                // Stylowanie pól według parametrów metody styleSudokuTile
                styleSudokuTile(tile, x, y);

                // Nasłuchiwanie na zdarzenia wywołane przez użytkownika
                tile.setOnKeyPressed(this);

                // Ustawienie wartości współrzędnych (przypisanie do klucza w hashmapie)
                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                root.getChildren().add(tile);
            }
        }
    }

    /**
     * Stylowanie pojedynczego pola
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

        // Szerokość i wysokość pojedynczego pola 64 x 64
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }


    /**
     * Metoda służąca wyrysowaniu linii na planszy
     *
     * Wartości x i y stanowią punkt startowy:
     * x rośnie z lewej do prawej
     * y rośnie z dołu na dół
     *
     * Każde pole ma wielkość 64x64, dlatego w każdej iteracji dodawana jest liczba 64
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
     * Optymalizacja kodu rysującego linie
     * X, Y, Height, Width,
     * @return line
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

    // Za każdym razem, gdy użytkownik wpisuje wartość w danym polu, aktualizowany jest odpowiednio interface użytkownika
    @Override
    public void updateSquare(int x, int y, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(x, y));
        String value = Integer.toString(input);

        if (value.equals("0")) { value = ""; }

        tile.textProperty().setValue(value);
    }

    // Aktualizacja planszy o wartość wpisaną przez użytkownika
    @Override
    public void updateBoard(SudokuGame game) {
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                javafx.scene.control.TextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));

                String value = Integer.toString(
                        game.getCopyOfGridState()[xIndex][yIndex]
                );

                if (value.equals("0")) value = "";
                tile.setText( value );

                // Jeśli dane pole ma wartość niezerową i stan gry = NEW, zaznacz pole jako niemożliwe do edycji
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

    // Komunikat o zakończonej grze (wywołanie okna dialogowego)
    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    // Komunikat o błędzie (wywołanie okna dialogowego)
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
            ) { // Ustawianie wartości wpisanej przez użytkownika
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
                // Czyszczenie pola (wpisując 0 lub backspace)
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((javafx.scene.control.TextField)event.getSource()).setText("");
            }
        }
        event.consume();
    }

    /**
     * @param value  integer 0-9
     * @param source kliknięte pole
     */
    private void handleInput(int value, Object source) {
        listener.onSudokuInput(
                ((SudokuTextField) source).getX(),
                ((SudokuTextField) source).getY(),
                value
        );
    }
}
