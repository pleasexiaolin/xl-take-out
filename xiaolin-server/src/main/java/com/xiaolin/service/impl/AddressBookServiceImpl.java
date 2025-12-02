package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.AddressBookDTO;
import com.xiaolin.entity.AddressBookDO;
import com.xiaolin.mapper.AddressBookMapper;
import com.xiaolin.result.Result;
import com.xiaolin.service.AddressBookService;
import com.xiaolin.vo.AddressBookVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lzh
 * @description: 地址簿实现
 * @date 2025/11/28 16:44
 */
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBookDO> implements AddressBookService {
    @Override
    public Result<Integer> save(AddressBookDTO form) {
        // 新增地址
        try {
            baseMapper.insert(new AddressBookDO(form, getCurrentUserId()));
        } catch (Exception e) {
            log.error("新增地址失败 message：{}", e.getMessage());
            return Result.error("地址出现问题，请稍后重试");
        }

        return Result.success();
    }

    @Override
    public Result<List<AddressBookVO>> listAll() {
        return Result.success(baseMapper.listAll(getCurrentUserId()));
    }

    @Override
    public Result<AddressBookVO> getDefaultAddress() {
        return Result.success(baseMapper.getDefaultAddress(getCurrentUserId()));
    }

    @Override
    public Result<Integer> update(AddressBookDTO form) {
        // 修改地址
        try {
            baseMapper.updateById(form);
        } catch (Exception e) {
            log.error("修改地址失败 message：{}", e.getMessage());
            return Result.error("地址出现问题，请稍后重试");
        }

        return Result.success();
    }

    @Override
    public Result<Integer> remove(Long id) {
        try {
            baseMapper.deleteById(id);
        } catch (Exception e) {
            log.error("删除地址失败 message：{}", e.getMessage());
            return Result.error("地址出现问题，请稍后重试");
        }
        return Result.success();
    }

    @Override
    public Result<AddressBookVO> getAddressById(Long id) {
        return Result.success(baseMapper.getAddressById(id, getCurrentUserId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Integer> setDefAddrById(Long id) {
        // 是否需要移除老的默认地址
        AddressBookVO defaultAddress = this.getDefaultAddress().getData();

        // 默认地址 与 当前地址不同 且只能有一个默认地址
        if (defaultAddress != null && !id.equals(defaultAddress.getId())) {

            // 移除老地址的 默认地址状态
            try {
                baseMapper.removeDefAddr(defaultAddress.getId(), getCurrentUserId());
            } catch (Exception e) {
                log.error("移除默认地址失败 message：{}", e.getMessage());
                return Result.error("地址出现问题，请稍后重试");
            }

        }

        // 判断当前地址是否默认地址
        AddressBookVO currentAddr = baseMapper.getAddressById(id, getCurrentUserId());
        if (currentAddr.getIsDefault() == 1){
            return Result.success();
        }

        // 更新默认地址
        try {
            baseMapper.setDefAddrById(id, getCurrentUserId());
        } catch (Exception e) {
            log.error("设置默认地址失败 message：{}", e.getMessage());
            return Result.error("地址出现问题，请稍后重试");
        }

        return Result.success();
    }

    private Long getCurrentUserId() {
        return Long.valueOf(BaseContext.getCurrentUser());
    }
}
