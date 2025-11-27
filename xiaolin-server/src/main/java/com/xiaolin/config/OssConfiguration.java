package com.xiaolin.config;

import com.xiaolin.properties.AliOssProperties;
import com.xiaolin.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * @author lzh
 * @description: 配置类，用于创建AliOssUtil对象
 * @date 2025/11/25 19:13
 */
@Configuration
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象...");
        String accessKeyId = System.getenv("OSS_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("OSS_ACCESS_KEY_SECRET");
        AliOssUtil aliOssUtil = new AliOssUtil(aliOssProperties.getEndpoint(),
                accessKeyId,
                accessKeySecret,
                aliOssProperties.getBucketName());
        log.info("完成创建阿里云文件上传工具类对象...");
        return aliOssUtil;
    }
}
