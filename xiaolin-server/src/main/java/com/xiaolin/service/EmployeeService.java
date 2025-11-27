package com.xiaolin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaolin.dto.EmpEditPasswordDTO;
import com.xiaolin.dto.EmployeeDTO;
import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.entity.EmployeeDO;
import com.xiaolin.query.EmployeePageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.vo.EmployeeLoginVO;
import com.xiaolin.vo.EmployeeVO;

/**
 * 员工服务
 */
public interface EmployeeService extends IService<EmployeeDO> {
    // 员工登录
    Result<EmployeeLoginVO> login(EmployeeLoginDTO employeeLoginDTO);

    // 新增员工
    Result<Integer> save(EmployeeDTO form);

    // 分页
    Page<EmployeeVO> pageEmp(EmployeePageQuery condition, Page<EmployeeVO> page);

    // 启用、禁用
    Result<Integer> updateStatusById(Integer status, Long id);

    // 员工信息
    Result<EmployeeVO> info(Long id);

    // 修改员工信息
    Result<Integer> update(EmployeeDTO form);

    // 修改密码
    Result<Integer> updatePassword(EmpEditPasswordDTO form);
}
