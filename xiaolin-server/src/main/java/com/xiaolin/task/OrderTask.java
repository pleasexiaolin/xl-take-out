package com.xiaolin.task;

import com.xiaolin.dto.OrdersCancelDTO;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.service.OrderService;
import com.xiaolin.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author lzh
 * @description: 订单定时任务
 * @date 2025/11/30 17:05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTask {

    private final OrderService orderService;

    // 支付超时 30秒检查一下支付状态 下单后超过15分钟仍未支付则判定为支付超时订单
    @Scheduled(cron = "0/30 * * * * ?")
    public void paymentTimeOut(){
        log.info("paymentTimeOut 定时任务开始执行：{}", new Date());
        // 获取待支付的订单集合
        List<OrderVO> orderVOS = orderService.listByStatus(OrdersDO.PENDING_PAYMENT);

        if (CollectionUtils.isEmpty(orderVOS)){
            return;
        }

        // 查询哪些订单的下单时间超过15分钟 超过则取消订单 并设置订单取消原因为用户支付超时
        LocalDateTime timeOut = LocalDateTime.now().minusMinutes(15);

        orderVOS.stream()
                .filter(item -> item.getOrderTime().isBefore(timeOut))
                .forEach(order -> {
                    // 创建取消订单的表单数据
                    OrdersCancelDTO cancelDTO = new OrdersCancelDTO();
                    cancelDTO.setId(order.getId());
                    cancelDTO.setCancelReason("支付超时，订单自动取消");

                    // 调用取消订单服务
                    orderService.cancel(cancelDTO);
                });
    }

    //接单超时 30分数未接单会直接退单
    @Scheduled(cron = "0 */2 * * * ?")
    public void confirmTimeOut(){
        log.info("confirmTimeOut 定时任务开始执行：{}", new Date());

        // 获取待支付的订单集合
        List<OrderVO> orderVOS = orderService.listByStatus(OrdersDO.TO_BE_CONFIRMED);

        if (CollectionUtils.isEmpty(orderVOS)){
            return;
        }

        // 查询哪些订单的结账时间超过30分钟 超过则退单 并设置订单取消原因为商家未接单
        LocalDateTime timeOut = LocalDateTime.now().minusMinutes(30);

        orderVOS.stream()
                .filter(item -> item.getCheckoutTime().isBefore(timeOut))
                .forEach(order -> {
                    // 创建取消订单的表单数据
                    OrdersCancelDTO cancelDTO = new OrdersCancelDTO();
                    cancelDTO.setId(order.getId());
                    cancelDTO.setCancelReason("商家繁忙，订单自动取消");

                    // 调用取消订单服务
                    orderService.cancel(cancelDTO);
                });
    }

    // 派送超时 一个半小时未派送会直接退单
    @Scheduled(cron = "* 0/10 * * * ?")
    public void deliveryTimeOut(){
        log.info("deliveryTimeOut 定时任务开始执行：{}", new Date());

        // 获取待支付的订单集合
        List<OrderVO> orderVOS = orderService.listByStatus(OrdersDO.DELIVERY_IN_PROGRESS);

        if (CollectionUtils.isEmpty(orderVOS)){
            return;
        }

        // 查询哪些订单的结账时间超过30分钟 超过则退单 并设置订单取消原因为商家未接单
        LocalDateTime timeOut = LocalDateTime.now().minusMinutes(90);

        orderVOS.stream()
                .filter(item -> item.getCheckoutTime().isBefore(timeOut))
                .forEach(order -> {
                    // 创建取消订单的表单数据
                    OrdersCancelDTO cancelDTO = new OrdersCancelDTO();
                    cancelDTO.setId(order.getId());
                    cancelDTO.setCancelReason("骑手迷路，订单自动取消");

                    // 调用取消订单服务
                    orderService.cancel(cancelDTO);
                });
    }
}
