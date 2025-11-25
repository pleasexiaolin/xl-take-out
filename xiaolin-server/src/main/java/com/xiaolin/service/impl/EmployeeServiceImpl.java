package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.JwtClaimsConstant;
import com.xiaolin.constant.MessageConstant;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.dto.EmployeeDTO;
import com.xiaolin.dto.EmployeeLoginDTO;
import com.xiaolin.entity.EmployeeDO;
import com.xiaolin.exception.AccountLockedException;
import com.xiaolin.exception.AccountNotFoundException;
import com.xiaolin.exception.PasswordErrorException;
import com.xiaolin.mapper.EmployeeMapper;
import com.xiaolin.properties.JwtProperties;
import com.xiaolin.query.EmployeePageQuery;
import com.xiaolin.result.Result;
import com.xiaolin.service.EmployeeService;
import com.xiaolin.utils.JwtUtil;
import com.xiaolin.vo.EmployeeLoginVO;
import com.xiaolin.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, EmployeeDO> implements EmployeeService {

    private final JwtProperties jwtProperties;

    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        EmployeeDO employee = baseMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //先加密再比对密码
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (StatusConstant.DISABLE.equals(employee.getStatus())) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());
        claims.put(JwtClaimsConstant.NAME, employee.getName());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        return EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
    }

    @Override
    public Result<Integer> save(EmployeeDTO form) {
        // 幂等校验
        EmployeeDO employee = baseMapper.getByUsername(form.getUsername());
        if (employee != null) {
            // 已经存在
            return Result.error("用户名已存在");
        }

        return Result.success(baseMapper.insert(new EmployeeDO(form)));
    }

    @Override
    public Page<EmployeeVO> pageEmp(EmployeePageQuery condition, Page<EmployeeVO> page) {
        Page<EmployeeVO> employeeVOPage = baseMapper.pageEmp(condition, page);
        System.out.println("total:" + employeeVOPage.getTotal());
        return employeeVOPage;
    }

}
