package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.entity.DishFlavorDO;
import com.xiaolin.vo.DishFlavorVO;

import java.util.List;

/**
 * @author lzh
 * @description: 菜品口味服务
 * @date 2025/11/25 17:25
 */
public interface DishFlavorService extends IService<DishFlavorDO> {
    // 获取菜品全部口味
    List<DishFlavorVO> getByDishId(Long id);
    List<DishFlavorVO> getByDishIds(List<Long> dishIds);

    // 更新菜品口味
    void batchUpdate(DishDTO form);

    // 删除菜品口味
    void batchRemove(List<Long> dishIds);


}
