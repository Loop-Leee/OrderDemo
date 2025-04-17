package com.lloop.orderdemo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // 集群模式配置，适用于分布式环境
        config.useClusterServers()
            .addNodeAddress(
                    "redis://127.0.0.1:7101",  // 声明主节点在宿主机的ip和端口号
                    "redis://127.0.0.1:7102",
                    "redis://127.0.0.1:7103"
            )
            .setMasterConnectionPoolSize(64)  // 设置连接池
            .setScanInterval(2000);   // 设置节点状态扫描间隔
            // .setPassword("my-redis-password"); // 如果有密码

        return Redisson.create(config);
    }
}
