package com.quantisen.boisson.infrastructure.caffeine.service.impl;

import java.util.Optional;

public interface CacheService<K, V> {
    void put(K key, V value);

    Optional<V> get(K key);

    void invalidate(K key);

    void invalidateAll();
}
