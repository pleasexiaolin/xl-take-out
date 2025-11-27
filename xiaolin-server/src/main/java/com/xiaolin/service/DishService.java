package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.entity.DishDO;
import com.xiaolin.query.DishPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.vo.DishVO;

import java.util.List;

/**
 * @author lzh
 * @description: 菜品服务
 * @date 2025/11/25 15:53
 */
public interface DishService extends IService<DishDO> {
    // 新增
    Result<Integer> save(DishDTO form);

    // 分页
    Page<DishVO> page(DishPageQuery condition, Page<DishVO> page);

    // 详情
    Result<DishVO> info(Long id);

    // 菜品分类
    Result<List<DishVO>> list(Long categoryId);

    // 启用、禁用
    Result<Integer> changeStatus(Integer status, Long id);

    // 修改
    Result<Integer> update(DishDTO form);

    // 批量删除 也可以单个
    Result<Integer> batchRemove(String ids);
}
