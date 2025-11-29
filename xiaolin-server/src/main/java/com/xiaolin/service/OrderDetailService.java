package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.entity.OrderDetailDO;

import java.util.List;

/**
 * @author lzh
 * @description: 订单明细服务
 * @date 2025/11/28 19:29
 */
public interface OrderDetailService extends IService<OrderDetailDO> {
    // 获取订单明细
    List<OrderDetailDO> getByOrderId(Long id);
}
