package com.xiaolin.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.dto.EmployeeDTO;
import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.query.EmployeeQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.EmployeeService;
import com.xiaolin.vo.EmployeeLoginVO;
import com.xiaolin.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/employee")
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
        if (StringUtils.isAnyBlank(employeeLoginDTO.getUsername(), employeeLoginDTO.getPassword())) {
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

    /**
     * 新增员工
     *
     * @param form 员工信息
     * @return 添加结果
     */
    @PostMapping
    public Result<Integer> save(@RequestBody EmployeeDTO form) {
        log.info("新增员工：{}", form);
        if (StringUtils.isAnyBlank(form.getUsername(), form.getPhone())) {
            return Result.error("用户名或手机号不能为空");
        }
        return employeeService.save(form);
    }

    /**
     * 分页查询
     * @param condition
     * @return
     */
    @GetMapping("/page")
    public Result<Page<EmployeeVO>> page(EmployeeQuery condition) {
        return Result.success(employeeService.pageEmp(condition, new Page<>(condition.getPage(), condition.getLimit())));
    }
}
