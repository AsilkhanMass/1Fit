package com.example.onefit.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PostCacheEvictionScheduling {
    @Scheduled(cron = "0 0 12 * * *")
    @CacheEvict(cacheNames = {"posts"}, allEntries = true)
    public void evictExpiredCaches(){
        log.warn("Cache eviction processed.");
    }
}
