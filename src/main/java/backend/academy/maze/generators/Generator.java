package backend.academy.maze.generators;

import backend.academy.maze.Maze;

/** Интерфейс генератора лабиринта */
public interface Generator {
    /**
     * Генерирует новый лабиринт по заданным размерам
     *
     * @param height желаемая высота лабиринта
     * @param width  желаемая ширина лабиринта
     * @return сгенерированный лабиринт
     */
    Maze generate(int height, int width);
}
