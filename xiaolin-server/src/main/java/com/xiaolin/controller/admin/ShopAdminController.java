package com.xiaolin.controller.admin;


import com.xiaolin.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.xiaolin.constant.RedisConstants.SHOP_KEY;

/**
 * @author lzh
 * @description: 管理端营业状态控制器
 * @date 2025/11/26 15:58
 */
@RequestMapping("/admin/shop")
@RestController
@RequiredArgsConstructor
public class ShopAdminController {

    public final RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> status() {
        // 读取营业状态
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_KEY);
        return Result.success(status);
    }

    /**
     * 设置营业状态
     * @return
     */
    @PutMapping("/{status}")
    public Result<Integer> status(@PathVariable Integer status) {
        if (status == null){
            return Result.error("老板设置的营业状态有问题哦");
        }

        // 存入营业状态
        redisTemplate.opsForValue().set(SHOP_KEY,status);
        return Result.success();
    }

}
