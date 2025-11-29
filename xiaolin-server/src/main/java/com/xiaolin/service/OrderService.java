package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.OrdersPaymentDTO;
import com.xiaolin.dto.OrdersSubmitDTO;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.query.OrdersQuery;
import com.xiaolin.result.Result;
import com.xiaolin.vo.OrderSubmitVO;
import com.xiaolin.vo.OrderVO;

/**
 * @author lzh
 * @description: 订单服务
 * @date 2025/11/28 18:18
 */
public interface OrderService extends IService<OrdersDO> {
    // 提交订单
    Result<OrderSubmitVO> submit(OrdersSubmitDTO form);
    // 支付订单
    Result<Integer> payment(OrdersPaymentDTO form);
    // 详情
    Result<OrderVO> orderDetail(Long id);
    // 历史订单
    Page<OrderVO> historyOrders(OrdersQuery condition, Page<OrderVO> page);
    // 催单
    Result<Integer> reminder(Long id);
    // 再来一单
    Result<Integer> repetition(Long id);
    // 取消
    Result<Integer> cancel(Long id);
}
