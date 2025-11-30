package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.OrdersCancelDTO;
import com.xiaolin.dto.OrdersPaymentDTO;
import com.xiaolin.dto.OrdersRejectionDTO;
import com.xiaolin.dto.OrdersSubmitDTO;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.query.OrdersQuery;
import com.xiaolin.result.Result;
import com.xiaolin.vo.OrderStatisticsVO;
import com.xiaolin.vo.OrderSubmitVO;
import com.xiaolin.vo.OrderVO;

import java.util.List;

/**
 * @author lzh
 * @description: 订单服务
 * @date 2025/11/28 18:18
 */
public interface OrderService extends IService<OrdersDO> {
    //==========================用户功能=============================//
    // 提交订单
    Result<OrderSubmitVO> submit(OrdersSubmitDTO form);

    // 支付订单
    Result<Integer> payment(OrdersPaymentDTO form);

    // 催单
    Result<Integer> reminder(Long id);

    // 再来一单
    Result<Integer> repetition(Long id);

    // 取消
    Result<Integer> cancel(Long id);
    // 分页
    Page<OrderVO> page(OrdersQuery condition, Page<OrderVO> page);

    //==========================通用功能=============================//
    // 详情
    Result<OrderVO> orderDetail(Long id);
    // 查询某状态下的全部订单
    List<OrderVO> listByStatus(Integer status);

    //==========================管理员功能=============================//
    //分页
    Page<OrderVO> adminPage(OrdersQuery condition, Page<OrderVO> objectPage);

    // 接单
    Result<Integer> confirm(Long id);

    // 拒单
    Result<Integer> rejection(OrdersRejectionDTO form);

    // 管理员取消
    Result<Integer> cancel(OrdersCancelDTO form);

    // 统计订单
    Result<OrderStatisticsVO> statistics();

    // 完成订单
    Result<Integer> complete(Long id);

    // 派送
    Result<Integer> delivery(Long id);

}
