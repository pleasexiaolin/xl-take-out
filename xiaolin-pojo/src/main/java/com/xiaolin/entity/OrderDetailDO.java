package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.vo.ShoppingCartVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("order_detail")
public class OrderDetailDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    //名称
    private String name;

    //订单id
    private Long orderId;

    //菜品id
    private Long dishId;

    //套餐id
    private Long setmealId;

    //口味
    private String dishFlavor;

    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;

    public OrderDetailDO(ShoppingCartVO item, Long orderId) {
        this.name = item.getName();
        this.orderId = orderId;
        this.dishId = item.getDishId();
        this.setmealId = item.getSetmealId();
        this.dishFlavor = item.getDishFlavor();
        this.number = item.getNumber();
        this.amount = item.getAmount();
        this.image = item.getImage();
    }
}
