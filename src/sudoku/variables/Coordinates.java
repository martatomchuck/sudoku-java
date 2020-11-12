package sudoku.variables;

import java.util.Objects;

/**
 * Wprowadzenie współrzędnych x i y na dwuwymiarowej planszy gry
 * Współrzędne przechowują lokalizację pojedynczego pola na planszy (mapie współrzędnych)
 */
public class Coordinates {
    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Metody zwracające x i y
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // Nadpisanie metod HashMapy
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}