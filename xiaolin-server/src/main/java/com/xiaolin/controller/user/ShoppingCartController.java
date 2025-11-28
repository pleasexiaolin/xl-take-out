package com.xiaolin.controller.user;

import com.xiaolin.dto.ShoppingCartDTO;
import com.xiaolin.result.Result;
import com.xiaolin.service.ShoppingCartService;
import com.xiaolin.vo.ShoppingCartVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 添加购物车
     * @param form
     * @return
     */
    @PostMapping("/add")
    public  Result<Integer> add(@RequestBody ShoppingCartDTO form){
        if (form.getDishId() == null && form.getSetmealId() == null){
            return Result.error("老板至少添加任一菜品或者套餐哦");
        }

        return shoppingCartService.add(form);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result<Integer> clean(){
        return shoppingCartService.clean();
    }

    /**
     * 购物车减一
     * @param form
     * @return
     */
    @PostMapping("/sub")
    public Result<Integer> sub(@RequestBody ShoppingCartDTO form){
        if (form.getDishId() == null && form.getSetmealId() == null){
            return Result.error("老板至减少任一菜品或者套餐哦");
        }
        return shoppingCartService.sub(form);
    }

}
