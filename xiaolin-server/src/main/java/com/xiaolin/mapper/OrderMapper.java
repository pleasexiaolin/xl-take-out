package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.query.OrdersQuery;
import com.xiaolin.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lzh
 * @description: 订单mapper
 * @date 2025/11/28 18:17
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrdersDO> {
    OrderVO getOrderByNumber(@Param("orderNumber") String orderNumber);

    Page<OrderVO> page(@Param("condition") OrdersQuery condition, @Param("page") Page<OrderVO> page);
}
