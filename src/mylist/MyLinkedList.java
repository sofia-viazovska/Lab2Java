package mylist;

import java.util.Objects;

/**
 * Реалізація MyList на основі двозв'язного списку вузлів.
 * Навмисно НЕ реалізує RandomAccess - доступ за індексом тут
 * потребує проходу по посиланнях (O(n)), на відміну від MyArrayList.
 *
 * Java-класи java.util.LinkedList/List не використовуються.
 */
public class MyLinkedList implements MyList {

    private static final class Node {
        Object value;
        Node prev;
        Node next;

        Node(Node prev, Object value, Node next) {
            this.prev = prev;
            this.value = value;
            this.next = next;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    @Override
    public void add(Object e) {
        linkLast(e);
    }

    @Override
    public void add(int index, Object element) {
        checkIndexForAdd(index);
        if (index == size) {
            linkLast(element);
        } else {
            linkBefore(element, node(index));
        }
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
        int insertAt = index;
        for (Object o : c) {
            add(insertAt++, o);
        }
    }

    @Override
    public Object get(int index) {
        checkIndex(index);
        return node(index).value;
    }

    @Override
    public Object remove(int index) {
        checkIndex(index);
        return unlink(node(index));
    }

    @Override
    public void set(int index, Object element) {
        checkIndex(index);
        node(index).value = element;
    }

    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (Node n = head; n != null; n = n.next, index++) {
            if (Objects.equals(n.value, o)) {
                return index;
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
        int i = 0;
        for (Node n = head; n != null; n = n.next) {
            result[i++] = n.value;
        }
        return result;
    }

    private void linkLast(Object e) {
        Node last = tail;
        Node newNode = new Node(last, e, null);
        tail = newNode;
        if (last == null) {
            head = newNode;
        } else {
            last.next = newNode;
        }
        size++;
    }

    private void linkBefore(Object e, Node succ) {
        Node pred = succ.prev;
        Node newNode = new Node(pred, e, succ);
        succ.prev = newNode;
        if (pred == null) {
            head = newNode;
        } else {
            pred.next = newNode;
        }
        size++;
    }

    private Object unlink(Node n) {
        Object value = n.value;
        Node prev = n.prev;
        Node next = n.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            n.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            n.next = null;
        }

        n.value = null;
        size--;
        return value;
    }

    /** Проходить до вузла з заданим індексом, стартуючи з ближчого кінця. */
    private Node node(int index) {
        if (index < (size >> 1)) {
            Node n = head;
            for (int i = 0; i < index; i++) {
                n = n.next;
            }
            return n;
        } else {
            Node n = tail;
            for (int i = size - 1; i > index; i--) {
                n = n.prev;
            }
            return n;
        }
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
        Node n = head;
        while (n != null) {
            sb.append(n.value);
            if (n.next != null) {
                sb.append(", ");
            }
            n = n.next;
        }
        return sb.append(']').toString();
    }
}
