package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.ShoppingCartDO;
import com.xiaolin.vo.ShoppingCartVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @description: 购物车mapper
 * @date 2025/11/27 18:01
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCartDO> {
    List<ShoppingCartVO> list(@Param("userId") Long userId);

    ShoppingCartVO getShoppingCartDish(@Param("userId") String currentUser, @Param("dishId") Long dishId, @Param("dishFlavor") String dishFlavor);

    ShoppingCartVO getShoppingCartSetmeal(@Param("userId") String currentUser, @Param("setmealId") Long setmealId);

    void clean(String currentUser);
}
