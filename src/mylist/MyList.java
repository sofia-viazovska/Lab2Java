package mylist;

/**
 * Власний інтерфейс списку (аналог java.util.List, без успадкування від нього).
 */
public interface MyList {

    /** Додає елемент в кінець списку. */
    void add(Object e);

    /** Додає елемент у вказане місце списку. */
    void add(int index, Object element);

    /** Додає масив елементів в кінець списку. */
    void addAll(Object[] c);

    /** Додає масив елементів у вказане місце списку. */
    void addAll(int index, Object[] c);

    /** Повертає елемент за індексом. */
    Object get(int index);

    /** Видаляє елемент за індексом і повертає його. */
    Object remove(int index);

    /** Змінює значення елемента за індексом. */
    void set(int index, Object element);

    /** Пошук індексу першого входження елемента за значенням (equals). -1, якщо не знайдено. */
    int indexOf(Object o);

    /** Розмір списку. */
    int size();

    /** Перетворює список в масив об'єктів. */
    Object[] toArray();
}
