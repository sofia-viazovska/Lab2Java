package mylist;

import java.util.Objects;
import java.util.RandomAccess;

/**
 * Реалізація MyList на основі масиву, що динамічно розширюється.
 * Реалізує маркерний інтерфейс RandomAccess, оскільки доступ за
 * індексом тут виконується за O(1) (на відміну від MyLinkedList).
 *
 * Java-класи java.util.ArrayList/List не використовуються.
 */
public class MyArrayList implements MyList, RandomAccess {

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY = new Object[0];

    private Object[] elements;
    private int size;

    public MyArrayList() {
        elements = EMPTY;
        size = 0;
    }

    public MyArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Ємність не може бути від'ємною: " + initialCapacity);
        }
        elements = initialCapacity == 0 ? EMPTY : new Object[initialCapacity];
        size = 0;
    }

    @Override
    public void add(Object e) {
        ensureCapacity(size + 1);
        elements[size++] = e;
    }

    @Override
    public void add(int index, Object element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }

    @Override
    public void addAll(Object[] c) {
        addAll(size, c);
    }

    @Override
    public void addAll(int index, Object[] c) {
        checkIndexForAdd(index);
        if (c == null || c.length == 0) {
            return;
        }
        ensureCapacity(size + c.length);
        System.arraycopy(elements, index, elements, index + c.length, size - index);
        System.arraycopy(c, 0, elements, index, c.length);
        size += c.length;
    }

    @Override
    public Object get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public Object remove(int index) {
        checkIndex(index);
        Object removed = elements[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // прибираємо посилання, щоб не заважати GC
        return removed;
    }

    @Override
    public void set(int index, Object element) {
        checkIndex(index);
        elements[index] = element;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(elements[i], o)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(elements, 0, result, 0, size);
        return result;
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= elements.length) {
            return;
        }
        int oldCapacity = elements.length == 0 ? DEFAULT_CAPACITY : elements.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1); // ріст у 1.5 рази
        if (newCapacity < minCapacity) {
            newCapacity = minCapacity;
        }
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        return sb.append(']').toString();
    }
}
