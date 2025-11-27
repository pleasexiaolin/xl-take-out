package com.xiaolin.controller.user;

import com.xiaolin.result.Result;
import com.xiaolin.service.CategoryService;
import com.xiaolin.service.DishService;
import com.xiaolin.service.SetmealService;
import com.xiaolin.vo.CategoryVO;
import com.xiaolin.vo.DishItemVO;
import com.xiaolin.vo.DishVO;
import com.xiaolin.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lzh
 * @description: 用户菜品控制器
 * @date 2025/11/27 17:12
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class CateSetmealDishController {

    private final CategoryService categoryService;
    private final DishService dishService;
    private final SetmealService setmealService;

    /**
     * 分类列表
     *
     * @param type
     * @return
     */
    @GetMapping("/category/list")
    public Result<List<CategoryVO>> categoryList(Integer type) {
        return categoryService.list(type);
    }

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/dish/list")
    public Result<List<DishVO>> dishList(Long categoryId) {
        if (categoryId == null) {
            return Result.error("分类id不能为空！");
        }
        return dishService.list(categoryId);
    }

    /**
     * 根据分类id查询套餐
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/setmeal/list")
    public Result<List<SetmealVO>> setmealList(Long categoryId) {
        if (categoryId == null) {
            return Result.error("分类id不能为空！");
        }
        return setmealService.list(categoryId);
    }

    /**
     * 根据套餐id查询包含的菜品
     *
     * @param id 套餐id
     * @return
     */
    @GetMapping("/setmeal/dish/{id}")
    public Result<List<DishItemVO>> getSetmealDish(@PathVariable Long id) {
        if (id == null) {
            return Result.error("套餐id不能为空！");
        }
        return setmealService.getDishListBySetmealId(id);
    }
}
