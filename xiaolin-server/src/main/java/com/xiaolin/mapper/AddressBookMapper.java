package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.dto.AddressBookDTO;
import com.xiaolin.entity.AddressBookDO;
import com.xiaolin.vo.AddressBookVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzh
 * @description: 地址簿mapper
 * @date 2025/11/28 16:42
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBookDO> {
    List<AddressBookVO> listAll(@Param("userId") Long currentUserId);

    AddressBookVO getDefaultAddress(@Param("userId") Long currentUserId);

    AddressBookVO getAddressById(@Param("id") Long id, @Param("userId") Long currentUserId);

    Integer setDefAddrById(@Param("id") Long id, @Param("userId") Long currentUserId);

    void removeDefAddr(@Param("id") Long id, @Param("userId") Long currentUserId);

    void updateById(@Param("form") AddressBookDTO form);
}
