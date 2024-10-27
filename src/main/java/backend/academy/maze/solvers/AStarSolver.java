package backend.academy.maze.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import lombok.AllArgsConstructor;

/**
 * Класс AStarSolver предназначен для нахождения пути в лабиринте
 * алгоритмом A* (A star).
 */
public class AStarSolver implements Solver {

    /**
     * Находит путь между двумя точками в лабиринте алгоритмом A*.
     * Алгоритм реализован на очереди с приоритетом. Таким образом сверху очереди
     * будут всплывать ноды с наименьшей стоимостью пути. Сначала добавляем стартовую точку.
     * Пока nodesToSee не опустеет извлекаем из неё текущий элемент: если он равен финишной
     * точке, то заканчиваем; иначе проверяем соседей текущей точки и добавляем их в очередь
     * с расчетом текущей стоимости. Стоимость рассчитывается через Манхеттенское расстояние.
     * Алгоритм A* работает быстрее, нежели DFS, поскольку учитывает стоимость пути.
     *
     * @param maze   лабиринт, в котором нужно искать путь
     * @param start  начальная точка
     * @param finish конечная точка
     * @return список из координат найденного пути (в порядке от начало до конца)
     */
    @Override
    public LinkedList<Coordinate> solve(Maze maze, Coordinate start, Coordinate finish) {
        if (maze.getCellType(start) == Cell.Type.WALL || maze.getCellType(finish) == Cell.Type.WALL) {
            return new LinkedList<>();
        }

        PriorityQueue<Node> nodesToSee = new PriorityQueue<>(Comparator.comparingInt(node -> node.totalCost));
        HashSet<Coordinate> visited = new HashSet<>();
        HashMap<Coordinate, Coordinate> cameFromPoint = new HashMap<>();

        int estimatedLeftPathCost = getManhattanDistance(start, finish);
        nodesToSee.add(new Node(start, 0, estimatedLeftPathCost, estimatedLeftPathCost));
        while (!nodesToSee.isEmpty()) {
            Node current = nodesToSee.poll();

            if (current.pos.equals(finish)) {
                Coordinate currentPoint = current.pos;
                LinkedList<Coordinate> path = new LinkedList<>();
                while (currentPoint != null) {
                    path.add(currentPoint);
                    currentPoint = cameFromPoint.get(currentPoint);
                }
                Collections.reverse(path);
                return path;
            }

            visited.add(current.pos);
            for (Coordinate neighbor : getNeighbors(maze, current.pos)) {
                if (visited.contains(neighbor) || maze.getCellType(neighbor) == Cell.Type.WALL) {
                    continue;
                }

                int currentPathCost = current.currentPathCost + 1;
                cameFromPoint.put(neighbor, current.pos);
                estimatedLeftPathCost = getManhattanDistance(neighbor, finish);
                Node neighborNode = new Node(neighbor, currentPathCost, estimatedLeftPathCost,
                    currentPathCost + estimatedLeftPathCost);
                nodesToSee.add(neighborNode);
            }
        }

        return new LinkedList<>();
    }

    /**
     * Рассчитывает Манхеттенское расстояние между двумя точками
     *
     * @param a первая точка для расчета
     * @param b вторая точка для расчета
     * @return Манхеттенское расстояние между точками
     */
    private int getManhattanDistance(Coordinate a, Coordinate b) {
        return Math.abs(a.row() - b.row()) + Math.abs(a.col() - b.col());
    }

    /**
     * Ищет новые соседние точки рядом с данной для просмотра.
     *
     * @param maze лабиринт, в котором содержится точка
     * @param c    проверяемая точка
     * @return список из координат соседей
     */
    private List<Coordinate> getNeighbors(Maze maze, Coordinate c) {
        List<Coordinate> neighbors = new ArrayList<>();
        int row = c.row();
        int col = c.col();

        if (row > 0) {
            neighbors.add(new Coordinate(row - 1, col));
        }
        if (row + 1 < maze.height()) {
            neighbors.add(new Coordinate(row + 1, col));
        }
        if (col > 0) {
            neighbors.add(new Coordinate(row, col - 1));
        }
        if (col + 1 < maze.width()) {
            neighbors.add(new Coordinate(row, col + 1));
        }

        return neighbors;
    }

    /** Класс Node по сути необходим для хранения точки и стоимости пути */
    @AllArgsConstructor
    static class Node {
        Coordinate pos;
        int currentPathCost;
        int estimatedLeftPathCost;
        int totalCost;
    }
}

