package com.jphaugla.config;

import org.redisson.api.RedissonClient;

import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import org.springframework.data.redis.connection.RedisConnectionFactory;


import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.core.env.Environment;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.context.annotation.Bean;


import java.time.Duration;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@EnableAsync
@EnableRedisRepositories

@ComponentScan("com.jphaugla")
public class RedisConfig {
    @Autowired
    private Environment env;
    private @Value("${spring.redis.timeout}")
    Duration redisCommandTimeout;

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplateW1 ( RedissonConnectionFactory redissonConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redissonConnectionFactory);
        // redisTemplate.setHashKeySerializer(new StringRedisSerializer());
       // redisTemplate.setKeySerializer(new StringRedisSerializer());
        // redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Long>(Long.class));
        // redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
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
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(1000);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}
