package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.dto.CategoryDTO;
import com.xiaolin.pojo.BaseDO;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("category")
public class CategoryDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    //类型: 1菜品分类 2套餐分类
    private Integer type;

    //分类名称
    private String name;

    //顺序
    private Integer sort;

    //分类状态 0标识禁用 1表示启用
    private Integer status;

    public CategoryDO(CategoryDTO form) {
        this.type = form.getType();
        this.name = form.getName();
        this.sort = form.getSort();
        this.status = StatusConstant.DISABLE;
    }
}
