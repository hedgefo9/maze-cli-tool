package backend.academy.maze;

import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс представляет собой хранилище для лабиринта и его параметров.
 */
@Getter
@AllArgsConstructor
public class Maze {
    private final int height;
    private final int width;
    private final Cell[][] grid;

    /**
     * Конструктор инициализирует поля и заполняет изначально все клетки стенами.
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     */
    public Maze(int height, int width) {
        this.height = height;
        this.width = width;
        grid = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(new Coordinate(row, col), Cell.Type.WALL);
            }
        }
    }

    /**
     * Получает тип указанной клетки
     *
     * @param c клетка
     */
    public Cell.Type getCellType(Coordinate c) {
        return grid[c.row()][c.col()].type();
    }

    /**
     * Обновляет тип указанной клетки
     *
     * @param c клетка
     */
    public void setCellType(Coordinate c, Cell.Type newType) {
        grid[c.row()][c.col()].type(newType);
    }

    /**
     * Создает deep copy текущего лабиринта
     *
     * @return копия текущего лабиринта
     */
    public Maze getCopy() {
        Cell[][] newGrid = new Cell[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                newGrid[row][col] = new Cell(new Coordinate(row, col), grid[row][col].type());
            }
        }
        return new Maze(height, width, newGrid);
    }
}
