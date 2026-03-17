package com.myblog.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
