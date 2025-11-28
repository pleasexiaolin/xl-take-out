package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.AddressBookDTO;
import com.xiaolin.entity.AddressBookDO;
import com.xiaolin.result.Result;
import com.xiaolin.vo.AddressBookVO;

import java.util.List;

/**
 * @author lzh
 * @description: 地址簿service
 * @date 2025/11/28 16:43
 */
public interface AddressBookService extends IService<AddressBookDO> {
    // 新增
    Result<Integer> save(AddressBookDTO form);
    // 全部
    Result<List<AddressBookVO>> listAll();
    // 获取默认地址
    Result<AddressBookVO> getDefaultAddress();
    // 更新
    Result<Integer> update(AddressBookDTO form);
    // 删除
    Result<Integer> remove(Long id);
    // 详情
    Result<AddressBookVO> getAddressById(Long id);
    // 设置默认地址
    Result<Integer> setDefAddrById(Long id);
}
