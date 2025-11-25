package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.entity.DishDO;
import com.xiaolin.entity.DishFlavorDO;
import com.xiaolin.mapper.DishMapper;
import com.xiaolin.query.DishPageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.DishFlavorService;
import com.xiaolin.service.DishService;
import com.xiaolin.vo.DishFlavorVO;
import com.xiaolin.vo.DishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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

    private final DishFlavorService dishFlavorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> save(DishDTO form) {
        // 幂等判断
        DishDO info = baseMapper.getByName(form.getName());
        if (info!=null && info.getName().equals(form.getName())){
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
        List<DishVO> vos = baseMapper.getListByCategoryId(categoryId);

        if (vos == null) {
            return Result.success(null);
        }
        vos.forEach(vo -> {
            List<DishFlavorVO> flavorVOS = dishFlavorService.getByDishId(vo.getId());
            vo.setFlavors(flavorVOS);
        });
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
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> update(DishDTO form) {
        // 幂等判断
        DishDO info = baseMapper.getByName(form.getName());
        if (info != null && !info.getName().equals(form.getName())) {
            return Result.error("改菜品名称已存在，请修改后重试！");
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

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> batchRemove(String ids) {
        try {
            // 解析ids转为List<Long> idsList
            List<Long> dishIds = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            // 删除菜品口味
            dishFlavorService.batchRemove(dishIds);

            // 执行批量删除
            boolean removed = super.removeByIds(dishIds);
            if (removed) {
                return Result.success();
            } else {
                return Result.error("删除失败");
            }

        } catch (NumberFormatException e) {
            log.error("ID格式错误: {}", e.getMessage());
            return Result.error("数据格式错误");
        } catch (Exception e) {
            log.error("批量删除菜品失败: {}", e.getMessage());
            return Result.error("系统异常，请稍后重试");
        }
    }
}
