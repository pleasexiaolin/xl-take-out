package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.entity.EmployeeDO;
import com.xiaolin.vo.EmployeeLoginVO;

/**
 * 员工服务
 */
public interface EmployeeService extends IService<EmployeeDO> {

    // 员工登录
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

}
