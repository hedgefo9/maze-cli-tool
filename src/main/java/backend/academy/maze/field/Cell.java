package backend.academy.maze.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс Cell представляет собой хранилище информации о клетке,
 * а именно об: координате и типе поверхности.
 */
@Getter
@Setter
@AllArgsConstructor
public class Cell {

    /**
     * Перечисление Type представление собой хранилище
     * для обозначений разных типов клеток
     */
    @AllArgsConstructor
    public enum Type {
        /** Символ стены */
        WALL("\uD83D\uDFE5"),
        /** Символ прохода */
        PASSAGE("\uD83D\uDFE8"),
        /** Символ начальной позиции при поиске пути */
        START("\uD83D\uDFE6"),
        /** Символ конечной позиции при поиске пути */
        FINISH("\uD83D\uDFEA"),
        /** Символ самого пути при поиске пути */
        PATH("\uD83D\uDFE9");

        private final String symbol;

        /**
         * Преобразовывает объект перечисления Type в строку
         *
         * @return строка, соответствующая типу клетки (символы стены, прохода и т.д), т.е symbol
         */
        @Override
        public String toString() {
            return symbol;
        }
    }

    /** Координата клетки */
    private Coordinate c;
    /** Тип клетки */
    private Type type;

    /**
     * Преобразовывает объект клетки в строку
     *
     * @return строка, соответствующая типу клетки (символы стены, прохода и т.д.)
     */
    @Override
    public String toString() {
        return type.toString();
    }
}
