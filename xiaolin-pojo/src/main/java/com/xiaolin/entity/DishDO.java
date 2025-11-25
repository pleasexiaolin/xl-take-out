package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.dto.DishDTO;
import com.xiaolin.pojo.BaseDO;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 菜品
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("dish")
public class DishDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    //菜品名称
    private String name;

    //菜品分类id
    private Long categoryId;

    //菜品价格
    private BigDecimal price;

    //图片
    private String image;

    //描述信息
    private String description;

    //0 停售 1 起售
    private Integer status;

    public DishDO(DishDTO form) {
        this.name = form.getName();
        this.categoryId = form.getCategoryId();
        this.price = form.getPrice();
        this.image = form.getImage();
        this.description = form.getDescription();
        this.status = StatusConstant.DISABLE;
    }
}
