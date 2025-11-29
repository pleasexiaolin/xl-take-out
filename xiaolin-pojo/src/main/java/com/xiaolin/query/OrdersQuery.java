package com.xiaolin.query;

import com.xiaolin.pojo.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lzh
 * @description: 订单查询
 * @date 2025/11/29 11:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersQuery extends PageQuery {

    //
    private Integer status;

    private Long userId;
}
