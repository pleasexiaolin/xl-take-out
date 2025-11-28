package com.xiaolin.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.CategoryDTO;
import com.xiaolin.query.CategoryPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.CategoryService;
import com.xiaolin.vo.CategoryVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lzh
 * @description: 分类控制器
 * @date 2025/11/25 8:38
 */
@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 新增
     *
     * @param form
     * @return
     */
    @PostMapping
    public Result<Integer> save(@RequestBody CategoryDTO form) {
        if (StringUtils.isAnyEmpty(form.getName())) {
            return Result.error("分类名称不能为空！");
        }
        return categoryService.save(form);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<Integer> remove(Long id) {
        if (id == null) {
            return Result.error("删除分类id不能为空");
        }
        return categoryService.remove(id);
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
        if (id == null || status == null) {
            return Result.error("状态或id错误！");
        }
        return categoryService.changeStatus(status, id);
    }

    /**
     * 分页
     * @param condition
     * @return
     */
    @GetMapping("/page")
    public Result<Page<CategoryVO>> page(CategoryPageQuery condition) {
        return Result.success(categoryService.page(condition, new Page<>(condition.getPage(), condition.getLimit())));
    }

    /**
     * 修改
     * @param form
     * @return
     */
    @PutMapping
    public Result<Integer> update(@RequestBody CategoryDTO form){
        if (StringUtils.isAnyEmpty(form.getName())) {
            return Result.error("分类名称不能为空！");
        }
        return categoryService.update(form);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<CategoryVO> info(@PathVariable Long id) {
        if (id == null) {
            return Result.error("id不能为空");
        }
        return categoryService.info(id);
    }

    /**
     * 分类列表
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> list(Integer type){
        if (type == null) {
            return Result.error("类型不能为空");
        }
        return categoryService.listAll(type);
    }

}
