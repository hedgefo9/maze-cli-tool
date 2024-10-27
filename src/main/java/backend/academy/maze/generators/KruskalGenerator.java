package backend.academy.maze.generators;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import backend.academy.util.RandomizedSet;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;

/** Класс KruskalGenerator необходим для генерации лабиринта посредством алгоритма Краскала. */
public class KruskalGenerator implements Generator {

    /**
     * Генерирует лабиринт. Алгоритм построен на компонентах. Сначала каждая четная клетка
     * становится проходом и находится в своей отдельной компоненте, но далее циклично
     * объединяются в одну компоненту клетки, которые находятся в разных компонентах путём
     * превращения стены между ними в проход. Алгоритм Краскала работает медленнее, чем Прима.
     *
     * @param height желаемая высота лабиринта
     * @param width  желаемая ширина лабиринта
     * @return сгенерированный лабиринт
     */
    @Override
    public Maze generate(int height, int width) {
        RandomizedSet<Edge> edges = new RandomizedSet<>();
        HashMap<Coordinate, Integer> components = new HashMap<>();

        int componentID = 0;
        Maze maze = new Maze(height, width);

        for (int row = 0; row < height; row += 2) {
            for (int col = 0; col < width; col += 2) {
                maze.setCellType(new Coordinate(row, col), Cell.Type.PASSAGE);
                componentID++;
                components.put(new Coordinate(row, col), componentID);
            }
        }

        for (int row = 0; row < height; row += 2) {
            for (int col = 0; col < width; col += 2) {
                Coordinate current = new Coordinate(row, col);
                for (List<Integer> d : List.of(List.of(0, 2), List.of(2, 0))) {
                    Coordinate neighbor = new Coordinate(current.row() + d.getFirst(), current.col() + d.getLast());
                    if (checkBorders(neighbor, height, width)) {
                        edges.add(new Edge(current, neighbor));
                    }
                }
            }
        }

        while (!edges.isEmpty()) {
            Edge edge = edges.getRandom();
            int component1 = components.get(edge.c1);
            int component2 = components.get(edge.c2);

            if (component1 != component2) {
                Coordinate middlePoint = new Coordinate(
                    (edge.c1.row() + edge.c2.row()) / 2,
                    (edge.c1.col() + edge.c2.col()) / 2
                );
                maze.setCellType(middlePoint, Cell.Type.PASSAGE);

                for (HashMap.Entry<Coordinate, Integer> entry : components.entrySet()) {
                    if (entry.getValue().equals(component2)) {
                        entry.setValue(component1);
                    }
                }
            }
            edges.remove(edge);
        }

        return maze;
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

    /**
     * Класс для хранения ребра. Ребро (c1, c2) означает,
     * что между точками c1 и c2 есть м̶и̶н̶д̶а̶л̶ь̶н̶а̶я̶ связь
     */
    @AllArgsConstructor
    static class Edge {
        Coordinate c1;
        Coordinate c2;
    }
}
