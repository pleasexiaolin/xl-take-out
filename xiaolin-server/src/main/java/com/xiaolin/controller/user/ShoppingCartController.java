package com.xiaolin.controller.user;

import com.xiaolin.result.Result;
import com.xiaolin.service.ShoppingCartService;
import com.xiaolin.vo.ShoppingCartVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lzh
 * @description: 购物车控制器
 * @date 2025/11/27 18:00
 */
@RestController
@RequestMapping("/user/shoppingCart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    /**
     * 购物车列表
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCartVO>> list() {
        return shoppingCartService.getShoppingCartList();
    }
}
