# Описание
Программа представляет собой консольное (CLI) приложение, которое позволяет генерировать и решать лабиринты различных размеров. Размеры задаются пользователем. Реализована генерация лабиринтов через алгоритмы Прима и Краскала, а решение - через алгоритмы DFS и A*. Возможно добавление новых реализаций благодаря единому контракту Generator и Solver.

# Сборка и запуск
Производится с помощью следующей команды в корневой папке проекта:
```Maven
mvn package && jar -uvfe target/java-1.0.0.jar backend.academy.Main && java -jar target/java-1.0.0.jar
```

# Использование
Работа с программой идёт через консольный интерфейс. Взаимодействие ведётся с помощью выбора пунктов путём ввода чисел в меню. Для генерации лабиринта нужно задать его размеры, а для решения - начальную и конечную точки. Решать лабиринт можно для разных точек, промежуточные решения не сохраняются в памяти (это сделано сознательно, но при необходимости можно легко изменить код, чтобы изменения сохранялись).

В режиме просмотра лабиринта со скроллингом для управления используются следующие клавиши:
[W] Вверх,  [A] Влево,  [S] Вниз,  [D] Вправо,  [Q] Выход.

Легенда карты: 🟥 - стена, 🟨 - проход, 🟩 - построенный путь, 🟦 - старт, 🟪 - финиш

В программе предусмотрена смена алгоритмов генерации и решения лабиринтов через соответствующие пункты в меню. Быстрее всего работают алгоритм Прима для генерации и A* для решения, они как раз и выбраны по-умолчанию.

Также проведено тестирование для поиска предельных значений размеров. Работа программы проверена на лабиринтах до 10000 X 10000 (100 миллионов ячеек), всё функционирует корректно, однако на всякий случай поставлено ограничение в 1000 X 1000 (вплоть до 7000 X 7000 выделение дополнительной памяти через конфигурационные параметры при компиляции не требуется), поскольку при нем программа работает быстро и стабильно.

# Скриншоты
## Основной интерфейс программы
![image_2024-11-18_05-16-01](https://github.com/user-attachments/assets/ae378d60-d59a-4bc2-b8ba-5ea40c8f97f7)


## Лабиринт размером 100 X 100 и его решение
![image_2024-11-18_05-10-37](https://github.com/user-attachments/assets/297b7ca1-2b3a-4aab-a9d7-e1ca0be31278)
![image_2024-11-18_05-10-37 (2)](https://github.com/user-attachments/assets/f10d0328-2d57-438e-ae49-4f04c511a62b)
![image_2024-11-18_05-10-38](https://github.com/user-attachments/assets/2ac10f57-25c1-4c12-9b43-84c89a8af312)


## Лабиринт размером 1000 X 1000 и его решение
![image_2024-11-18_05-15-54](https://github.com/user-attachments/assets/eb5b5643-8c5c-4377-8e8a-c5b4a3e48d8d)
![image_2024-11-18_05-15-54 (2)](https://github.com/user-attachments/assets/3e526d62-6f85-423d-adb5-72a19577df7a)
![image_2024-11-18_05-15-55](https://github.com/user-attachments/assets/09a5b8ec-6e7e-4cda-8f4b-62b1424db39d)



