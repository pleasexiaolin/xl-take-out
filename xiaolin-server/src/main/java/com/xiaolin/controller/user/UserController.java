package com.xiaolin.controller.user;

import com.xiaolin.dto.UserLoginDTO;
import com.xiaolin.result.Result;
import com.xiaolin.service.UserService;
import com.xiaolin.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lzh
 * @description: 用户控制器
 * @date 2025/11/26 17:23
 */
@RestController
@RequestMapping("/user/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * C端登录
     *
     * @param form
     * @return
     */
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO form) {
        if (StringUtils.isEmpty(form.getCode())) {
            return Result.error("code 不能为空");
        }

        return userService.login(form);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }
}
