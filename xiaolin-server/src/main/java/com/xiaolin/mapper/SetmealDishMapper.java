package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.SetmealDishDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @description: 套餐-菜品关联mapper
 * @date 2025/11/25 21:03
 */
@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDishDO> {

    void batchDeleteBySetmealId(@Param("setmealIds") List<Long> setmealIds);

    void deleteBySetmealId(@Param("setmealId") Long setmealId);

    List<SetmealDishDO> selectBatchByDishId(@Param("dishIds") List<Long> dishIds);
}
