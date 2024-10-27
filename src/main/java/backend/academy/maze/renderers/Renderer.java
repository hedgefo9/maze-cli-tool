package backend.academy.maze.renderers;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Coordinate;
import java.util.LinkedList;

/** Интерфейс для рендера (вывода) лабиринта в какой-либо интерфейс */
public interface Renderer {

    /**
     * Выводит лабиринт
     *
     * @param maze лабиринт
     */
    void render(Maze maze) throws Exception;

    /**
     * Выводит лабиринт с построенным путём
     *
     * @param maze лабиринт
     * @param path найденный путь
     */
    void render(Maze maze, LinkedList<Coordinate> path) throws Exception;
}
