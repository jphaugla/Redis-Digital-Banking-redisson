package com.jphaugla.config;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;

import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.context.annotation.Bean;


import java.io.File;
import java.io.IOException;
import java.time.Duration;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@EnableAsync
@EnableRedisRepositories

@ComponentScan("com.jphaugla")
public class RedisConfig {
    @Autowired
    private @Value("${spring.redis.timeout}")
    Duration redisCommandTimeout;
    private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Autowired
    private @Value("${app.corePoolSize:20}")
    int corePoolSize;
    @Autowired
    private @Value("${app.redissonYamlPath}")
    String redissonYamlPath;

    @Bean
    public RedissonClient redisson() throws IOException {
        Config config = Config.fromYAML(new File(redissonYamlPath));
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplateW1 ( RedissonConnectionFactory redissonConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redissonConnectionFactory);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Long>(Long.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @Primary
    public StringRedisTemplate stringRedisTemplateW1 ( RedissonConnectionFactory redissonConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redissonConnectionFactory);
        return stringRedisTemplate;
    }



    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        on large 64 core machine, drove setCorePoolSize to 200 to really spike performance
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        logger.info("in threadpool bean, corePoolSize is " + String.valueOf(corePoolSize));
        return executor;
    }
}
