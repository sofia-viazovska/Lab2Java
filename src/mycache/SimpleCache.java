package mycache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Проста реалізація кешу з обмеженим розміром (LRU-витіснення, eviction)
 * та часом життя записів (TTL, expiry). Саме ці дві риси і роблять кеш
 * відмінним від звичайної Map, разом із забороною null-ключів/значень.
 *
 * Для зберігання використовується java.util.LinkedHashMap в режимі
 * accessOrder=true (це дає LRU-порядок безкоштовно), поверх якого додано
 * власну логіку протермінування записів за часом.
 *
 * @param <K> тип ключа
 * @param <V> тип значення
 */
public class SimpleCache<K, V> implements MyCache<K, V> {

    /** Обгортка над значенням з часом протермінування. */
    private static final class CacheEntry<V> {
        final V value;
        final long expiresAtMillis; // -1, якщо запис не має терміну дії

        CacheEntry(V value, long expiresAtMillis) {
            this.value = value;
            this.expiresAtMillis = expiresAtMillis;
        }

        boolean isExpired() {
            return expiresAtMillis >= 0 && System.currentTimeMillis() >= expiresAtMillis;
        }
    }

    private final int maxSize;
    private final long ttlMillis; // -1 = без обмеження часу життя
    private final LinkedHashMap<K, CacheEntry<V>> storage;

    /**
     * @param maxSize   максимальна кількість записів; при перевищенні найдавніше
     *                  використаний запис витісняється (eviction)
     * @param ttlMillis час життя запису в мілісекундах з моменту put; -1 - без обмеження
     */
    public SimpleCache(int maxSize, long ttlMillis) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Розмір кешу має бути додатним, отримано: " + maxSize);
        }
        this.maxSize = maxSize;
        this.ttlMillis = ttlMillis;
        // accessOrder = true -> вузли переміщуються в кінець при доступі (get), даючи LRU-порядок
        this.storage = new LinkedHashMap<K, CacheEntry<V>>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                return size() > SimpleCache.this.maxSize; // тут відбувається eviction
            }
        };
    }

    public SimpleCache(int maxSize) {
        this(maxSize, -1);
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Ключ кешу не може бути null");
        }
        if (value == null) {
            throw new NullPointerException("Значення кешу не може бути null");
        }
        long expiresAt = ttlMillis < 0 ? -1 : System.currentTimeMillis() + ttlMillis;
        storage.put(key, new CacheEntry<>(value, expiresAt));
    }

    @Override
    public V get(K key) {
        Objects.requireNonNull(key, "Ключ кешу не може бути null");
        CacheEntry<V> entry = storage.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {
            storage.remove(key); // протермінований запис (expiry) видаляється при зверненні
            return null;
        }
        return entry.value;
    }

    @Override
    public void remove(K key) {
        Objects.requireNonNull(key, "Ключ кешу не може бути null");
        storage.remove(key);
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public int size() {
        purgeExpired();
        return storage.size();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    private void purgeExpired() {
        storage.entrySet().removeIf(e -> e.getValue().isExpired());
    }
}
