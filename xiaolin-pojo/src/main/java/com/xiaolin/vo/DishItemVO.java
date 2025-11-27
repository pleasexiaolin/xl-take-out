package com.xiaolin.vo;

import com.xiaolin.entity.DishDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {

    //菜品名称
    private String name;

    //份数
    private Integer copies;

    //菜品图片
    private String image;

    //菜品描述
    private String description;

    public DishItemVO(DishDO dishDO, Integer copies) {
        this.copies = copies;
        this.description = dishDO.getDescription();
        this.image = dishDO.getImage();
        this.name = dishDO.getName();
    }
}
