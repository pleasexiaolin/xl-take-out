package com.xiaolin.controller.user;

import com.xiaolin.constant.RedisKeyConstant;
import com.xiaolin.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzh
 * @description: 用户端营业状态控制器
 * @date 2025/11/26 16:15
 */
@RestController
@RequestMapping("/user/shop")
@RequiredArgsConstructor
public class ShopUserController {

    public final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> status() {
        // 读取营业状态
        Integer status = (Integer) redisTemplate.opsForValue().get(RedisKeyConstant.SHOP_KEY);
        return Result.success(status);
    }
}
