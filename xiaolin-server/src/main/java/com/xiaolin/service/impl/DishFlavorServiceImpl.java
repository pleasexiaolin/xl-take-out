package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.dto.DishFlavorDTO;
import com.xiaolin.entity.DishFlavorDO;
import com.xiaolin.mapper.DishFlavorMapper;
import com.xiaolin.service.DishFlavorService;
import com.xiaolin.vo.DishFlavorVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lzh
 * @description: 菜品口味服务实现
 * @date 2025/11/25 17:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavorDO> implements DishFlavorService {

    @Override
    public List<DishFlavorVO> getByDishId(Long id) {
        if (id == null) {
            return null;
        }
        return baseMapper.getByDishId(id);
    }

    @Override
    public void batchUpdate(DishDTO form) {
        // 获取库中菜品的全部口味  和 表单口味
        List<DishFlavorVO> dataBaseFlavors = baseMapper.getByDishId(form.getId());
        List<DishFlavorDTO> formFlavors = form.getFlavors();

        // 1.若表单口味为空 库中口味为空 直接返回
        if (formFlavors == null && dataBaseFlavors == null) {
            return;
        }

        // 2.若表单口味为空 库中非空 则删除库中口味数据
        if (formFlavors == null) {
            super.removeByIds(dataBaseFlavors.stream().map(DishFlavorVO::getId).collect(Collectors.toList()));
            return;
        }

        // 3.若表单口味非空 库中为空 则新增表单口味
        if (dataBaseFlavors == null) {
            super.saveBatch(formFlavors.stream().map(vo -> new DishFlavorDO(vo, form.getId())).collect(Collectors.toList()));
            return;
        }

        // 4 若表单口味非空 库中口味非空。表单是变更后的数据，库中是现有数据。相同id则更新，新增的id则插入，缺少的id则删除
        // 提取ID集合用于比较
        Set<Long> formIds = formFlavors.stream()
                .map(DishFlavorDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<Long> databaseIds = dataBaseFlavors.stream()
                .map(DishFlavorVO::getId)
                .collect(Collectors.toSet());

        // 找出需要删除的记录（存在于数据库但不存在于表单）
        List<Long> deleteIds = dataBaseFlavors.stream()
                .map(DishFlavorVO::getId)
                .filter(id -> !formIds.contains(id))
                .collect(Collectors.toList());
        if (!deleteIds.isEmpty()) {
            super.removeByIds(deleteIds);
        }

        // 找出需要新增的记录（存在于表单但不存在于数据库）
        List<DishFlavorDO> insertList = formFlavors.stream()
                .filter(flavor -> flavor.getId() == null)
                .map(vo -> new DishFlavorDO(vo, form.getId()))
                .collect(Collectors.toList());
        if (!insertList.isEmpty()) {
            super.saveBatch(insertList);
        }

        // 找出需要更新的记录（存在于两者且ID相同）
        List<DishFlavorDO> updateList = formFlavors.stream()
                .filter(flavor -> flavor.getId() != null && databaseIds.contains(flavor.getId()))
                .map(vo -> new DishFlavorDO(vo, form.getId()))
                .collect(Collectors.toList());
        if (!updateList.isEmpty()) {
            super.updateBatchById(updateList);
        }
    }

    @Override
    public void batchRemove(List<Long> dishIds) {
        try {
            baseMapper.batchRemove(dishIds);
        } catch (Exception e) {
            log.error("批量删除菜品口味失败：{}", e.getMessage());
            throw new RuntimeException("系统异常，请稍后重试");
        }
    }
}
