package com.example.demo.Configs;

import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class AppConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }



        @Bean
        public JedisConnectionFactory jedisConnectionFactory() {
            return new JedisConnectionFactory();
        }

        @Bean
        public RedisTemplate<Long, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
            RedisTemplate<Long, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(jedisConnectionFactory);
            return template;
        }
    }

