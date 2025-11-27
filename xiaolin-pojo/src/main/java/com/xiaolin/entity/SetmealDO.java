package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.dto.SetmealDTO;
import com.xiaolin.pojo.BaseDO;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 套餐
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("setmeal")
public class SetmealDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    //分类id
    private Long categoryId;

    //套餐名称
    private String name;

    //套餐价格
    private BigDecimal price;

    //状态 0:停用 1:启用
    private Integer status;

    //描述信息
    private String description;

    //图片
    private String image;

    public SetmealDO(SetmealDTO form) {
        this.name = form.getName();
        this.categoryId = form.getCategoryId();
        this.price = form.getPrice();
        this.image = form.getImage();
        this.description = form.getDescription();
        this.status = StatusConstant.DISABLE;
    }
}
