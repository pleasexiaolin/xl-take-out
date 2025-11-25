package com.xiaolin.dto;

import lombok.Data;

/**
 * @author lzh
 * @description: 菜品口味表单
 * @date 2025/11/25 17:11
 */
@Data
public class DishFlavorDTO {
    private Long id;
    //菜品id
    private Long dishId;

    //口味名称
    private String name;

    //口味数据list
    private String value;
}
