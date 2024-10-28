package backend.academy.maze.renderers;

import backend.academy.maze.Maze;
import backend.academy.maze.field.Cell;
import backend.academy.maze.field.Coordinate;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;
import static java.lang.Math.max;

/**
 * Класс ConsoleRenderer нужен для вывода лабиринта и прочей полезной информации
 * в консоль. Предоставляет возможность прокрутки лабиринта в окне фиксированного размера,
 * таким образом даже лабиринты огромных размеров могут быть выведены. Функционал протестирован
 * на максимальном размере в 10000x10000, но потенциально всё ограничивается только памятью и
 * здравым смыслом. Всё взаимодействие с пользователем ведётся через консоль.
 */
public class ConsoleRenderer implements Renderer {
    /** Смещение по оси X (отвечает за прокрутку) */
    private int offsetX = 0;
    /** Смещение по оси Y (отвечает за прокрутку) */
    private int offsetY = 0;
    /** Ширина видимой области лабиринта */
    private final static int VIEW_WIDTH = 40;
    /** Высота видимой области лабиринта */
    private final static int VIEW_HEIGHT = 20;
    /** Поток для печати информации */
    private final PrintStream ps;

    /**
     * Конструктор по-умолчанию
     *
     * @param ps поток для печати
     */
    public ConsoleRenderer(PrintStream ps) {
        this.ps = ps;
    }

    /** Очищает экран */
    private void clearScreen() {
        ps.flush();
        ps.print("\033[H\033[2J");
    }

    /** Отображает лабиринт в консоли в фиксированном окне и важную информацию об управлении */
    private void display(Maze maze, Terminal terminal) {
        clearScreen();

        final int columnPrintFrequency = 4;

        Size terminalSize = terminal.getSize();
        int terminalWidth = terminalSize.getColumns();
        // int terminalHeight = terminalSize.getRows();
        int startX = max(0, terminalWidth - VIEW_WIDTH) / columnPrintFrequency;
        // int startY = (terminalHeight - viewHeight) / columnPrintFrequency;

        String title = "[Вид лабиринта]";
        ps.println(" ".repeat(max(0, (terminalWidth - title.length()) / 2)) + title);

        ps.print(" ".repeat(max(0, startX - 2)));
        ps.print("   ");
        for (int x = 0; x < VIEW_WIDTH; x++) {
            int columnNumber = offsetX + x + 1;

            if (x % columnPrintFrequency == 0) {
                String columnLabel = String.valueOf(columnNumber);
                int columnWidth = columnLabel.length();
                ps.print(columnLabel);

                int spacesToNextLabel = max(0, columnPrintFrequency * 2 - columnWidth);
                ps.print(" ".repeat(spacesToNextLabel));
            }
        }
        ps.println();
        ps.println(" ".repeat(startX) + "┌" + "--".repeat(VIEW_WIDTH));

        for (int y = 0; y < VIEW_HEIGHT; y++) {
            int rowNumber = offsetY + y + 1;
            ps.print(" ".repeat(max(0, startX - String.valueOf(rowNumber).length())));

            if (rowNumber % 2 == 0) {
                ps.printf("%d|", rowNumber);  // Нумерация строк
            } else {
                ps.print(" ".repeat(String.valueOf(rowNumber).length()) + "|");
            }

            for (int x = 0; x < VIEW_WIDTH; x++) {
                int mazeX = offsetX + x;
                int mazeY = offsetY + y;

                if (mazeX < maze.width() && mazeY < maze.height()) {
                    ps.print(maze.getCellType(new Coordinate(mazeY, mazeX)).toString());
                } else {
                    ps.print(' ');
                }
            }
            ps.println();
        }

        ps.print("\n\n");
        ps.println(StringUtils.center(String.format("Размеры лабиринта: %d X %d (число строчек X число колонок).",
            maze.height(), maze.width()), terminalWidth));
        ps.println(StringUtils.center(
            "Клавиши для управления: [W] Вверх  [A] Влево  [S] Вниз  [D] Вправо  [Q] Выход", terminalWidth));
        ps.println(StringUtils.center(String.format(
            "Легенда карты: %s - стена, %s - проход, %s - построенный путь, "
                + "%s - старт, %s - финиш",
            Cell.Type.WALL, Cell.Type.PASSAGE, Cell.Type.PATH, Cell.Type.START, Cell.Type.FINISH), terminalWidth));
        ps.println();
        ps.println("[[Программа ожидает ввод, Enter нажимать не нужно...]]");
    }

    /** Обрабатывает ввод и прокручивает лабиринт */
    private void scroll(Maze maze, char command) {
        final int pointsToScrollAtOneTime = 10;
        switch (command) {
            case 'w':
                if (offsetY > 0) {
                    offsetY -= pointsToScrollAtOneTime;
                }
                break;
            case 's':
                if (offsetY < maze.height() - VIEW_HEIGHT) {
                    offsetY += pointsToScrollAtOneTime;
                }
                break;
            case 'a':
                if (offsetX > 0) {
                    offsetX -= pointsToScrollAtOneTime;
                }
                break;
            case 'd':
                if (offsetX < maze.width() - VIEW_WIDTH) {
                    offsetX += pointsToScrollAtOneTime;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Оркестр вывода: вызывает функцию отображения лабиринта и справочной информации,
     * обрабатывает скроллинг и выход из программы.
     *
     * @param maze лабиринт для вывода
     */
    public void render(Maze maze) throws IOException {
        offsetX = 0;
        offsetY = 0;
        Terminal terminal = TerminalBuilder.builder()
            .system(true)
            .jansi(true)
            .build();

        terminal.enterRawMode();
        NonBlockingReader reader = terminal.reader();

        display(maze, terminal);
        while (true) {
            int input = Character.toLowerCase(reader.read());

            if (input == -1) {
                continue;
            }

            char command = (char) input;

            if (command == 'q') {
                terminal.close();
                clearScreen();
                break;
            }

            scroll(maze, command);
            display(maze, terminal);
        }
    }

    /**
     * Также выводит лабиринт, но в котором найден путь между двумя заданными точками.
     * Создает копию переданного лабиринта, а затем добавляет в неё найденный путь.
     *
     * @param maze лабиринт для вывода
     * @param path найденный путь
     */
    public void render(Maze maze, LinkedList<Coordinate> path) throws IOException {
        if (path.isEmpty()) {
            ps.println("Одна или все из указанных точек являются стенами! Попробуйте выбрать другие");
            return;
        }
        Maze mazeWithPath = maze.getCopy();
        for (Coordinate c : path) {
            mazeWithPath.setCellType(c, Cell.Type.PATH);
        }
        mazeWithPath.setCellType(path.getFirst(), Cell.Type.START);
        mazeWithPath.setCellType(path.getLast(), Cell.Type.FINISH);

        render(mazeWithPath);
    }
}
