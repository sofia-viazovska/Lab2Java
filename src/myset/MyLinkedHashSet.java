package myset;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Власна реалізація LinkedHashSet: хеш-таблиця (масив "кошиків" з ланцюжками
 * колізій) плюс двозв'язний список, що зберігає порядок вставки елементів.
 * Саме так влаштований java.util.LinkedHashSet/LinkedHashMap всередині,
 * але тут усе написано з нуля, без використання java.util.HashSet/LinkedHashSet.
 *
 * @param <E> тип елементів множини
 */
public class MyLinkedHashSet<E> implements Iterable<E> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    /** Вузол зберігає значення, зв'язок для колізій у "кошику" та зв'язок за порядком вставки. */
    private static final class Node<E> {
        final E value;
        final int hash;
        Node<E> hashNext; // наступний елемент у тому ж кошику (ланцюжок колізій)
        Node<E> before;   // попередній за порядком вставки
        Node<E> after;    // наступний за порядком вставки

        Node(E value, int hash) {
            this.value = value;
            this.hash = hash;
        }
    }

    private Node<E>[] buckets;
    private Node<E> head; // найстаріший вставлений елемент
    private Node<E> tail; // найновіший вставлений елемент
    private int size;

    @SuppressWarnings("unchecked")
    public MyLinkedHashSet() {
        buckets = new Node[DEFAULT_CAPACITY];
    }

    /** Додає елемент, якщо його ще немає. Повертає true, якщо множина змінилась. */
    public boolean add(E value) {
        if (value == null) {
            throw new NullPointerException("MyLinkedHashSet не допускає null-елементів");
        }
        int hash = hash(value);
        int bucketIndex = bucketIndex(hash, buckets.length);

        for (Node<E> n = buckets[bucketIndex]; n != null; n = n.hashNext) {
            if (n.hash == hash && value.equals(n.value)) {
                return false; // такий елемент вже є
            }
        }

        if (size + 1 > buckets.length * LOAD_FACTOR) {
            resize();
            bucketIndex = bucketIndex(hash, buckets.length);
        }

        Node<E> newNode = new Node<>(value, hash);
        newNode.hashNext = buckets[bucketIndex];
        buckets[bucketIndex] = newNode;

        linkLast(newNode);
        size++;
        return true;
    }

    /** Видаляє елемент. Повертає true, якщо він був у множині. */
    public boolean remove(Object value) {
        if (value == null) {
            return false;
        }
        int hash = hash(value);
        int bucketIndex = bucketIndex(hash, buckets.length);

        Node<E> prev = null;
        for (Node<E> n = buckets[bucketIndex]; n != null; n = n.hashNext) {
            if (n.hash == hash && value.equals(n.value)) {
                if (prev == null) {
                    buckets[bucketIndex] = n.hashNext;
                } else {
                    prev.hashNext = n.hashNext;
                }
                unlink(n);
                size--;
                return true;
            }
            prev = n;
        }
        return false;
    }

    public boolean contains(Object value) {
        if (value == null) {
            return false;
        }
        int hash = hash(value);
        int bucketIndex = bucketIndex(hash, buckets.length);
        for (Node<E> n = buckets[bucketIndex]; n != null; n = n.hashNext) {
            if (n.hash == hash && value.equals(n.value)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Ітерація виконується у порядку вставки елементів. */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E value = current.value;
                current = current.after;
                return value;
            }
        };
    }

    private void linkLast(Node<E> node) {
        if (tail == null) {
            head = node;
        } else {
            tail.after = node;
            node.before = tail;
        }
        tail = node;
    }

    private void unlink(Node<E> node) {
        Node<E> before = node.before;
        Node<E> after = node.after;
        if (before == null) {
            head = after;
        } else {
            before.after = after;
        }
        if (after == null) {
            tail = before;
        } else {
            after.before = before;
        }
        node.before = null;
        node.after = null;
    }

    /** Подвоює кількість кошиків і перебудовує ланцюжки колізій, зберігаючи порядок вставки незмінним. */
    @SuppressWarnings("unchecked")
    private void resize() {
        Node<E>[] oldBuckets = buckets;
        buckets = new Node[oldBuckets.length * 2];
        for (Node<E> n = head; n != null; n = n.after) {
            n.hashNext = null;
        }
        for (Node<E> n = head; n != null; n = n.after) {
            int idx = bucketIndex(n.hash, buckets.length);
            n.hashNext = buckets[idx];
            buckets[idx] = n;
        }
    }

    private static int hash(Object value) {
        int h = value.hashCode();
        return h ^ (h >>> 16); // розсіюємо старші біти, як у java.util.HashMap
    }

    private static int bucketIndex(int hash, int capacity) {
        return hash & (capacity - 1); // capacity завжди степінь двійки
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> n = head;
        while (n != null) {
            sb.append(n.value);
            if (n.after != null) {
                sb.append(", ");
            }
            n = n.after;
        }
        return sb.append(']').toString();
    }
}
