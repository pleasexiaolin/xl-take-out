package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.dto.OrdersSubmitDTO;
import com.xiaolin.entity.OrderDetailDO;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.mapper.OrderMapper;
import com.xiaolin.result.Result;
import com.xiaolin.service.AddressBookService;
import com.xiaolin.service.OrderDetailService;
import com.xiaolin.service.OrderService;
import com.xiaolin.service.ShoppingCartService;
import com.xiaolin.vo.AddressBookVO;
import com.xiaolin.vo.OrderSubmitVO;
import com.xiaolin.vo.ShoppingCartVO;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzh
 * @description: 订单服务实现
 * @date 2025/11/28 18:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrdersDO> implements OrderService {

    private final AddressBookService addrService;
    private final ShoppingCartService shoppingCartService;
    private final OrderDetailService orderDetailService;

    @Override
    @Synchronized
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderSubmitVO> submit(OrdersSubmitDTO form) {
        // 获取购物车数据 新增订单明细
        List<ShoppingCartVO> shoppingCartVOS = shoppingCartService.getShoppingCartList().getData();

        if (CollectionUtils.isEmpty(shoppingCartVOS)) {
            return Result.error("客官购物车是空的哦");
        }

        // 获取地址信息
        AddressBookVO addrVO = addrService.getAddressById(form.getAddressBookId()).getData();

        // 新增订单信息
        OrdersDO ordersDO = new OrdersDO(addrVO, form);
        try {
            baseMapper.insert(ordersDO);
        } catch (Exception e) {
            log.error("提交订单失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        // 新增订单明细
        List<OrderDetailDO> orderDetailDOS = shoppingCartVOS.stream().map(item -> new OrderDetailDO(item, ordersDO.getId())).collect(Collectors.toList());
        try {
            orderDetailService.saveBatch(orderDetailDOS);
        } catch (Exception e) {
            log.error("新增订单明细失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        // 清空购物车
        shoppingCartService.clean();

        return Result.success(new OrderSubmitVO(ordersDO));
    }
}
