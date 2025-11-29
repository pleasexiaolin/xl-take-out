package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.context.BaseContext;
import com.xiaolin.vo.DishVO;
import com.xiaolin.vo.SetmealVO;
import com.xiaolin.vo.ShoppingCartVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("shopping_cart")
public class ShoppingCartDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    //名称
    private String name;

    //用户id
    private Long userId;

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

    private LocalDateTime createTime;

    public ShoppingCartDO(ShoppingCartVO cartVO, DishVO dishVO,String dishFlavor) {
        if (cartVO != null) {
            this.id = cartVO.getId();
            this.number = cartVO.getNumber() + 1;
            this.amount = dishVO.getPrice();
        }else {
            this.name = dishVO.getName();
            this.userId = Long.valueOf(BaseContext.getCurrentUser());
            this.dishId = dishVO.getId();
            this.dishFlavor = dishFlavor;
            this.number = 1;
            this.amount = dishVO.getPrice();
            this.image = dishVO.getImage();
            this.createTime = LocalDateTime.now();
        }
    }

    public ShoppingCartDO(ShoppingCartVO cartVO, SetmealVO setmealVO) {
        if (cartVO != null) {
            this.id = cartVO.getId();
            this.number = cartVO.getNumber() + 1;
            this.amount = setmealVO.getPrice();
        }else {
            this.name = setmealVO.getName();
            this.userId = Long.valueOf(BaseContext.getCurrentUser());
            this.setmealId = setmealVO.getId();
            this.number = 1;
            this.amount = setmealVO.getPrice();
            this.image = setmealVO.getImage();
            this.createTime = LocalDateTime.now();
        }
    }

    public ShoppingCartDO(OrderDetailDO item) {
        this.name = item.getName();
        this.image = item.getImage();
        this.userId = Long.valueOf(BaseContext.getCurrentUser());
        this.dishId = item.getId();
        this.setmealId = item.getSetmealId();
        this.dishFlavor = item.getDishFlavor();
        this.number = item.getNumber();
        this.amount = item.getAmount();
        this.createTime = LocalDateTime.now();
    }
}
