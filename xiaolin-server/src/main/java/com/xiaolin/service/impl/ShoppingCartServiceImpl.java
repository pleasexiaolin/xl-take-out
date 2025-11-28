package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.ShoppingCartDTO;
import com.xiaolin.entity.ShoppingCartDO;
import com.xiaolin.mapper.ShoppingCartMapper;
import com.xiaolin.result.Result;
import com.xiaolin.service.DishService;
import com.xiaolin.service.SetmealService;
import com.xiaolin.service.ShoppingCartService;
import com.xiaolin.vo.DishVO;
import com.xiaolin.vo.SetmealVO;
import com.xiaolin.vo.ShoppingCartVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lzh
 * @description: 购物车服务实现
 * @date 2025/11/27 18:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCartDO> implements ShoppingCartService {

    private final DishService dishService;
    private final SetmealService setmealService;

    @Override
    public Result<List<ShoppingCartVO>> getShoppingCartList() {
        // 获取当前用户
        String currentUser = BaseContext.getCurrentUser();
        if (StringUtils.isEmpty(currentUser)) {
            return Result.error("登陆者身份不明，请重新登陆！");
        }

        return Result.success(baseMapper.list(Long.valueOf(currentUser)));
    }

    @Override
    public Result<Integer> add(ShoppingCartDTO form) {
        // 获取当前用户
        String currentUser = BaseContext.getCurrentUser();
        if (StringUtils.isEmpty(currentUser)) {
            return Result.error("登陆者身份不明，请重新登陆！");
        }
        // 判断当前用户点的数量 把 shoppingCartVOS 当中的 number 加起来超过10 就提示不让点了
        List<ShoppingCartVO> shoppingCartVOS = this.getShoppingCartList().getData();
        if (shoppingCartVOS != null) {
            int totalNumber = shoppingCartVOS.stream()
                    .mapToInt(ShoppingCartVO::getNumber)
                    .sum();
            if (totalNumber >= 15) {
                return Result.error("少点一些，小哥要不送了。");
            }
        }

        ShoppingCartDO shoppingCartDO = new ShoppingCartDO();

        // 添加菜品到购物车
        if (form.getDishId() != null) {
            DishVO dishVO = dishService.info(form.getDishId()).getData();
            ShoppingCartVO dishCartVO = baseMapper.getShoppingCartDish(currentUser, dishVO.getId(), form.getDishFlavor());
            shoppingCartDO = new ShoppingCartDO(dishCartVO, dishVO, form.getDishFlavor());
        }

        // 添加套餐到购物车
        if (form.getSetmealId() != null) {
            SetmealVO setmealVO = setmealService.info(form.getSetmealId()).getData();
            ShoppingCartVO setmealCartVO = baseMapper.getShoppingCartSetmeal(currentUser, setmealVO.getId());
            shoppingCartDO = new ShoppingCartDO(setmealCartVO, setmealVO);
        }

        // saveOrUpdate
        try {
            super.saveOrUpdate(shoppingCartDO);
        } catch (Exception e) {
            log.error("新增购物车失败 massage:{}", e.getMessage());
            return Result.error("商品售空，请通知老板娘加货。");
        }

        return Result.success();
    }

    @Override
    public Result<Integer> clean() {
        // 获取当前用户
        String currentUser = BaseContext.getCurrentUser();
        if (StringUtils.isEmpty(currentUser)) {
            return Result.error("登陆者身份不明，请重新登陆！");
        }

        try {
            baseMapper.clean(currentUser);
        } catch (Exception e) {
            log.error("清空购物车失败 massage:{}", e.getMessage());
            return Result.error("购物车出了点小差 客官请稍后重试");
        }

        return Result.success();
    }

    @Override
    public Result<Integer> sub(ShoppingCartDTO form) {
        // 获取当前用户
        String currentUser = BaseContext.getCurrentUser();
        if (StringUtils.isEmpty(currentUser)) {
            return Result.error("登陆者身份不明，请重新登陆！");
        }

        ShoppingCartDO shoppingCartDO = new ShoppingCartDO();

        // 减一
        if (form.getDishId() != null) {
            // 先查询购物车的存量
            ShoppingCartVO dishCartVO = baseMapper.getShoppingCartDish(currentUser, form.getDishId(), form.getDishFlavor());
            // 空就没数据了 无法删除
            if (dishCartVO == null) {
                return Result.success();
            }

            // 处理超减
            handOverSub(shoppingCartDO, dishCartVO);
        }
        if (form.getSetmealId() != null) {
            ShoppingCartVO setmealCartVO = baseMapper.getShoppingCartSetmeal(currentUser, form.getSetmealId());

            if (setmealCartVO == null) {
                return Result.success();
            }

            // 处理超减
            handOverSub(shoppingCartDO, setmealCartVO);
        }

        try {
            baseMapper.updateById(shoppingCartDO);
        } catch (Exception e) {
            log.error("购物车减一失败 massage:{}", e.getMessage());
            return Result.error("购物车出了点小差 客官请稍后重试");
        }

        return Result.success();
    }

    private void handOverSub(ShoppingCartDO shoppingCartDO, ShoppingCartVO cartVO) {
        // 有数据 二次判断数量是否为0
        shoppingCartDO.setId(cartVO.getId());
        if (cartVO.getNumber() == 0) {
            // 不能再减一了 删掉这条购物车数据
            baseMapper.deleteById(shoppingCartDO);
        }
        // -1
        shoppingCartDO.setNumber(cartVO.getNumber() - 1);
    }
}
