package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.ShoppingCartDO;
import com.xiaolin.vo.ShoppingCartVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lzh
 * @description: 购物车mapper
 * @date 2025/11/27 18:01
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCartDO> {
    List<ShoppingCartVO> list();
}
