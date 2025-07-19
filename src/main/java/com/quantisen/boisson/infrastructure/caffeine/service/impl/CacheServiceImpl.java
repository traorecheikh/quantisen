package com.quantisen.boisson.infrastructure.caffeine.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
@Named("caffeineCacheService")
public class CacheServiceImpl implements CacheService<Object, Object> {

    private final Cache<Object, Object> cache;

    public CacheServiceImpl() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();
    }

    @Override
    public void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Optional<Object> get(Object key) {
        return Optional.ofNullable(cache.getIfPresent(key));
    }

    @Override
    public void invalidate(Object key) {
        cache.invalidate(key);
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }
}
