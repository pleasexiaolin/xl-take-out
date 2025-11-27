package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.dto.DishFlavorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜品口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dish_flavor")
public class DishFlavorDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;
    //菜品id
    private Long dishId;

    //口味名称
    private String name;

    //口味数据list
    private String value;

    public DishFlavorDO(DishFlavorDTO form, Long id) {
        if (form.getId() != null){
            this.id = form.getId();
        }
        this.dishId = id;
        this.name = form.getName();
        this.value = form.getValue();
    }
}
