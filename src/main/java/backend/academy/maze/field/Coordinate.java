package backend.academy.maze.field;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс Coordinate представляет собой хранилище для параметров
 * точек - номера строки и номера столбца.
 */
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Coordinate {
    int row;
    int col;
}
