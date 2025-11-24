package com.xiaolin.controller.admin;

import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.result.Result;
import com.xiaolin.service.EmployeeService;
import com.xiaolin.vo.EmployeeLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * 登录
     *
     * @param employeeLoginDTO 用户信息
     * @return 成功 失败
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        if (StringUtils.isAnyBlank(employeeLoginDTO.getUsername(), employeeLoginDTO.getPassword())){
            return Result.error("用户名或密码不能为空");
        }
        return Result.success(employeeService.login(employeeLoginDTO));
    }

    /**
     * 退出
     *
     * @return 成功
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success();
    }

}
