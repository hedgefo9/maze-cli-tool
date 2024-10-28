package backend.academy.maze.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Класс DepthFirstSearchSolver предназначен для нахождения пути в лабиринте
 * методом поиска в глубину.
 */
public class DepthFirstSearchSolver implements Solver {

    /**
     * Находит путь между двумя точками в лабиринте методом DFS.
     * Начинаем с точки start. Смотрим соседей текущей клетки,
     * при обнаружении непосещенного прохода добавляем его в список соседей.
     * Если список с соседями не пуст, то добавляем точку в дек,
     * чтобы далее можно было либо вернуться, либо в итоге получить путь; иначе
     * возвращаемся назад по деку на одну точку назад, т.к из текущей нет выхода.
     * Повторяем эти шаги до тех пор, пока текущая клетка не станет финишной.
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

        HashMap<Coordinate, Boolean> visited = new HashMap<>();
        Deque<Coordinate> dq = new LinkedList<>();
        SecureRandom sr = new SecureRandom();

        Coordinate current = start;
        visited.put(current, Boolean.TRUE);
        ArrayList<Coordinate> neighbours = new ArrayList<>();

        while (!current.equals(finish)) {
            for (List<Integer> i : List.of(List.of(0, -1), List.of(0, 1), List.of(-1, 0), List.of(1, 0))) {
                Coordinate currentNeighbour = new Coordinate(current.row() + i.getFirst(),
                    current.col() + i.getLast());
                if (!(checkBorders(currentNeighbour, maze.height(), maze.width()))) {
                    continue;
                }
                if (!visited.containsKey(currentNeighbour)
                    && maze.getCellType(currentNeighbour) != Cell.Type.WALL) {
                    neighbours.add(currentNeighbour);
                }
            }
            if (!neighbours.isEmpty()) {
                dq.push(current);

                int index = sr.nextInt(neighbours.size());
                current = neighbours.get(index);
                visited.put(current, Boolean.TRUE);
                neighbours.clear();
                // maze.setCellType(current, Cell.Type.VISITED);
            } else if (!dq.isEmpty()) {
                current = dq.pop();
                visited.put(current, Boolean.TRUE);
            } else {
                break;
            }
        }
        dq.push(current);

        return new LinkedList<>(dq.reversed());
    }

    /**
     * Проверяет л̶и̶ч̶н̶ы̶е̶ границы. Необходимо для проверки, существует ли соседняя клетка.
     *
     * @param c      координата проверяемой точки
     * @param height высота лабиринта, в котором идёт п̶о̶г̶р̶а̶н̶и̶ч̶н̶ы̶й̶ контроль
     * @param width  ширина лабиринта, в котором идёт п̶о̶г̶р̶а̶н̶и̶ч̶н̶ы̶й̶ контроль
     * @return логическое true, если точка в границах поля, иначе false
     */
    public boolean checkBorders(Coordinate c, int height, int width) {
        return (c.row() >= 0) && (c.row() < height) && (c.col() >= 0) && (c.col() < width);
    }
}
