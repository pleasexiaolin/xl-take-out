package com.xiaolin.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.OrdersCancelDTO;
import com.xiaolin.dto.OrdersConfirmDTO;
import com.xiaolin.dto.OrdersRejectionDTO;
import com.xiaolin.query.OrdersQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.OrderService;
import com.xiaolin.vo.OrderStatisticsVO;
import com.xiaolin.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author lzh
 * @description: 管理端订单控制器
 * @date 2025/11/30 1:08
 */
@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;


    /**
     * 分页
     *
     * @param condition
     * @return
     */
    @GetMapping("/conditionSearch")
    public Result<Page<OrderVO>> conditionSearch(OrdersQuery condition) {
        return Result.success(orderService.adminPage(condition, new Page<>(condition.getPage(), condition.getLimit())));
    }


    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        if (id == null) {
            return Result.error("查询的订单id为空");
        }
        return orderService.orderDetail(id);
    }

    /**
     * 接单
     *
     * @param form
     * @return
     */
    @PutMapping("/confirm")
    public Result<Integer> confirm(@RequestBody OrdersConfirmDTO form) {
        if (form.getId() == null) {
            return Result.error("查询的订单id为空");
        }
        return orderService.confirm(form.getId());
    }

    /**
     * 拒单
     *
     * @param form
     * @return
     */
    @PutMapping("/rejection")
    public Result<Integer> rejection(@RequestBody OrdersRejectionDTO form) {
        if (form.getId() == null) {
            return Result.error("拒单id为空");
        }
        return orderService.rejection(form);
    }

    /**
     * 取消订单
     *
     * @param form
     * @return
     */
    @PutMapping("/cancel")
    public Result<Integer> cancel(@RequestBody OrdersCancelDTO form) {
        if (form.getId() == null) {
            return Result.error("拒单id为空");
        }
        return orderService.cancel(form);
    }

    /**
     * 订单统计
     *
     * @return
     */
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics() {
        return orderService.statistics();
    }

    /**
     * 完成订单
     *
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    public Result<Integer> complete(@PathVariable Long id) {
        if (id == null) {
            return Result.error("完成订单的id为空");
        }
        return orderService.complete(id);
    }

    /**
     * 派送
     *
     * @param id
     * @return
     */
    @PutMapping("/delivery/{id}")
    public Result<Integer> delivery(@PathVariable Long id) {
        if (id == null) {
            return Result.error("派送订单的id为空");
        }
        return orderService.delivery(id);
    }

}
