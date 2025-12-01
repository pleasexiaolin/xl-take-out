package com.xiaolin.service.impl;

import com.xiaolin.mapper.DishMapper;
import com.xiaolin.mapper.OrderMapper;
import com.xiaolin.mapper.SetmealMapper;
import com.xiaolin.mapper.UserMapper;
import com.xiaolin.service.WorkspaceService;
import com.xiaolin.vo.BusinessDataVO;
import com.xiaolin.vo.DishOverViewVO;
import com.xiaolin.vo.OrderOverViewVO;
import com.xiaolin.vo.SetmealOverViewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author lzh
 * @description: 工作台服务
 * @date 2025/11/30 21:22
 */
@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;

    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        BusinessDataVO result = orderMapper.getBusinessData(begin, end);
        if (result.getAllOrders() != 0 && result.getValidOrderCount() != 0) {
            //订单完成率
            result.setOrderCompletionRate(result.getValidOrderCount().doubleValue() / result.getAllOrders());
            //平均客单价
            result.setUnitPrice(result.getTurnover() / result.getValidOrderCount());
        }

        //新增用户数
        Integer newUsers = userMapper.countByMap(begin, end);
        result.setNewUsers(newUsers);
        return result;
    }

    @Override
    public OrderOverViewVO getOrderOverView() {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);
        return orderMapper.getOrderOverView(startOfDay);
    }

    @Override
    public DishOverViewVO getDishOverView() {
        return dishMapper.getDishOverView();
    }

    @Override
    public SetmealOverViewVO getSetmealOverView() {
        return setmealMapper.getSetmealOverView();
    }
}
