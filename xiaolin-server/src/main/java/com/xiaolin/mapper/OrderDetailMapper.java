package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.OrderDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @description: 订单明细mapper
 * @date 2025/11/28 19:23
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetailDO> {
    List<OrderDetailDO> getByOrderId(@Param("id") Long id);
}
