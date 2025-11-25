package com.xiaolin.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐菜品关系
 */
@Data
public class SetmealDishDTO implements Serializable {
    private Long id;

    //套餐id
    private Long setmealId;

    //菜品id
    private Long dishId;

    //菜品名称 （冗余字段）
    private String name;

    //菜品原价
    private BigDecimal price;

    //份数
    private Integer copies;
}
