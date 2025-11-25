package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.entity.DishDO;
import com.xiaolin.query.DishPageQuery;
import com.xiaolin.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzh
 * @description: 菜谱mapper
 * @date 2025/11/25 15:54
 */
@Mapper
public interface DishMapper extends BaseMapper<DishDO> {
    Page<DishVO> page(@Param("condition") DishPageQuery condition, @Param("page") Page<DishVO> page);

    DishVO getById(@Param("id") Long id);

    List<DishVO> getListByCategoryId(@Param("categoryId") Long categoryId);

    void changeStatus(@Param("status") Integer status, @Param("id") Long id, @Param("updateUser") String updateUser, @Param("time") LocalDateTime time);

    DishDO getByName(@Param("name") String name);

    void updateById(@Param("form") DishDTO form, @Param("updateUser") String currentUser, @Param("time") LocalDateTime now);
}
