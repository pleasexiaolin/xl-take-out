package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.entity.OrderDetailDO;
import com.xiaolin.mapper.OrderDetailMapper;
import com.xiaolin.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author lzh
 * @description:
 * @date 2025/11/28 19:29
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetailDO> implements OrderDetailService {
}
