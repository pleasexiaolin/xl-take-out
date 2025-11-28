package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.OrdersDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lzh
 * @description: 订单mapper
 * @date 2025/11/28 18:17
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrdersDO> {
}
