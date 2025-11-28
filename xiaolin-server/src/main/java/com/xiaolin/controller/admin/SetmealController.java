package com.xiaolin.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.SetmealDTO;
import com.xiaolin.query.SetmealPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.SetmealService;
import com.xiaolin.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author lzh
 * @description: 套餐管理
 * @date 2025/11/25 20:21
 */
@RestController
@RequestMapping("/admin/setmeal")
@RequiredArgsConstructor
public class SetmealController {

    private final SetmealService setmealService;

    /**
     * 新增
     *
     * @param form
     * @return
     */
    @PostMapping
    public Result<Integer> save(@RequestBody SetmealDTO form){
        if (StringUtils.isAnyEmpty(form.getName())) {
            return Result.error("菜品名称不能为空！");
        }
        return setmealService.save(form);
    }

    /**
     * 分页
     * @param condition
     * @return
     */
    @GetMapping("/page")
    public Result<Page<SetmealVO>> page(SetmealPageQuery condition) {
        return Result.success(setmealService.page(condition, new Page<>(condition.getPage(), condition.getLimit())));
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> info(@PathVariable Long id) {
        if (id == null) {
            return Result.error("id不能为空！");
        }
        return setmealService.info(id);
    }

    /**
     * 启用、禁用
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<Integer> changeStatus(@PathVariable Integer status, Long id) {
        if (status == null || id == null) {
            return Result.error("状态或id不能为空！");
        }
        return setmealService.changeStatus(status, id);
    }

    /**
     * 修改
     *
     * @param form
     * @return
     */
    @PutMapping
    public Result<Integer> update(@RequestBody SetmealDTO form) {
        if (form.getId() == null) {
            return Result.error("状态或id不能为空！");
        }
        return setmealService.update(form);
    }

    /**
     * 批量删除
     *
     * @param ids 逗号分割
     * @return
     */
    @DeleteMapping
    public Result<Integer> batchRemove(String ids) {
        if (ids == null || ids.trim().isEmpty()) {
            return Result.error("请选择要删除的数据");
        }
        return setmealService.batchRemove(ids);
    }
}
