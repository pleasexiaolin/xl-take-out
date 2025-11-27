package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.entity.ShoppingCartDO;
import com.xiaolin.mapper.ShoppingCartMapper;
import com.xiaolin.result.Result;
import com.xiaolin.service.ShoppingCartService;
import com.xiaolin.vo.ShoppingCartVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzh
 * @description: 购物车服务实现
 * @date 2025/11/27 18:05
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCartDO> implements ShoppingCartService {

    @Override
    public Result<List<ShoppingCartVO>> getShoppingCartList() {
        return Result.success(baseMapper.list());
    }
}
