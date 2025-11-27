package com.xiaolin.utils;

import java.util.Arrays;
import java.util.List;
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
}
