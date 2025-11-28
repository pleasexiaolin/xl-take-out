package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.CategoryDTO;
import com.xiaolin.entity.CategoryDO;
import com.xiaolin.query.CategoryPageQuery;
import com.xiaolin.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzh
 * @description: 分类mapper
 * @date 2025/11/25 8:40
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryDO> {
    CategoryDO getByName(@Param("name") String name);

    void changeStatus(@Param("status") Integer status, @Param("id") Long id, @Param("updateUser") String updateUser, @Param("time") LocalDateTime time);

    Page<CategoryVO> page(@Param("condition") CategoryPageQuery condition, @Param("page") Page<CategoryVO> page);

    CategoryVO info(@Param("id") Long id);

    List<CategoryVO> listByType(@Param("type") Integer type);

    void updateById(@Param("entity") CategoryDTO entity, @Param("updateUser") String updateUser,@Param("time") LocalDateTime time);

    List<CategoryVO> listEnable(Integer type);
}
