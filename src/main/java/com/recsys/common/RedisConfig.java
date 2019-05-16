package com.recsys.common;

import com.recsys.model.SubAssociatedCard;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.List;

import static io.lettuce.core.ReadFrom.SLAVE_PREFERRED;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@EnableRedisRepositories
public class RedisConfig {

    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String redisHost) {
        this.host = redisHost;
    }


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration
                .builder()
                .readFrom(SLAVE_PREFERRED)
                .build();

        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(host, 6379);

        return new LettuceConnectionFactory(serverConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, List<SubAssociatedCard>> associatedRedisTemplate() {
        RedisTemplate<String, List<SubAssociatedCard>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }


}
