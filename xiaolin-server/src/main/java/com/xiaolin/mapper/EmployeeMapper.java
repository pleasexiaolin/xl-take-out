package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaolin.entity.EmployeeDO;
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

}
