package backend.academy.maze;

import backend.academy.maze.field.Coordinate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class User {
    private final PrintStream ps;
    private final Scanner sc;

    /**
     * Конструктор инициализирует поля класса и печатает приветственное сообщение
     * о том, как работать с программой.
     *
     * @param ps поток вывода
     * @param sc инструмент для ввода
     */
    public User(PrintStream ps, Scanner sc) {
        this.ps = ps;
        this.sc = sc;

        ps.println(
            """
                Добро пожаловать в программу "Лабиринт"! Она позволяет генерировать и решать лабиринты
                различных размеров. Для генерации лабиринта вам нужно задать его размеры, а далее для
                решения — задать начальную и конечную точки.
                Текущий алгоритм генерации: алгоритм Прима (выбран по-умолчанию).
                Текущий алгоритм решения: алгоритм A* (выбран по-умолчанию)."""
        );
    }

    /**
     * Получает число, выбранное пользователем
     *
     * @param limit число ограничивающее выбор сверху
     * @return выбранное число (с 0)
     */
    private int getNumberChoice(int limit) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.matches("\\d+")) {
                int i = Integer.parseInt(line) - 1;
                if (i >= 0 && i < limit) {
                    return i;
                } else {
                    ps.println("Введенное число некорректно, пожалуйста, попробуйте еще раз");
                }
            } else {
                ps.println("Введенное значение некорректно, пожалуйста, попробуйте еще раз");
            }
        }
        return -1;
    }

    /**
     * Опрашивает пользователя для генерации лабиринта.
     *
     * @return список из двух чисел (желаемое количество строк и количество столбцов в лабиринте)
     */
    @SuppressFBWarnings("PRMC_POSSIBLY_REDUNDANT_METHOD_CALLS")
    public List<Integer> pollForMazeGeneration() {
        final int sizeLimitForSide = 1000;
        ps.println("Введите размер поля (два числа)");
        ps.println("Введите количество строк:");
        int rows = getNumberChoice(sizeLimitForSide) + 1;
        ps.println("Введите количество столбцов:");
        int columns = getNumberChoice(sizeLimitForSide) + 1;
        ps.println("Программа думает... Ещё немного и всё будет готово. Ожидайте!");
        return Arrays.asList(rows, columns);
    }

    /**
     * Опрашивает пользователя для нахождения пути между двумя точками в лабиринте.
     *
     * @param height высота лабиринта
     * @param width  ширина лабиринта
     * @return список из двух координат (начальная точка и конечная точка)
     */
    public List<Coordinate> pollForPathBuilding(int height, int width) {
        int rowStart;
        int colStart;
        int rowEnd;
        int colEnd;

        ps.println("Введите координаты начальной точки (сначала номер строки, затем на новой строке номер столбца)");
        rowStart = getNumberChoice(height);
        colStart = getNumberChoice(width);
        ps.println("Введите координаты конечной точки (сначала номер строки, затем на новой строке номер столбца)");
        rowEnd = getNumberChoice(height);
        colEnd = getNumberChoice(width);
        ps.println("Программа мощно перекладывает байтики... Ожидайте!");

        return Arrays.asList(new Coordinate(rowStart, colStart), new Coordinate(rowEnd, colEnd));
    }

    /**
     * Машет пользователю ручкой (прощается в общем)
     */
    public void printGoodByeMessage() {
        ps.println("До свидания!");
    }

    /**
     * Опрашивает пользователя для выбора пункта из меню.
     *
     * @return опция из перечисления Choice
     */
    public Choice pollForMenuChoice() {
        ps.println(
            """
                Выберите действие из доступных ниже (одно число):
                1) Сгенерировать лабиринт и сделать его текущим
                2) Решить текущий лабиринт
                3) Напечатать текущий лабиринт
                4) Сменить алгоритм генерации лабиринта
                5) Сменить алгоритм решения лабиринта
                6) Выйти"""
        );
        final int choicesNumber = 6;
        return Choice.values()[getNumberChoice(choicesNumber)];
    }

    /**
     * Опрашивает пользователя для смены алгоритма генерации лабиринта.
     *
     * @return номер желаемой реализации
     */
    public int pollForGenerateMazeAlgorithmChange() {
        ps.println(
            """
                Выберите алгоритм из доступных ниже (одно число):
                1) алгоритм Прима
                2) алгоритм Краскала"""
        );
        final int generateMazeAlgorithmNumber = 2;
        int choice = getNumberChoice(generateMazeAlgorithmNumber);
        ps.printf("Выбрана опция %d, алгоритм генерации лабиринта изменён.%n", choice + 1);
        return choice;
    }

    /**
     * Опрашивает пользователя для смены алгоритма построения пути в лабиринте.
     *
     * @return номер желаемой реализации
     */
    public int pollForSolveMazeAlgorithmChange() {
        ps.println(
            """
                Выберите алгоритм из доступных ниже (одно число):
                1) алгоритм A* (A star)
                2) алгоритм поиска в глубину"""
        );
        final int generateMazeAlgorithmNumber = 2;
        int choice = getNumberChoice(generateMazeAlgorithmNumber);
        ps.printf("Выбрана опция %d, алгоритм решения лабиринта изменён.%n", choice + 1);
        return choice;
    }

    /**
     * Предупреждает пользователя о том, что лабиринт ещё не сгенерирован
     */
    public void printWarningAboutNoMazeToDealWith() {
        ps.println("Нет лабиринта для взаимодействия! Сначала его нужно сгенерировать :)");
    }
}
