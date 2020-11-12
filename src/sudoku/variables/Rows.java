package sudoku.variables;

/**
 * Enum zawierający oznaczenie wierszy w pojedynczym kwadracie
 * Square jest złożony z 9 pól 3x3:
 * 3 x TOP
 * 3 x MIDDLE
 * 3 x BOTTOM
 * Na planszy jest łącznie 9 takich kwadratów
 *
 * Enum zapewnia większą czytelność przy walidacji wpisanych wartości (cyfry od 1 do 9) w pojedynczym kwadracie
 */
public enum Rows {
    TOP,
    MIDDLE,
    BOTTOM
}