package com.xiaolin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.MessageConstant;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.OrdersCancelDTO;
import com.xiaolin.dto.OrdersPaymentDTO;
import com.xiaolin.dto.OrdersRejectionDTO;
import com.xiaolin.dto.OrdersSubmitDTO;
import com.xiaolin.entity.OrderDetailDO;
import com.xiaolin.entity.OrdersDO;
import com.xiaolin.entity.ShoppingCartDO;
import com.xiaolin.entity.UserDO;
import com.xiaolin.exception.OrderBusinessException;
import com.xiaolin.mapper.OrderMapper;
import com.xiaolin.mapper.UserMapper;
import com.xiaolin.query.OrdersQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.AddressBookService;
import com.xiaolin.service.OrderDetailService;
import com.xiaolin.service.OrderService;
import com.xiaolin.service.ShoppingCartService;
import com.xiaolin.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
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
    private final UserMapper userMapper;

    @Override
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> payment(OrdersPaymentDTO form) {
        // 获取订单信息
        OrderVO orderVO = baseMapper.getOrderByNumber(form.getOrderNumber());

        // 如果订单已经支付直接返回
        if (orderVO.getPayStatus() == 1 || orderVO.getPayStatus() == 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 获取用户余额
        UserDO userDO = userMapper.selectById(Integer.valueOf(BaseContext.getCurrentUser()));
        if (userDO.getBalance().compareTo(orderVO.getAmount()) < 0) {
            log.info("用户：{} 余额不足，无法完成支付", userDO.getId());
            return Result.error("余额不足，无法完成支付，请充值。");
        }

        // 余额充足 修改订单状态 扣除余额
        // 修改订单状态
        try {
            OrdersDO ordersDO = new OrdersDO(orderVO.getId(), OrdersDO.PAID, OrdersDO.TO_BE_CONFIRMED);
            ordersDO.setCheckoutTime(LocalDateTime.now());
            baseMapper.updateById(ordersDO);
        } catch (Exception e) {
            log.error("修改订单状态失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }
        // 扣除余额
        try {
            userMapper.updateById(new UserDO(userDO.getId(), userDO.getBalance().subtract(orderVO.getAmount())));
            log.info("用户：{} 消费 {} 元", userDO.getId(), orderVO.getAddress());
        } catch (Exception e) {
            log.error("扣除余额失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    @Override
    public Result<OrderVO> orderDetail(Long id) {
        OrderVO orderVO = new OrderVO();

        // info
        OrdersDO ordersDO = baseMapper.selectById(id);
        List<OrderDetailDO> orderDetailList = orderDetailService.getByOrderId(id);

        // do 2 vo
        BeanUtil.copyProperties(ordersDO, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        orderVO.setOrderDishes(orderDetailList.stream().map(OrderDetailDO::getName).toString());

        return Result.success(orderVO);
    }

    @Override
    public Page<OrderVO> adminPage(OrdersQuery condition, Page<OrderVO> page) {
        return getOrderVOPage(condition, page);
    }

    @Override
    public Page<OrderVO> page(OrdersQuery condition, Page<OrderVO> page) {
        condition.setUserId(Long.valueOf(BaseContext.getCurrentUser()));
        return getOrderVOPage(condition, page);
    }

    @Override
    public Result<Integer> confirm(Long id) {
        // 获取订单详情
        OrdersDO ordersDO = baseMapper.selectById(id);
        if (ordersDO.getStatus() != 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        try {
            baseMapper.updateById(new OrdersDO(ordersDO.getId(), null, OrdersDO.CONFIRMED));
        } catch (Exception e) {
            log.error("修改已接单状态失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> rejection(OrdersRejectionDTO form) {
        // 获取订单详情
        OrdersDO ordersDO = baseMapper.selectById(form.getId());
        if (ordersDO.getStatus() != 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        try {
            OrdersDO orderDO = new OrdersDO(ordersDO.getId(), OrdersDO.REFUND, OrdersDO.CANCELLED);
            orderDO.setRejectionReason(form.getRejectionReason());
            baseMapper.updateById(orderDO);
        } catch (Exception e) {
            log.error("修改拒单状态失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        // 金额回退
        UserDO userDO = userMapper.selectById(ordersDO.getUserId());
        try {
            userMapper.updateById(new UserDO(userDO.getId(), userDO.getBalance().add(ordersDO.getAmount())));
        } catch (Exception e) {
            log.error("商户拒单 金额回退失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> cancel(OrdersCancelDTO form) {
        // 获取订单信息
        OrdersDO ordersDO = baseMapper.selectById(form.getId());
        // 校验订单是否存在
        if (ordersDO == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDO.getStatus() > 4) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        try {
            OrdersDO orderDO = new OrdersDO(ordersDO.getId(), OrdersDO.REFUND, OrdersDO.CANCELLED);
            orderDO.setCancelReason(form.getCancelReason());
            orderDO.setCancelTime(LocalDateTime.now());
            baseMapper.updateById(orderDO);
        } catch (Exception e) {
            log.error("商家取消订单状态失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        // 金额回退
        UserDO userDO = userMapper.selectById(ordersDO.getUserId());
        try {
            userMapper.updateById(new UserDO(userDO.getId(), userDO.getBalance().add(ordersDO.getAmount())));
        } catch (Exception e) {
            log.error("商家取消后 金额回退失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    @Override
    public Result<OrderStatisticsVO> statistics() {
        return Result.success(baseMapper.statistics());
    }

    @Override
    public Result<Integer> complete(Long id) {
        // 获取订单详情
        OrdersDO ordersDO = baseMapper.selectById(id);
        if (ordersDO.getStatus() != 4) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        try {
            baseMapper.updateById(new OrdersDO(ordersDO.getId(), null, OrdersDO.COMPLETED));
        } catch (Exception e) {
            log.error("完成订单状态失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    @Override
    public Result<Integer> delivery(Long id) {
        // 获取订单详情
        OrdersDO ordersDO = baseMapper.selectById(id);
        if (ordersDO.getStatus() != 3) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        try {
            baseMapper.updateById(new OrdersDO(ordersDO.getId(), null, OrdersDO.DELIVERY_IN_PROGRESS));
        } catch (Exception e) {
            log.error("派送订单状态失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    @Override
    public Result<Integer> reminder(Long id) {
        return Result.success();
    }

    @Override
    public Result<Integer> repetition(Long id) {
        // 获取订购详情数据
        List<OrderDetailDO> detailDOS = orderDetailService.getByOrderId(id);
        List<ShoppingCartDO> cartDOS = detailDOS.stream().map(ShoppingCartDO::new).collect(Collectors.toList());

        try {
            shoppingCartService.saveBatch(cartDOS);
        } catch (Exception e) {
            log.error("再来一单失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> cancel(Long id) {
        // 获取订单信息
        OrdersDO ordersDO = baseMapper.selectById(id);
        // 校验订单是否存在
        if (ordersDO == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        if (ordersDO.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 修改订单状态
        try {
            OrdersDO orderDO = new OrdersDO(ordersDO.getId(), OrdersDO.REFUND, OrdersDO.CANCELLED);
            orderDO.setCancelReason("不接单 我不要了");
            orderDO.setCancelTime(LocalDateTime.now());
            baseMapper.updateById(orderDO);
        } catch (Exception e) {
            log.error("取消订单状态失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        // 金额回退
        UserDO userDO = userMapper.selectById(ordersDO.getUserId());
        try {
            userMapper.updateById(new UserDO(userDO.getId(), userDO.getBalance().add(ordersDO.getAmount())));
        } catch (Exception e) {
            log.error("金额回退失败 message: {}", e.getMessage());
            return Result.error("系统繁忙，请稍后重试");
        }

        return Result.success();
    }

    private Page<OrderVO> getOrderVOPage(OrdersQuery condition, Page<OrderVO> page) {
        Page<OrderVO> result = baseMapper.page(condition, page);

        result.getRecords().forEach(item -> {
            List<OrderDetailDO> detailDOS = orderDetailService.getByOrderId(item.getId());
            item.setOrderDetailList(detailDOS);

            String orderDishes = detailDOS.stream()
                    .map(it -> it.getName() + "*" + it.getNumber())
                    .collect(Collectors.joining(";"));

            item.setOrderDishes(orderDishes);
        });

        return result;
    }
}
