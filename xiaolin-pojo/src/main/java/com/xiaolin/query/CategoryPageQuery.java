package com.xiaolin.query;

import com.xiaolin.pojo.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lzh
 * @description: 分类分页查询
 * @date 2025/11/25 8:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageQuery extends PageQuery {

    //类型 1 菜品分类 2 套餐分类
    private Integer type;

    //分类名称
    private String name;
}
