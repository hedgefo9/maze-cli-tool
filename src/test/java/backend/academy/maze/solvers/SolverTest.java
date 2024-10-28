package backend.academy.maze.solvers;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolverTest {
    private Maze maze;
    private static final Coordinate START = new Coordinate(0, 0);
    private static final Coordinate FINISH = new Coordinate(4, 4);

    @BeforeEach
    void setUp() {
        maze = new Maze(5, 5);
        createTestMaze();
    }

    static List<Solver> provideSolvers() {
        return List.of(new DepthFirstSearchSolver(), new AStarSolver());
    }

    @ParameterizedTest
    @MethodSource("provideSolvers")
    void testSolveReturnsNonEmptyPath(Solver solver) {
        LinkedList<Coordinate> path = solver.solve(maze, START, FINISH);
        assertNotEquals(0, path.size(), "Path should not be empty when a solution is possible.");
    }

    @ParameterizedTest
    @MethodSource("provideSolvers")
    void testSolvePathStartAndFinish(Solver solver) {
        LinkedList<Coordinate> path = solver.solve(maze, START, FINISH);
        assertEquals(START, path.getFirst(), "Path should start at the start coordinate.");
        assertEquals(FINISH, path.getLast(), "Path should end at the finish coordinate.");
    }

    @ParameterizedTest
    @MethodSource("provideSolvers")
    void testSolvePathExists(Solver solver) {
        LinkedList<Coordinate> path = solver.solve(maze, START, FINISH);
        for (int i = 1; i < path.size(); i++) {
            Coordinate prev = path.get(i - 1);
            Coordinate current = path.get(i);
            assertTrue(isNeighbor(prev, current), "Each step in the path should be to a neighboring cell.");
        }
    }

    private boolean isNeighbor(Coordinate a, Coordinate b) {
        int rowDiff = Math.abs(a.row() - b.row());
        int colDiff = Math.abs(a.col() - b.col());
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }

    private void createTestMaze() {
        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                maze.setCellType(new Coordinate(row, col), Cell.Type.WALL);
            }
        }
        maze.setCellType(START, Cell.Type.PASSAGE);
        maze.setCellType(FINISH, Cell.Type.PASSAGE);

        maze.setCellType(new Coordinate(0, 1), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(1, 1), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(2, 1), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(3, 1), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(4, 1), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(4, 2), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(4, 3), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(4, 4), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(3, 3), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(2, 3), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(1, 3), Cell.Type.PASSAGE);
        maze.setCellType(new Coordinate(1, 4), Cell.Type.PASSAGE);
    }
}

