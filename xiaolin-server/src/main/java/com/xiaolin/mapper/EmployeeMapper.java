package com.xiaolin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaolin.entity.EmployeeDO;
import com.xiaolin.query.EmployeePageQuery;
import com.xiaolin.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeeDO> {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    EmployeeDO getByUsername(@Param("username") String username);

    /**
     * 分页查询员工
     *
     * @param condition
     * @param page
     * @return
     */
    Page<EmployeeVO> pageEmp(@Param("condition") EmployeePageQuery condition, @Param("page") Page<EmployeeVO> page);

    /**
     * 修改员工状态
     *
     * @param status
     * @param id
     * @param updateUser
     * @return
     */
    Integer updateStatusById(@Param("status")Integer status, @Param("id")Long id, @Param("updateUser")String updateUser);

    /**
     * 获取员工信息
     *
     * @param id
     * @return
     */
    EmployeeVO getEmpInfo(Long id);

    /**
     * 修改密码
     *
     * @param newPassword
     * @param id
     * @param updateUser
     */
    void updatePassword(@Param("newPassword")String newPassword, @Param("id")Long id,@Param("updateUser") String updateUser);
}
