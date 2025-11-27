package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

    Result<List<ShoppingCartVO>> getShoppingCartList();
}
