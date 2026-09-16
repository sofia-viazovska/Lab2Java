package mylist;

import org.junit.Test;

import java.util.RandomAccess;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MyArrayListTest {

    @Test
    public void implementsRandomAccessMarker() {
        assertTrue(new MyArrayList() instanceof RandomAccess);
    }

    @Test
    public void addAndGet() {
        MyList list = new MyArrayList();
        list.add("a");
        list.add("b");
        list.add("c");

        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    public void addAtIndexShiftsElements() {
        MyList list = new MyArrayList();
        list.add("a");
        list.add("c");
        list.add(1, "b"); // a, c -> a, b, c

        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    public void addAllAtEnd() {
        MyList list = new MyArrayList();
        list.add("a");
        list.addAll(new Object[]{"b", "c", "d"});

        assertArrayEquals(new Object[]{"a", "b", "c", "d"}, list.toArray());
    }

    @Test
    public void addAllAtIndex() {
        MyList list = new MyArrayList();
        list.add("a");
        list.add("d");
        list.addAll(1, new Object[]{"b", "c"}); // a, d -> a, b, c, d

        assertArrayEquals(new Object[]{"a", "b", "c", "d"}, list.toArray());
    }

    @Test
    public void removeShiftsElementsAndReturnsRemoved() {
        MyList list = new MyArrayList();
        list.add("a");
        list.add("b");
        list.add("c");

        Object removed = list.remove(1);

        assertEquals("b", removed);
        assertEquals(2, list.size());
        assertArrayEquals(new Object[]{"a", "c"}, list.toArray());
    }

    @Test
    public void set() {
        MyList list = new MyArrayList();
        list.add("a");
        list.add("b");
        list.set(1, "B");

        assertEquals("B", list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void indexOfFindsFirstMatchOrMinusOne() {
        MyList list = new MyArrayList();
        list.add("a");
        list.add("b");
        list.add("a");

        assertEquals(0, list.indexOf("a"));
        assertEquals(-1, list.indexOf("z"));
    }

    @Test
    public void growsBeyondDefaultCapacity() {
        MyList list = new MyArrayList();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        assertEquals(100, list.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    public void getOutOfBoundsThrows() {
        MyList list = new MyArrayList();
        list.add("a");
        try {
            list.get(5);
            fail("Очікувався IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException expected) {
            // ок
        }
    }
}
