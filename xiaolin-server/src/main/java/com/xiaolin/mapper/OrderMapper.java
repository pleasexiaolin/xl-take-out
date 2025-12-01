package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.GoodsSalesDTO;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.query.OrdersQuery;
import com.xiaolin.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author lzh
 * @description: 订单mapper
 * @date 2025/11/28 18:17
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrdersDO> {
    OrderVO getOrderByNumber(@Param("orderNumber") String orderNumber);

    Page<OrderVO> page(@Param("condition") OrdersQuery condition, @Param("page") Page<OrderVO> page);

    OrderStatisticsVO statistics();

    List<OrderVO> listByStatus(@Param("status")Integer status);

    Double sumByMap( Map<String, Object> map);

    Integer countByMap(Map<String, Object> map);

    OrderOverViewVO getOrderOverView(@Param("begin")LocalDateTime begin);

    List<GoodsSalesDTO> getSalesTop10(@Param("begin") LocalDateTime beginTime, @Param("end")LocalDateTime endTime);

    BusinessDataVO getBusinessData(@Param("begin")LocalDateTime begin, @Param("end")LocalDateTime end);

    OrderReportVO ordersStatistics(@Param("begin") LocalDate begin, @Param("end")LocalDate end);

    List<OrderDailyCountVO> getOrderDailyStatistics(@Param("begin")LocalDateTime beginTime, @Param("end")LocalDateTime endTime);

    List<DailyTurnoverVO> getDailyTurnover(@Param("begin")LocalDateTime beginTime, @Param("end")LocalDateTime endTime);

}
