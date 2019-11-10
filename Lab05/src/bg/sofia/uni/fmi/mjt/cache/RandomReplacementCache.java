package bg.sofia.uni.fmi.mjt.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RandomReplacementCache<K, V> implements Cache<K, V> {
    private static final int DEFAULT_CAPACITY = 10_000;

    private Map<K, V> cache;
    private final long capacity;
    private long size;
    private long successfulHits;
    private long totalHits;

    public RandomReplacementCache() {
        this(DEFAULT_CAPACITY);
    }

    public RandomReplacementCache(long capacity) {
        cache = new HashMap<>();
        this.capacity = capacity;
        size = 0;
        successfulHits = 0;
        totalHits = 0;
    }

    @Override
    public V get(K key) {
        if (cache.get(key) != null) {
            successfulHits++;
        }

        totalHits++;

        return cache.get(key);
    }

    @Override
    public void set(K key, V value) {
        if (key != null && value != null) {
            if (cache.get(key) == null) {
                evictIfFull();
                size++;
            }

            cache.put(key, value);
        }
    }

    @Override
    public boolean remove(K key) {
        if (cache.remove(key) != null) {
            size--;
            return true;
        }

        return false;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public void clear() {
        cache.clear();
        size = 0;
        successfulHits = 0;
        totalHits = 0;
    }

    @Override
    public double getHitRate() {
        if (totalHits == 0) {
            return 0;
        }

        return (double) successfulHits / totalHits;
    }

    @Override
    public int getUsesCount(K key) {
        throw new UnsupportedOperationException();
    }

    public boolean isFull() {
        return size == capacity;
    }

    public void evictIfFull() {
        if (isFull()) {
            Iterator<Entry<K, V>> iterator = cache.entrySet().iterator();
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
                size--;
            }
        }
    }

}