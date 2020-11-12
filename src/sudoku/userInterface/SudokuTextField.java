package sudoku.userInterface;

import javafx.scene.control.TextField;

// Rozszerzenie klasy SudokuTextField pochodzącej z biblioteki JavaFX
// Implementacja kontrolki do wpisywania tekstu przez użytkownika
public class SudokuTextField extends javafx.scene.control.TextField {
    private final int x;
    private final int y;

    public SudokuTextField(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    @Override
    public void replaceText(int i, int i1, String s) {
        if (!s.matches("[0-9]")) {
            super.replaceText(i, i1, s);
        }
    }

    @Override
    public void replaceSelection(String s) {
        if (!s.matches("[0-9]")) {
            super.replaceSelection(s);
        }
    }
}
