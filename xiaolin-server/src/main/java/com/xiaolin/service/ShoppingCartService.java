package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.ShoppingCartDTO;
import com.xiaolin.entity.ShoppingCartDO;
import com.xiaolin.result.Result;
import com.xiaolin.vo.ShoppingCartVO;

import java.util.List;

/**
 * @author lzh
 * @description: 购物车服务
 * @date 2025/11/27 18:04
 */
public interface ShoppingCartService  extends IService<ShoppingCartDO> {
    // 查看
    Result<List<ShoppingCartVO>> getShoppingCartList();
    // 添加
    Result<Integer> add(ShoppingCartDTO form);
    // 清空
    Result<Integer> clean();
    // 删除一个
    Result<Integer> sub(ShoppingCartDTO form);
}
