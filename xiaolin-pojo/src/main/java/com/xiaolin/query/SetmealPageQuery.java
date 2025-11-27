package com.xiaolin.query;

import com.xiaolin.pojo.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lzh
 * @description: 套餐分页查询
 * @date 2025/11/25 20:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetmealPageQuery extends PageQuery {
    // 名称
    private String name;

    //分类id
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    private Integer status;
}
