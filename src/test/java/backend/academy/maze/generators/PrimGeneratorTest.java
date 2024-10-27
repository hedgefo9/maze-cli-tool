package backend.academy.maze.generators;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PrimGeneratorTest {
    private PrimGenerator generator;
    private Maze maze;
    private int height;
    private int width;

    @BeforeEach
    void setUp() {
        generator = new PrimGenerator();
        height = 10;
        width = 10;
        maze = generator.generate(height, width);
    }

    @Test
    void testInitialSetup() {
        assertEquals(height, maze.height());
        assertEquals(width, maze.width());

        boolean foundPassage = false;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (maze.getCellType(new Coordinate(row, col)) == Cell.Type.PASSAGE) {
                    foundPassage = true;
                    break;
                }
            }
        }
        assertTrue(foundPassage, "There should be at least one passage cell in the maze.");
    }

    @Test
    void testConnectivityOfPassages() {
        Set<Coordinate> visited = new HashSet<>();
        Coordinate start = findAnyPassageCoordinate();
        explorePassages(start, visited);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Coordinate c = new Coordinate(row, col);
                if (maze.getCellType(c) == Cell.Type.PASSAGE) {
                    assertTrue(visited.contains(c), "All passage cells should be reachable from each other.");
                }
            }
        }
    }

    @Test
    void testMazeHasNoDisconnectedPassages() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Coordinate c = new Coordinate(row, col);
                if (maze.getCellType(c) == Cell.Type.PASSAGE) {
                    boolean hasAdjacentPassage = false;
                    for (Coordinate neighbor : getNeighbors(c)) {
                        if (maze.getCellType(neighbor) == Cell.Type.PASSAGE) {
                            hasAdjacentPassage = true;
                            break;
                        }
                    }
                    assertTrue(hasAdjacentPassage, "Each passage cell should have at least one adjacent passage cell.");
                }
            }
        }
    }

    private Coordinate findAnyPassageCoordinate() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Coordinate c = new Coordinate(row, col);
                if (maze.getCellType(c) == Cell.Type.PASSAGE) {
                    return c;
                }
            }
        }
        throw new IllegalStateException("No passage cell found in the maze.");
    }

    private void explorePassages(Coordinate c, Set<Coordinate> visited) {
        if (visited.contains(c) || maze.getCellType(c) != Cell.Type.PASSAGE) {
            return;
        }
        visited.add(c);
        for (Coordinate neighbor : getNeighbors(c)) {
            explorePassages(neighbor, visited);
        }
    }

    private Set<Coordinate> getNeighbors(Coordinate c) {
        Set<Coordinate> neighbors = new HashSet<>();
        int row = c.row();
        int col = c.col();
        if (row > 0) neighbors.add(new Coordinate(row - 1, col));
        if (row < height - 1) neighbors.add(new Coordinate(row + 1, col));
        if (col > 0) neighbors.add(new Coordinate(row, col - 1));
        if (col < width - 1) neighbors.add(new Coordinate(row, col + 1));
        return neighbors;
    }
}
