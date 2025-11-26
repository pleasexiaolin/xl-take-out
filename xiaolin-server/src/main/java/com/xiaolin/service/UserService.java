package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.UserLoginDTO;
import com.xiaolin.entity.UserDO;
import com.xiaolin.result.Result;
import com.xiaolin.vo.UserLoginVO;

/**
 * @author lzh
 * @description: 用户服务
 * @date 2025/11/26 17:41
 */
public interface UserService extends IService<UserDO> {
    // 登录
    Result<UserLoginVO> login(UserLoginDTO form);
}
