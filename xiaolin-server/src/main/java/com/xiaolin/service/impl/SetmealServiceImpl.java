package com.xiaolin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.SetmealDTO;
import com.xiaolin.dto.SetmealDishDTO;
import com.xiaolin.entity.SetmealDO;
import com.xiaolin.entity.SetmealDishDO;
import com.xiaolin.mapper.SetmealDishMapper;
import com.xiaolin.mapper.SetmealMapper;
import com.xiaolin.query.SetmealPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.SetmealService;
import com.xiaolin.utils.StringUtils;
import com.xiaolin.vo.SetmealVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzh
 * @description: 套餐服务实现
 * @date 2025/11/25 20:26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, SetmealDO> implements SetmealService {

    private final SetmealDishMapper setmealDishMapper;

    @Override
    public Result<Integer> save(SetmealDTO form) {
        // 幂等判断
        SetmealDO info = baseMapper.getByName(form.getName());
        if (info != null && info.getName().equals(form.getName())) {
            return Result.error("已有套餐，换个名字再试一下。");
        }

        // 先保存菜品信息获取对应id
        SetmealDO dishDO = new SetmealDO(form);
        try {
            baseMapper.insert(dishDO);
        } catch (Exception e) {
            log.error("新增套餐失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }

        // 保存套餐菜品关联关系
        if (form.getSetmealDishes() != null) {
            for (SetmealDishDTO dto : form.getSetmealDishes()) {
                setmealDishMapper.insert(new SetmealDishDO(dto, dishDO.getId()));
            }
        }

        return Result.success();
    }

    @Override
    public Page<SetmealVO> page(SetmealPageQuery condition, Page<SetmealVO> page) {
        return baseMapper.page(condition, page);
    }

    @Override
    public Result<SetmealVO> info(Long id) {
        SetmealVO vo = baseMapper.getById(id);
        vo.setSetmealDishes(baseMapper.getSetmealDishById(vo.getId()));
        return Result.success(vo);
    }

    @Override
    public Result<Integer> changeStatus(Integer status, Long id) {
        try {
            baseMapper.changeStatus(status, id, BaseContext.getCurrentUser(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("修改套餐状态失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }
        return Result.success();
    }

    @Override
    public Result<Integer> update(SetmealDTO form) {
        // 幂等判断
        SetmealDO info = baseMapper.getByName(form.getName());
        if (info != null && !info.getName().equals(form.getName())) {
            return Result.error("该套餐名称已存在，请修改后重试！");
        }

        // 修改套餐
        try {
            baseMapper.updateById(form, BaseContext.getCurrentUser(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("更新分类失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }

        // 修改菜品关联关系
        updateSetmealDish(form);

        return Result.success();
    }

    @Override
    public Result<Integer> batchRemove(String ids) {
        // 解析ids转为List<Long> idsList
        List<Long> setmealIds = StringUtils.streamIds(ids);

        // 启售状态不能删
        setmealIds = baseMapper.selectBatchIds(setmealIds).stream()
                .filter(item -> StatusConstant.DISABLE.equals(item.getStatus()))
                .map(SetmealDO::getId)
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(setmealIds)) {
            return Result.error("启售状态下无法删除！");
        }

        // 删除关联关系
        setmealDishMapper.batchDeleteBySetmealId(setmealIds);

        try {
            // 执行批量删除
            super.removeByIds(setmealIds);
        } catch (Exception e) {
            log.error("批量删除菜品失败: {}", e.getMessage());
            return Result.error("系统异常，请稍后重试");
        }

        return Result.success();
    }

    private void updateSetmealDish(SetmealDTO form) {
        if (form.getSetmealDishes() == null) {
            return;
        }

        // 删除库中老数据
        setmealDishMapper.deleteBySetmealId(form.getId());

        // 保存套餐菜品关联关系
        for (SetmealDishDTO dto : form.getSetmealDishes()) {
            setmealDishMapper.insert(new SetmealDishDO(dto, form.getId()));
        }
    }
}
