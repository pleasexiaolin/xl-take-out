package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.CategoryDTO;
import com.xiaolin.entity.CategoryDO;
import com.xiaolin.query.CategoryPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.vo.CategoryVO;

import java.util.List;

/**
 * @author lzh
 * @description: 分类 服务
 * @date 2025/11/25 8:39
 */
public interface CategoryService extends IService<CategoryDO> {
    // 新增
    Result<Integer> save(CategoryDTO form);

    // 删除
    Result<Integer> remove(Long id);

    // 启用、禁用
    Result<Integer> changeStatus(Integer status, Long id);

    // 分页
    Page<CategoryVO> page(CategoryPageQuery condition, Page<CategoryVO> objectPage);

    // 修改
    Result<Integer> update(CategoryDTO form);

    // 详情
    Result<CategoryVO> info(Long id);

    // 分类列表
    Result<List<CategoryVO>> list(Integer type);
}
