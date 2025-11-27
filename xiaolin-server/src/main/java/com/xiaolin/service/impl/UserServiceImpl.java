package com.xiaolin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.JwtClaimsConstant;
import com.xiaolin.constant.MessageConstant;
import com.xiaolin.dto.UserLoginDTO;
import com.xiaolin.entity.UserDO;
import com.xiaolin.exception.LoginFailedException;
import com.xiaolin.mapper.UserMapper;
import com.xiaolin.properties.JwtProperties;
import com.xiaolin.properties.WeChatProperties;
import com.xiaolin.result.Result;
import com.xiaolin.service.UserService;
import com.xiaolin.utils.HttpClientUtil;
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

    private final WeChatProperties weChatProperties;

    @Override
    public Result<UserLoginVO> login(UserLoginDTO form) {
        // 微信登录
        UserDO userDO = this.wxLogin(form);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, userDO.getId());
        claims.put(JwtClaimsConstant.NAME, userDO.getName());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        return Result.success(new UserLoginVO(userDO, token));
    }

    private UserDO wxLogin(UserLoginDTO form) {
        String openid = getOpenid(form.getCode());
        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        UserDO user = baseMapper.getByOpenid(openid);
        if (user == null) {
            user = new UserDO(openid);
            baseMapper.insert(user);
        }

        return user;
    }

    //调用微信接口服务，获得当前微信用户的openid
    private String getOpenid(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(weChatProperties.getLoginUrl(), map);

        return JSON.parseObject(json).getString("openid");
    }
}
