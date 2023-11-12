package pl.bartlomiej.emailverifydemo.globalConfig;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> usersCacheConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .removalListener((key, value, cause) -> {
                    if (cause.wasEvicted()) {
                        log.info("Users Cache has been emptied.");
                    }
                });
    }

    @Bean
    public Caffeine<Object, Object> validateVerifyTokenCacheConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .removalListener((key, value, cause) -> {
                    if (cause.wasEvicted()) {
                        log.info("Token Cache has been emptied.");
                    }
                });
    }

    @Bean("usersCacheManager")
    @Primary
    public CacheManager usersCacheManager(@Qualifier("usersCacheConfig") Caffeine<Object, Object> usersCacheConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(usersCacheConfig);
        return cacheManager;
    }

    @Bean("validateVerifyTokenCacheManager")
    public CacheManager validateVerifyTokenCacheManager(@Qualifier("validateVerifyTokenCacheConfig") Caffeine<Object, Object> validateVerifyTokenCacheConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(validateVerifyTokenCacheConfig);
        return cacheManager;
    }
}
