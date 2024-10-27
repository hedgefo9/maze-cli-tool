package backend.academy.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс RandomizedSet представляет собой множество, хранящее элементы,
 * которые можно добавлять и удалять, и даёт возможность получения
 * случайного элемента в среднем за O(1). Обычный HashSet не дает взять случайный
 * элемент за O(1), та же ситуация с удалением элемента в List, нужно сначала получить
 * все элементы за O(N) и далее получить случайный элемент. Требует дополнительной памяти
 * на хранение списка всех элементов (в дополнении к множеству).
 */
public class RandomizedSet<E> {
    /**
     * Хэш-таблица для хранения индекса, на котором стоит элемент в list
     */
    private final HashMap<E, Integer> itemToIndex;
    /**
     * Список всех элементов во множестве (порядок вставки не сохраняется, т.к
     * при удалении элементы меняются местами ради оптимизации)
     */
    private final ArrayList<E> list;
    /**
     * <i>Безопасный</i> генератор случайных чисел
     */
    SecureRandom sr;

    /**
     * Конструктор по-умолчанию, инициализирует поля пустыми структурами
     */
    public RandomizedSet() {
        itemToIndex = new HashMap<>();
        list = new ArrayList<>();
        sr = new SecureRandom();
    }

    /**
     * Добавляет элемент во множество.
     *
     * @param e элемент для вставки во множество
     * @return результат добавления (true, если элемент добавлен, иначе false)
     */
    public boolean add(E e) {
        if (itemToIndex.containsKey(e)) {
            return false;
        } else {
            boolean isAdded = list.add(e);
            itemToIndex.put(e, list.size() - 1);
            return isAdded;
        }
    }

    /**
     * Удаляет элемент из множества.
     *
     * @param e элемент для удаления из множества
     * @return результат удаления: true, если элемент удалён, иначе false
     */
    public boolean remove(E e) {
        if (!itemToIndex.containsKey(e)) {
            return false;
        }

        int index = itemToIndex.get(e);

        E last = list.getLast();
        list.set(index, last);
        list.removeLast();

        itemToIndex.put(last, index);

        itemToIndex.remove(e);
        return true;
    }

    /**
     * Получает случайный элемент из множества.
     *
     * @return случайный элемент, если в множестве лежат элементы, иначе null
     */
    public E getRandom() {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(sr.nextInt(list.size()));
    }

    /**
     * Проверяет множество на пустоту
     *
     * @return если множество пусто - true, иначе false
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }
}
