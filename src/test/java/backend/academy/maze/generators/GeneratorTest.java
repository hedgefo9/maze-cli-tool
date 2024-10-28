package backend.academy.maze.generators;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneratorTest {
    private final static int HEIGHT = 10;
    private final static int WIDTH = 10;

    static List<Generator> generatorProvider() {
        return List.of(new KruskalGenerator(), new PrimGenerator());
    }

    @ParameterizedTest
    @MethodSource("generatorProvider")
    void testMazeDimensions(Generator generator) {
        Maze maze = generator.generate(HEIGHT, WIDTH);
        assertEquals(HEIGHT, maze.height());
        assertEquals(WIDTH, maze.width());
    }

    @ParameterizedTest
    @MethodSource("generatorProvider")
    void testGeneratedMazeContainsPassage(Generator generator) {
        Maze maze = generator.generate(HEIGHT, WIDTH);
        assertDoesNotThrow(() -> findAnyPassage(maze), "Maze should contain at least one passage cell.");
    }

    @ParameterizedTest
    @MethodSource("generatorProvider")
    void testNoFloatingPassages(Generator generator) {
        Maze maze = generator.generate(HEIGHT, WIDTH);
        for (int row = 1; row < HEIGHT - 1; row++) {
            for (int col = 1; col < WIDTH - 1; col++) {
                if (maze.getCellType(new Coordinate(row, col)) == Cell.Type.PASSAGE) {
                    boolean hasAdjacentPassage = false;
                    for (Coordinate neighbor : getNeighbors(new Coordinate(row, col))) {
                        if (maze.getCellType(neighbor) == Cell.Type.PASSAGE) {
                            hasAdjacentPassage = true;
                            break;
                        }
                    }
                    assertTrue(hasAdjacentPassage, "Passage cells should connect to other passages.");
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("generatorProvider")
    void testConnectivityOfPassages(Generator generator) {
        Maze maze = generator.generate(HEIGHT, WIDTH);
        Set<Coordinate> visited = new HashSet<>();
        Coordinate start = findAnyPassage(maze);
        explorePassages(maze, start, visited);

        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                Coordinate c = new Coordinate(row, col);
                if (maze.getCellType(c) == Cell.Type.PASSAGE) {
                    assertTrue(visited.contains(c), "All passage cells should be reachable from each other.");
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("generatorProvider")
    void testPassageDensity(Generator generator) {
        Maze maze = generator.generate(HEIGHT, WIDTH);
        long passageCount = 0;
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (maze.getCellType(new Coordinate(row, col)) == Cell.Type.PASSAGE) {
                    passageCount++;
                }
            }
        }
        double passageRatio = (double) passageCount / (HEIGHT * WIDTH);
        System.out.println(passageRatio);
        assertTrue(passageRatio >= 0.3 && passageRatio <= 0.7, "Passage density should be between 30% and 70%");
    }


    private void explorePassages(Maze maze, Coordinate c, Set<Coordinate> visited) {
        if (visited.contains(c) || maze.getCellType(c) != Cell.Type.PASSAGE) {
            return;
        }
        visited.add(c);
        for (Coordinate neighbor : getNeighbors(c)) {
            explorePassages(maze, neighbor, visited);
        }
    }

    private Coordinate findAnyPassage(Maze maze) {
        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                Coordinate c = new Coordinate(row, col);
                if (maze.getCellType(c) == Cell.Type.PASSAGE) {
                    return c;
                }
            }
        }
        throw new IllegalStateException("No passage cell found in the maze.");
    }

    private Set<Coordinate> getNeighbors(Coordinate c) {
        Set<Coordinate> neighbors = new HashSet<>();
        int row = c.row();
        int col = c.col();
        if (row > 0) {
            neighbors.add(new Coordinate(row - 1, col));
        }
        if (row < HEIGHT - 1) {
            neighbors.add(new Coordinate(row + 1, col));
        }
        if (col > 0) {
            neighbors.add(new Coordinate(row, col - 1));
        }
        if (col < WIDTH - 1) {
            neighbors.add(new Coordinate(row, col + 1));
        }
        return neighbors;
    }
}
