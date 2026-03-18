package com.myblog.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.master.host}")
    private String masterHost;

    @Value("${spring.redis.master.port}")
    private int masterPort;

    @Value("${spring.redis.slave.host}")
    private String slaveHost;

    @Value("${spring.redis.slave.port}")
    private int slavePort;
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        config.useMasterSlaveServers()
                .setMasterAddress("redis://" + masterHost + ":" + masterPort)
                .addSlaveAddress("redis://" + slaveHost + ":" + slavePort)
                .setReadMode(ReadMode.SLAVE);

        return Redisson.create(config);
    }

    @Bean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        Map<String, CacheConfig> config = new HashMap<>();
        config.put("blogs", new CacheConfig(10 * 60 * 1000, 5 * 60 * 1000));
        return new RedissonSpringCacheManager(redissonClient, config);
    }

}
