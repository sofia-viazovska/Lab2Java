package mycache;

/**
 * Інтерфейс кешу. На відміну від java.util.Map:
 *  - ключі та значення не можуть бути null (кидається NullPointerException);
 *  - записи можуть "протерміновуватись" (expiry) з часом;
 *  - записи можуть бути "витіснені" (eviction), коли кеш переповнений.
 *
 * @param <K> тип ключа
 * @param <V> тип значення
 */
public interface MyCache<K, V> {

    /** Додає або оновлює запис. Кидає NullPointerException, якщо key або value == null. */
    void put(K key, V value);

    /** Повертає значення за ключем, або null, якщо запису немає чи він протермінований. */
    V get(K key);

    /** Видаляє запис за ключем. */
    void remove(K key);

    /** Перевіряє наявність дійсного (не протермінованого) запису за ключем. */
    boolean containsKey(K key);

    /** Поточна кількість дійсних записів у кеші. */
    int size();

    /** Повністю очищає кеш. */
    void clear();
}
