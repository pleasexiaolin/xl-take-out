package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.DishFlavorDO;
import com.xiaolin.vo.DishFlavorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @description: 菜品口味mapper
 * @date 2025/11/25 16:12
 */
@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavorDO> {
    List<DishFlavorVO> getByDishId(@Param("id") Long id);

    void batchRemove(@Param("dishIds")List<Long> dishIds);
}
