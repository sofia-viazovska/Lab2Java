package mylist;

import org.junit.Test;

import java.util.RandomAccess;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class MyLinkedListTest {

    @Test
    public void doesNotImplementRandomAccess() {
        assertFalse(new MyLinkedList() instanceof RandomAccess);
    }

    @Test
    public void addAndGet() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    public void addAtIndex() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("c");
        list.add(1, "b");

        assertArrayEquals(new Object[]{"a", "b", "c"}, list.toArray());
    }

    @Test
    public void addAllAtEnd() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.addAll(new Object[]{"b", "c", "d"});

        assertArrayEquals(new Object[]{"a", "b", "c", "d"}, list.toArray());
    }

    @Test
    public void addAllAtIndex() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("d");
        list.addAll(1, new Object[]{"b", "c"});

        assertArrayEquals(new Object[]{"a", "b", "c", "d"}, list.toArray());
    }

    @Test
    public void removeFromMiddle() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("b");
        list.add("c");

        Object removed = list.remove(1);

        assertEquals("b", removed);
        assertArrayEquals(new Object[]{"a", "c"}, list.toArray());
    }

    @Test
    public void removeHeadAndTail() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("b");
        list.add("c");

        list.remove(0); // b, c
        list.remove(1); // b

        assertEquals(1, list.size());
        assertEquals("b", list.get(0));
    }

    @Test
    public void set() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("b");
        list.set(0, "A");

        assertEquals("A", list.get(0));
    }

    @Test
    public void indexOf() {
        MyList list = new MyLinkedList();
        list.add("a");
        list.add("b");
        list.add("a");

        assertEquals(0, list.indexOf("a"));
        assertEquals(-1, list.indexOf("z"));
    }

    @Test
    public void getOutOfBoundsThrows() {
        MyList list = new MyLinkedList();
        list.add("a");
        try {
            list.get(3);
            fail("Очікувався IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ок
        }
    }
}
