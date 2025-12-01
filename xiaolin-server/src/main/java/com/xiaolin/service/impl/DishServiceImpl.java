package com.xiaolin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.RedisKeyConstant;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.entity.DishDO;
import com.xiaolin.entity.DishFlavorDO;
import com.xiaolin.entity.SetmealDishDO;
import com.xiaolin.mapper.DishMapper;
import com.xiaolin.mapper.SetmealDishMapper;
import com.xiaolin.query.DishPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.DishFlavorService;
import com.xiaolin.service.DishService;
import com.xiaolin.utils.RedisUtil;
import com.xiaolin.utils.StringUtils;
import com.xiaolin.vo.DishFlavorVO;
import com.xiaolin.vo.DishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lzh
 * @description: 菜品服务实现
 * @date 2025/11/25 15:55
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DishServiceImpl extends ServiceImpl<DishMapper, DishDO> implements DishService {

    private final RedisUtil redisUtil;
    private final DishFlavorService dishFlavorService;
    private final SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> save(DishDTO form) {
        // 幂等判断
        DishDO info = baseMapper.getByName(form.getName());
        if (info != null && info.getName().equals(form.getName())) {
            return Result.error("菜品名字重复，换个名字吧。");
        }

        // 先保存菜品信息获取对应id
        DishDO dishDO = new DishDO(form);
        try {
            baseMapper.insert(dishDO);
        } catch (Exception e) {
            log.error("新增菜品失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }

        // 保存口味信息
        if (form.getFlavors() != null) {
            dishFlavorService.saveBatch(form.getFlavors().stream().map(item -> new DishFlavorDO(item, dishDO.getId())).collect(Collectors.toList()));
        }

        // 清理redis缓存
        redisUtil.cleanCache(RedisKeyConstant.DISH_KEY + form.getCategoryId());
        return Result.success();
    }

    @Override
    public Page<DishVO> page(DishPageQuery condition, Page<DishVO> page) {
        Page<DishVO> result = baseMapper.page(condition, page);

        if (result == null || result.getRecords() == null) {
            return result;
        }

        result.getRecords().forEach(dishVO -> {
            List<DishFlavorVO> flavorVOS = dishFlavorService.getByDishId(dishVO.getId());
            dishVO.setFlavors(flavorVOS);
        });

        return result;
    }

    @Override
    public Result<DishVO> info(Long id) {
        DishVO vo = baseMapper.getById(id);
        vo.setFlavors(dishFlavorService.getByDishId(vo.getId()));
        return Result.success(vo);
    }

    @Override
    public Result<List<DishVO>> list(Long categoryId) {
        String key = RedisKeyConstant.DISH_KEY + categoryId;
        // 获取redis数据
        List<DishVO> cachedData = redisUtil.getValueAs(key, List.class);
        if(CollectionUtil.isNotEmpty(cachedData)){
            return Result.success(cachedData);
        }

        List<DishVO> vos = baseMapper.getEnableDishListByCategoryId(categoryId);
        if (vos == null) {
            return Result.success(null);
        }

        // 批量获取所有菜品的ID
        List<Long> dishIds = vos.stream()
                .map(DishVO::getId)
                .collect(Collectors.toList());

        // 批量查询所有菜品的口味信息
        List<DishFlavorVO> allFlavors = dishFlavorService.getByDishIds(dishIds);

        // 按菜品ID分组口味信息
        Map<Long, List<DishFlavorVO>> flavorMap = allFlavors.stream()
                .collect(Collectors.groupingBy(DishFlavorVO::getDishId));

        // 关联口味信息到菜品
        vos.forEach(vo -> {
            vo.setFlavors(flavorMap.getOrDefault(vo.getId(), Collections.emptyList()));
        });

        // 缓存数据
        redisUtil.setValue(key, vos);
        return Result.success(vos);
    }

    @Override
    public Result<Integer> changeStatus(Integer status, Long id) {
        try {
            baseMapper.changeStatus(status, id, BaseContext.getCurrentUser(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("修改菜品状态失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }

        // 清理redis缓存
        redisUtil.deleteKeysByPrefix(RedisKeyConstant.DISH_KEY);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> update(DishDTO form) {
        // 幂等判断
        DishDO info = baseMapper.getByName(form.getName());
        if (info != null && !info.getName().equals(form.getName())) {
            return Result.error("该菜品名称已存在，请修改后重试！");
        }

        // 修改菜品
        try {
            baseMapper.updateById(form, BaseContext.getCurrentUser(), LocalDateTime.now());
        } catch (Exception e) {
            log.error("更新分类失败，message：{}", e.getMessage());
            return Result.error("系统异常，请稍后重试。");
        }

        // 修改菜品口味
        dishFlavorService.batchUpdate(form);

        // 清理redis缓存
        redisUtil.deleteKeysByPrefix(RedisKeyConstant.DISH_KEY);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> batchRemove(String ids) {
        // 解析ids转为List<Long> idsList
        List<Long> dishIds = StringUtils.streamIds(ids);

        // 启售状态不能删
        dishIds = baseMapper.selectBatchIds(dishIds).stream()
                .filter(item -> StatusConstant.DISABLE.equals(item.getStatus()))
                .map(DishDO::getId)
                .collect(Collectors.toList());

        if (CollectionUtil.isEmpty(dishIds)) {
            return Result.error("启售状态下无法删除！");
        }

        // 套餐关联菜品不能删
        List<SetmealDishDO> setmealDishDOList = setmealDishMapper.selectBatchByDishId(dishIds);

        // 获取关联套餐的菜品ID集合
        List<Long> relatedDishIds = setmealDishDOList.stream()
                .map(SetmealDishDO::getDishId)
                .distinct()
                .collect(Collectors.toList());

        // 从可删除列表中移除关联套餐的菜品
        dishIds = dishIds.stream()
                .filter(id -> !relatedDishIds.contains(id))
                .collect(Collectors.toList());

        // 如果没有可删除的菜品，返回错误提示
        if (CollectionUtil.isEmpty(dishIds)) {
            return Result.error("没有可删除的菜品，菜品未禁用或者关联了套餐！");
        }

        // 删除菜品口味
        dishFlavorService.batchRemove(dishIds);

        try {
            // 执行批量删除
            super.removeByIds(dishIds);
        } catch (Exception e) {
            log.error("批量删除菜品失败: {}", e.getMessage());
            return Result.error("系统异常，请稍后重试");
        }

        // 清理redis缓存
        redisUtil.deleteKeysByPrefix(RedisKeyConstant.DISH_KEY);
        return Result.success();
    }
}
