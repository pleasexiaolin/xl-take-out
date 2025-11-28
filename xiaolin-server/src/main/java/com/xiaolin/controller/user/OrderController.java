package com.xiaolin.controller.user;

import com.xiaolin.dto.OrdersSubmitDTO;
import com.xiaolin.result.Result;
import com.xiaolin.service.OrderService;
import com.xiaolin.vo.OrderSubmitVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO form){
        if (form.getAddressBookId() == null){
            return Result.error("客官还没选择地址呢");
        }
        return orderService.submit(form);
    }

}
