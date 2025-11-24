package com.xiaolin.vo;

import com.xiaolin.entity.OrderDetailDO;
import com.xiaolin.entity.OrdersDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends OrdersDO implements Serializable {

    //订单菜品信息
    private String orderDishes;

    //订单详情
    private List<OrderDetailDO> orderDetailList;

}
