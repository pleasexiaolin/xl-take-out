package com.xiaolin.query;

import com.xiaolin.pojo.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lzh
 * @description: 菜谱分页参数
 * @date 2025/11/25 15:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishPageQuery extends PageQuery {
    // 名称
    private String name;

    //分类id
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    private Integer status;
}
