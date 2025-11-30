package com.xiaolin.service;

import com.xiaolin.vo.BusinessDataVO;
import com.xiaolin.vo.DishOverViewVO;
import com.xiaolin.vo.OrderOverViewVO;
import com.xiaolin.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * @author lzh
 * @description: 工作台服务
 * @date 2025/11/30 21:22
 */
public interface WorkspaceService {
    // 营业数据
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    // 订单总览
    OrderOverViewVO getOrderOverView();

    // 菜品总览
    DishOverViewVO getDishOverView();

    // 套餐总览
    SetmealOverViewVO getSetmealOverView();
}
