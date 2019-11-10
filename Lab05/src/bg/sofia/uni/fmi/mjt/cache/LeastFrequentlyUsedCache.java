package bg.sofia.uni.fmi.mjt.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class LeastFrequentlyUsedCache<K, V> extends RandomReplacementCache<K, V> {
    private Map<K, Integer> keyFrequencyUsage;

    public LeastFrequentlyUsedCache() {
        super();
        keyFrequencyUsage = new HashMap<>();
    }

    public LeastFrequentlyUsedCache(long capacity) {
        super(capacity);
        keyFrequencyUsage = new HashMap<>();
    }

    @Override
    public V get(K key) {
        increaseUsesCount(key);

        return super.get(key);
    }

    @Override
    public void set(K key, V value) {
        increaseUsesCount(key);

        super.set(key, value);
    }

    @Override
    public int getUsesCount(K key) {
        Integer usesCount = keyFrequencyUsage.get(key);

        if (usesCount == null) {
            return 0;
        }

        return usesCount;
    }

    @Override
    public void evictIfFull() {
        if (isFull()) {
            Set<Entry<K, Integer>> pairs = keyFrequencyUsage.entrySet();
            Iterator<Entry<K, Integer>> iterator = pairs.iterator();
            Entry<K, Integer> minUsedPair = iterator.next();

            while (iterator.hasNext()) {
                Entry<K, Integer> currentPair = iterator.next();
                if (currentPair.getValue().intValue() < minUsedPair.getValue().intValue()) {
                    minUsedPair = currentPair;
                }
            }

            super.remove(minUsedPair.getKey());
            pairs.remove(minUsedPair);
        }
    }

    private void increaseUsesCount(K key) {
        if (keyFrequencyUsage.putIfAbsent(key, 1) != null) {
            keyFrequencyUsage.put(key, keyFrequencyUsage.get(key) + 1);
        }
    }
}
