package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.entity.Employee;
import com.xiaolin.vo.EmployeeLoginVO;

/**
 * 员工服务
 */
public interface EmployeeService extends IService<Employee> {

    // 员工登录
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

}
