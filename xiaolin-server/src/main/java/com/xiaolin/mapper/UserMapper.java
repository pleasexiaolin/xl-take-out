package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lzh
 * @description: 用户mapper
 * @date 2025/11/26 17:42
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
}
