package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.entity.EmployeeDO;
import com.xiaolin.query.EmployeeQuery;
import com.xiaolin.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeeDO> {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    EmployeeDO getByUsername(@Param("username") String username);

    /**
     * 分页查询员工
     * @param condition
     * @param page
     * @return
     */
    Page<EmployeeVO> pageEmp(@Param("condition")EmployeeQuery condition, @Param("page")Page<EmployeeVO> page);
}
