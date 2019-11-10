package bg.sofia.uni.fmi.mjt.cache;

import bg.sofia.uni.fmi.mjt.cache.enums.EvictionPolicy;

public interface CacheFactory {
    /**
     * Constructs a new Cache with the specified maximum capacity and eviction
     * policy.
     * 
     * @throws IllegalArgumentException if the given capacity is less than or equal
     *                                  to zero. Note that IllegalArgumentException
     *                                  is a `RuntimeException` from the JDK
     */
    static <K, V> Cache<K, V> getInstance(long capacity, EvictionPolicy policy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }

        return constructInstance(capacity, policy);
    }

    /**
     * Constructs a new Cache with maximum capacity of 10_000 items and the
     * specified eviction policy.
     */
    static <K, V> Cache<K, V> getInstance(EvictionPolicy policy) {
        final int maxCapacity = 10_000;
        return constructInstance(maxCapacity, policy);
    }

    /**
     * Constructs the new Cache based by capacity and eviction policy.
     * 
     */
    private static <K, V> Cache<K, V> constructInstance(long capacity, EvictionPolicy policy) {
        if (policy == EvictionPolicy.LEAST_FREQUENTLY_USED) {
            return new LeastFrequentlyUsedCache<>(capacity);
        }

        return new RandomReplacementCache<>(capacity);
    }
}
