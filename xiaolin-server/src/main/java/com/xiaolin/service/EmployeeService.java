package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.EmployeeDTO;
import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.entity.EmployeeDO;
import com.xiaolin.query.EmployeeQuery;
import com.xiaolin.result.Result;
import com.xiaolin.vo.EmployeeLoginVO;
import com.xiaolin.vo.EmployeeVO;

/**
 * 员工服务
 */
public interface EmployeeService extends IService<EmployeeDO> {

    // 员工登录
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    // 新增员工
    Result<Integer> save(EmployeeDTO form);

    // 分页
    Page<EmployeeVO> pageEmp(EmployeeQuery condition, Page<EmployeeVO> page);
}
