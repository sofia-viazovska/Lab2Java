package mycache;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SimpleCacheTest {

    @Test
    public void putAndGet() {
        MyCache<String, Integer> cache = new SimpleCache<>(10);
        cache.put("one", 1);

        assertEquals(Integer.valueOf(1), cache.get("one"));
        assertTrue(cache.containsKey("one"));
        assertEquals(1, cache.size());
    }

    @Test
    public void nullKeyThrows() {
        MyCache<String, Integer> cache = new SimpleCache<>(10);
        try {
            cache.put(null, 1);
            fail("Очікувався NullPointerException для null-ключа");
        } catch (NullPointerException expected) {
            // ок - на відміну від Map, кеш не допускає null-ключів
        }
    }

    @Test
    public void nullValueThrows() {
        MyCache<String, Integer> cache = new SimpleCache<>(10);
        try {
            cache.put("key", null);
            fail("Очікувався NullPointerException для null-значення");
        } catch (NullPointerException expected) {
            // ок - на відміну від Map, кеш не допускає null-значень
        }
    }

    @Test
    public void missingKeyReturnsNull() {
        MyCache<String, Integer> cache = new SimpleCache<>(10);
        assertNull(cache.get("немає такого"));
        assertFalse(cache.containsKey("немає такого"));
    }

    @Test
    public void evictsLeastRecentlyUsedWhenOverCapacity() {
        // Кеш на 2 записи: третій put() має витіснити (eviction) найдавніше використаний
        MyCache<String, String> cache = new SimpleCache<>(2);

        cache.put("a", "A");
        cache.put("b", "B");
        cache.put("c", "C"); // мало витіснити "a" (найдавніший, ще не читаний)

        assertEquals(2, cache.size());
        assertNull("запис 'a' мав бути витіснений", cache.get("a"));
        assertEquals("B", cache.get("b"));
        assertEquals("C", cache.get("c"));
    }

    @Test
    public void accessingEntryProtectsItFromEviction() {
        MyCache<String, String> cache = new SimpleCache<>(2);

        cache.put("a", "A");
        cache.put("b", "B");
        cache.get("a");       // "a" стає нещодавно використаним
        cache.put("c", "C");  // тепер мало витіснити "b", а не "a"

        assertEquals("A", cache.get("a"));
        assertNull("запис 'b' мав бути витіснений, бо був найдавніше використаним", cache.get("b"));
        assertEquals("C", cache.get("c"));
    }

    @Test
    public void entryExpiresAfterTtl() throws InterruptedException {
        MyCache<String, String> cache = new SimpleCache<>(10, 50); // TTL = 50 мс

        cache.put("key", "value");
        assertEquals("value", cache.get("key")); // ще дійсний

        Thread.sleep(150);

        assertNull("запис мав протермінуватися (expiry)", cache.get("key"));
        assertEquals(0, cache.size());
    }

    @Test
    public void withoutTtlEntryNeverExpires() throws InterruptedException {
        MyCache<String, String> cache = new SimpleCache<>(10); // без TTL

        cache.put("key", "value");
        Thread.sleep(50);

        assertEquals("value", cache.get("key"));
    }

    @Test
    public void removeAndClear() {
        MyCache<String, String> cache = new SimpleCache<>(10);
        cache.put("a", "A");
        cache.put("b", "B");

        cache.remove("a");
        assertFalse(cache.containsKey("a"));
        assertEquals(1, cache.size());

        cache.clear();
        assertEquals(0, cache.size());
    }
}
