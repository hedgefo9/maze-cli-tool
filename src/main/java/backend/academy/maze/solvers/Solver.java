package backend.academy.maze.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Coordinate;
import java.util.LinkedList;

/** Интерфейс для нахождения пути в лабиринте */
public interface Solver {
    /**
     * Находит путь между двумя точками в лабиринте
     *
     * @param maze   лабиринт, в котором нужно искать путь
     * @param start  начальная точка
     * @param finish конечная точка
     * @return список из координат найденного пути (в порядке от начало до конца)
     */
    LinkedList<Coordinate> solve(Maze maze, Coordinate start, Coordinate finish);
}
