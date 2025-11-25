package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.SetmealDTO;
import com.xiaolin.entity.SetmealDO;
import com.xiaolin.query.SetmealPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.vo.SetmealVO;

/**
 * @author lzh
 * @description: 套餐服务
 * @date 2025/11/25 20:25
 */
public interface SetmealService extends IService<SetmealDO> {
    // 新增
    Result<Integer> save(SetmealDTO form);

    // 分页
    Page<SetmealVO> page(SetmealPageQuery condition, Page<SetmealVO> page);

    // 详情
    Result<SetmealVO> info(Long id);

    // 启用、禁用
    Result<Integer> changeStatus(Integer status, Long id);

    // 修改
    Result<Integer> update(SetmealDTO form);

    // 批量删除 也可以单个
    Result<Integer> batchRemove(String ids);
}
