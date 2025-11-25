package com.xiaolin.vo;

import lombok.Data;

/**
 * @author lzh
 * @description: 菜品口味vo
 * @date 2025/11/25 16:16
 */
@Data
public class DishFlavorVO {

    private Long id;
    //菜品id
    private Long dishId;

    //口味名称
    private String name;

    //口味数据list
    private String value;
}
