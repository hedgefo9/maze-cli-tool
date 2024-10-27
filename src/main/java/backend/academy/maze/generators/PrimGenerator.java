package backend.academy.maze.generators;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import backend.academy.util.RandomizedSet;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

/** Класс PrimGenerator необходим для генерации лабиринта посредством алгоритма Прима. */
public class PrimGenerator implements Generator {
    /** <i>Безопасный</i> генератор случайных чисел */
    SecureRandom sr;

    {
        sr = new SecureRandom();
    }

    /**
     * Генерирует лабиринт. Сначала лабиринт полностью заполнен стенами.
     * Алгоритм построен на постепенном случайном вырезании проходов:
     * случайно выбираем начальную точку и добавляем её соседей в множество для просмотра. Далее
     * для каждой точки во множестве смотрим есть ли рядом соседи, являющиеся проходами, если да,
     * то рушим стену между ними (в одном случайном направлении) и завершаем итерацию. Также снова
     * добавляем соседей во множество для просмотра. Алгоритм Прима работает быстрее, чем Крускала.
     *
     * @param height желаемая высота лабиринта
     * @param width  желаемая ширина лабиринта
     * @return сгенерированный лабиринт
     */
    @Override
    public Maze generate(int height, int width) {
        Maze maze = new Maze(height, width);
        int row = sr.nextInt(height);
        int col = sr.nextInt(width);

        maze.setCellType(new Coordinate(row, col), Cell.Type.PASSAGE);

        RandomizedSet<Coordinate> pointsToSee = new RandomizedSet<>();
        lookForNewPointsToSee(maze, new Coordinate(row, col), pointsToSee);

        while (!pointsToSee.isEmpty()) {
            Coordinate c = pointsToSee.getRandom();
            col = c.col();
            row = c.row();
            maze.setCellType(c, Cell.Type.PASSAGE);
            pointsToSee.remove(c);

            ArrayList<Direction> dirs = new ArrayList<>(Arrays.asList(Direction.values()));
            while (!dirs.isEmpty()) {
                Direction d = dirs.remove(sr.nextInt(dirs.size()));
                switch (d) {
                    case UP:
                        if (row - 2 >= 0 && maze.getCellType(new Coordinate(row - 2, col)) == Cell.Type.PASSAGE) {
                            maze.setCellType(new Coordinate(row - 1, col), Cell.Type.PASSAGE);
                            dirs.clear();
                        }
                        break;
                    case DOWN:
                        if (row + 2 < height && maze.getCellType(new Coordinate(row + 2, col)) == Cell.Type.PASSAGE) {
                            maze.setCellType(new Coordinate(row + 1, col), Cell.Type.PASSAGE);
                            dirs.clear();
                        }
                        break;
                    case LEFT:
                        if (col - 2 >= 0 && maze.getCellType(new Coordinate(row, col - 2)) == Cell.Type.PASSAGE) {
                            maze.setCellType(new Coordinate(row, col - 1), Cell.Type.PASSAGE);
                            dirs.clear();
                        }
                        break;
                    case RIGHT:
                        if (col + 2 < width && maze.getCellType(new Coordinate(row, col + 2)) == Cell.Type.PASSAGE) {
                            maze.setCellType(new Coordinate(row, col + 1), Cell.Type.PASSAGE);
                            dirs.clear();
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Неизвестное и недопустимое направление: " + d);
                }
            }

            lookForNewPointsToSee(maze, new Coordinate(row, col), pointsToSee);
        }

        return maze;
    }

    /**
     * Ищет новые соседние точки рядом с данной для просмотра и добавляет их во множество pointsToSee.
     *
     * @param maze        лабиринт, в котором содержится точка
     * @param c           проверяемая точка
     * @param pointsToSee множество с точками для просмотра
     */
    void lookForNewPointsToSee(Maze maze, Coordinate c, RandomizedSet<Coordinate> pointsToSee) {
        if (c.row() - 2 >= 0 && maze.getCellType(new Coordinate(c.row() - 2, c.col())) == Cell.Type.WALL) {
            pointsToSee.add(new Coordinate(c.row() - 2, c.col()));
        }

        if (c.row() + 2 < maze.height()
            && maze.getCellType(new Coordinate(c.row() + 2, c.col())) == Cell.Type.WALL) {
            pointsToSee.add(new Coordinate(c.row() + 2, c.col()));
        }

        if (c.col() - 2 >= 0 && maze.getCellType(new Coordinate(c.row(), c.col() - 2)) == Cell.Type.WALL) {
            pointsToSee.add(new Coordinate(c.row(), c.col() - 2));
        }

        if (c.col() + 2 < maze.width()
            && maze.getCellType(new Coordinate(c.row(), c.col() + 2)) == Cell.Type.WALL) {
            pointsToSee.add(new Coordinate(c.row(), c.col() + 2));
        }
    }

    /** Перечисление с направлениями, по которым можно пройти при поиске соседей клетки */
    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}
