package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.JwtClaimsConstant;
import com.xiaolin.dto.UserLoginDTO;
import com.xiaolin.entity.UserDO;
import com.xiaolin.mapper.UserMapper;
import com.xiaolin.properties.JwtProperties;
import com.xiaolin.properties.WeChatProperties;
import com.xiaolin.result.Result;
import com.xiaolin.service.UserService;
import com.xiaolin.utils.JwtUtil;
import com.xiaolin.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lzh
 * @description: 用户服务实现
 * @date 2025/11/26 17:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final JwtProperties jwtProperties;

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    private final WeChatProperties weChatProperties;

    @Override
    public Result<UserLoginVO> login(UserLoginDTO form) {
        // todo 待完善登录
        UserDO userDO = this.wxLogin(form);//后绪步骤实现

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userDO.getId());
        claims.put(JwtClaimsConstant.NAME, userDO.getName());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        return Result.success(new UserLoginVO(userDO, token));
    }

    private UserDO wxLogin(UserLoginDTO form) {

        return null;
    }


}
