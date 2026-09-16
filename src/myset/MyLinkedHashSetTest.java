package myset;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyLinkedHashSetTest {

    @Test
    public void addReturnsFalseForDuplicates() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();

        assertTrue(set.add("a"));
        assertFalse(set.add("a")); // дублікат - множина не змінилась
        assertEquals(1, set.size());
    }

    @Test
    public void iterationPreservesInsertionOrder() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();
        set.add("banana");
        set.add("apple");
        set.add("cherry");

        Iterator<String> it = set.iterator();
        assertEquals("banana", it.next());
        assertEquals("apple", it.next());
        assertEquals("cherry", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void removePreservesOrderOfRemaining() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();
        set.add("a");
        set.add("b");
        set.add("c");

        assertTrue(set.remove("b"));
        assertFalse(set.contains("b"));
        assertEquals(2, set.size());

        Iterator<String> it = set.iterator();
        assertEquals("a", it.next());
        assertEquals("c", it.next());
    }

    @Test
    public void containsWorks() {
        MyLinkedHashSet<Integer> set = new MyLinkedHashSet<>();
        set.add(1);
        set.add(2);

        assertTrue(set.contains(1));
        assertFalse(set.contains(99));
    }

    @Test
    public void addNullThrows() {
        MyLinkedHashSet<String> set = new MyLinkedHashSet<>();
        try {
            set.add(null);
            org.junit.Assert.fail("Очікувався NullPointerException");
        } catch (NullPointerException expected) {
            // ок
        }
    }

    @Test
    public void survivesResizeAndKeepsOrder() {
        MyLinkedHashSet<Integer> set = new MyLinkedHashSet<>();
        for (int i = 0; i < 50; i++) {
            set.add(i);
        }
        assertEquals(50, set.size());

        int expected = 0;
        for (int value : set) {
            assertEquals(expected, value);
            expected++;
        }
    }
}
