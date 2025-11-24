package com.xiaolin.service;


import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
