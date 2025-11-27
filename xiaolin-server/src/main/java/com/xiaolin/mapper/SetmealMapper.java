package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.SetmealDTO;
import com.xiaolin.entity.SetmealDO;
import com.xiaolin.entity.SetmealDishDO;
import com.xiaolin.query.SetmealPageQuery;
import com.xiaolin.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzh
 * @description: 套餐mapper
 * @date 2025/11/25 20:26
 */
@Mapper
public interface SetmealMapper extends BaseMapper<SetmealDO> {
    Page<SetmealVO> page(@Param("condition") SetmealPageQuery condition, @Param("page") Page<SetmealVO> page);

    SetmealVO getById(@Param("id") Long id);

    List<SetmealVO> getListByCategoryId(@Param("categoryId") Long categoryId);

    void changeStatus(@Param("status") Integer status, @Param("id") Long id, @Param("updateUser") String updateUser, @Param("time") LocalDateTime time);

    SetmealDO getByName(@Param("name") String name);

    void updateById(@Param("form") SetmealDTO form, @Param("updateUser") String currentUser, @Param("time") LocalDateTime now);

    List<SetmealDishDO> getSetmealDishById(@Param("setmealId") Long id);

    List<SetmealVO> getEnableListByCategoryId(Long categoryId);
}
