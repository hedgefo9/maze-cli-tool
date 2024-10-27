package backend.academy;

import backend.academy.maze.Choice;
import backend.academy.maze.Maze;
import backend.academy.maze.User;
import backend.academy.maze.field.Coordinate;
import backend.academy.maze.generators.Generator;
import backend.academy.maze.generators.KruskalGenerator;
import backend.academy.maze.generators.PrimGenerator;
import backend.academy.maze.renderers.ConsoleRender;
import backend.academy.maze.solvers.AStarSolver;
import backend.academy.maze.solvers.DepthFirstSearchSolver;
import backend.academy.maze.solvers.Solver;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    InputStream is = System.in;
    OutputStream os = System.out;
    Charset charset = StandardCharsets.UTF_8;

    public static void main(String[] args) throws Exception {
        PrintStream ps = new PrintStream(os, true, charset);
        Scanner sc = new Scanner(is, charset);
        User user = new User(ps, sc);

        ConsoleRender cr = new ConsoleRender(ps);
        Generator[] generators = new Generator[] {new PrimGenerator(), new KruskalGenerator()};
        Solver[] solvers = new Solver[] {new AStarSolver(), new DepthFirstSearchSolver()};
        Generator generator = generators[0];
        Solver solver = solvers[0];
        Maze maze = null;

        Choice action = user.pollForMenuChoice();
        while (action != Choice.QUIT) {
            if (action == Choice.GENERATE_MAZE) {
                List<Integer> mazeSize = user.pollForMazeGeneration();
                maze = generator.generate(mazeSize.getFirst(), mazeSize.getLast());
                cr.render(maze);
            } else if (action == Choice.SOLVE_MAZE) {
                if (maze != null) {
                    List<Coordinate> points = user.pollForPathBuilding(maze.height(), maze.width());
                    LinkedList<Coordinate> path = solver.solve(maze, points.getFirst(), points.getLast());
                    cr.render(maze, path);
                } else {
                    user.printWarningAboutNoMazeToDealWith();
                }
            } else if (action == Choice.PRINT_MAZE) {
                if (maze != null) {
                    cr.render(maze);
                } else {
                    user.printWarningAboutNoMazeToDealWith();
                }
            } else if (action == Choice.CHANGE_GENERATE_MAZE_ALGORITHM) {
                int choice = user.pollForGenerateMazeAlgorithmChange();
                generator = generators[choice];
            } else if (action == Choice.CHANGE_SOLVE_MAZE_ALGORITHM) {
                int choice = user.pollForSolveMazeAlgorithmChange();
                solver = solvers[choice];
            }

            action = user.pollForMenuChoice();
        }

        user.printGoodByeMessage();
    }
}
