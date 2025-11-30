package com.xiaolin.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.OrdersPaymentDTO;
import com.xiaolin.dto.OrdersSubmitDTO;
import com.xiaolin.query.OrdersQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.OrderService;
import com.xiaolin.vo.OrderSubmitVO;
import com.xiaolin.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author lzh
 * @description: 订单控制器
 * @date 2025/11/28 18:17
 */
@RestController
@RequestMapping("/user/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 提交订单
     *
     * @param form
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO form) {
        if (form.getAddressBookId() == null) {
            return Result.error("客官还没选择地址呢");
        }
        return orderService.submit(form);
    }

    /**
     * 订单支付 -- 模拟支付 目前无法接入商户
     *
     * @param form
     * @return
     */
    @PutMapping("/payment")
    public Result<Integer> payment(@RequestBody OrdersPaymentDTO form) {
        if (form.getOrderNumber() == null) {
            return Result.error("没有订单需要支付哦");
        }
        return orderService.payment(form);
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        if (id == null) {
            return Result.error("查询的订单id为空");
        }
        return orderService.orderDetail(id);
    }

    /**
     * 历史订单
     *
     * @param condition
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<Page<OrderVO>> historyOrders(OrdersQuery condition) {
        return Result.success(orderService.page(condition, new Page<>(condition.getPage(), condition.getLimit())));
    }

    /**
     * 催单
     *
     * @param id
     * @return
     */
    @GetMapping("/reminder/{id}")
    public Result<Integer> reminder(@PathVariable Long id) {
        if (id == null) {
            return Result.error("客官要催哪一单呀");
        }
        return orderService.reminder(id);
    }

    /**
     * 再来一单
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    public Result<Integer> repetition(@PathVariable Long id) {
        if (id == null) {
            return Result.error("客官要再买哪一单呀");
        }
        return orderService.repetition(id);
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    public Result<Integer> cancel(@PathVariable Long id) {
        if (id == null) {
            return Result.error("客官要取消哪一单呀");
        }
        return orderService.cancel(id);
    }

}
