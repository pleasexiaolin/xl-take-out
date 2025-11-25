package com.xiaolin.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.query.DishPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.DishService;
import com.xiaolin.vo.DishVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lzh
 * @description: 菜品控制器
 * @date 2025/11/25 15:52
 */
@RestController
@RequestMapping("/admin/dish")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping
    public Result<Integer> save(@RequestBody DishDTO form){
        if (StringUtils.isAnyEmpty(form.getName())) {
            return Result.error("菜品名称不能为空！");
        }
        return dishService.save(form);
    }

    /**
     * 分页
     *
     * @param condition
     * @return
     */
    @GetMapping("/page")
    public Result<Page<DishVO>> page(DishPageQuery condition) {
        return Result.success(dishService.page(condition, new Page<>(condition.getPage(), condition.getLimit())));
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> info(@PathVariable Long id) {
        if (id == null) {
            return Result.error("id不能为空！");
        }
        return dishService.info(id);
    }

    /**
     * 菜品分类
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId) {
        if (categoryId == null) {
            return Result.error("分类id不能为空！");
        }
        return dishService.list(categoryId);
    }


    /**
     * 启用、禁用
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<Integer> changeStatus(@PathVariable Integer status, Long id) {
        if (status == null || id == null) {
            return Result.error("状态或id不能为空！");
        }
        return dishService.changeStatus(status, id);
    }

    /**
     * 修改
     * @param form
     * @return
     */
    @PutMapping
    public Result<Integer> update(@RequestBody DishDTO form){
        if (form.getId() == null) {
            return Result.error("状态或id不能为空！");
        }
        return dishService.update(form);
    }

    /**
     * 批量删除
     * @param ids 逗号分割
     * @return
     */
    @DeleteMapping
    public Result<Integer> batchRemove(String ids){
        if (ids == null || ids.trim().isEmpty()) {
            return Result.error("请选择要删除的数据");
        }
        return dishService.batchRemove(ids);
    }

}
