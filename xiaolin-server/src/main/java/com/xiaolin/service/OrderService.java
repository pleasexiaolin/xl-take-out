package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.OrdersSubmitDTO;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.result.Result;
import com.xiaolin.vo.OrderSubmitVO;

/**
 * @author lzh
 * @description: 订单服务
 * @date 2025/11/28 18:18
 */
public interface OrderService extends IService<OrdersDO> {
    Result<OrderSubmitVO> submit(OrdersSubmitDTO form);
}
