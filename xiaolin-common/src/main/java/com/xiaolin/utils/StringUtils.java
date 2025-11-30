package com.xiaolin.utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author lzh
 * @description: 字符串工具
 * @date 2025/11/25 20:50
 */
public class StringUtils {
    public static List<Long> streamIds(String ids) {
        return Arrays.stream(ids.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * 生成订单号，格式为：年月日-四位随机字母数字
     * @return 订单号字符串
     */
    public static String generateOrderNumber() {
        LocalDateTime now = LocalDateTime.now();
        String datePart = now.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 5);
        return datePart + "-" + randomPart;
    }

}
