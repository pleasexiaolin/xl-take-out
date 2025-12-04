package com.xiaolin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "xl.redis")
@Data
public class RedisProperties {
    private String host;
    private String port;
    private String password;
    private String database;
}
