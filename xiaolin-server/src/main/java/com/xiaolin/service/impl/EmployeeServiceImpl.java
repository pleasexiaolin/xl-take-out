package com.xiaolin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaolin.constant.JwtClaimsConstant;
import com.xiaolin.constant.MessageConstant;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.context.BaseContext;
import com.xiaolin.dto.EmpEditPasswordDTO;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
        List<EmployeeVO> records = employeeVOPage.getRecords();
        // 当前用户是 admin则不脱敏 否则脱敏
        String currentUser = BaseContext.getCurrentUser();
        if (!"admin".equals(currentUser)) {
            for (EmployeeVO record : records) {
                record.setPhone( record.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                record.setIdNumber( record.getIdNumber().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
        }

        return employeeVOPage;
    }


    @Override
    public Result<Integer> updateStatusById(Integer status, Long id) {
        try {
            baseMapper.updateStatusById(status, id, BaseContext.getCurrentUser());
        } catch (Exception e) {
            log.error("更新员工状态失败：{}", e.getMessage());
            return Result.error("更新员工状态失败");
        }

        return Result.success();
    }

    @Override
    public Result<EmployeeVO> info(Long id) {
        return Result.success(baseMapper.getEmpInfo(id));
    }

    @Override
    public Result<Integer> update(EmployeeDTO form) {
        // 幂等校验
        EmployeeDO empInfo = baseMapper.getByUsername(form.getUsername());
        if (empInfo != null && !empInfo.getUsername().equals(form.getUsername()) ) {
            // 已经存在
            return Result.error("用户名已存在");
        }
        EmployeeDO entity = EmployeeDO.builder()
                .id(form.getId())
                .username(form.getUsername())
                .name(form.getName())
                .phone(form.getPhone())
                .sex(form.getSex())
                .idNumber(form.getIdNumber())
                .build();
        entity.setUpdateUser(BaseContext.getCurrentUser());
        entity.setUpdateTime(LocalDateTime.now());
        try {
            super.updateById(entity);
        } catch (Exception e) {
            log.error("更新员工信息失败：{}", e.getMessage());
            return Result.error("更新员工信息失败");
        }

        return Result.success();
    }

    @Override
    public Result<Integer> updatePassword(EmpEditPasswordDTO form) {
        // 幂等校验
        EmployeeVO empInfo = baseMapper.getEmpInfo(form.getEmpId());
        if (empInfo == null) {
            return Result.error("用户不存在");
        }

        try {
            String oldPassword = DigestUtils.md5DigestAsHex(form.getOldPassword().getBytes());
            if (!oldPassword.equals(empInfo.getPassword())) {
                return Result.error("旧密码错误");
            }
            String newPassword = DigestUtils.md5DigestAsHex(form.getNewPassword().getBytes());
            baseMapper.updatePassword(newPassword, form.getEmpId(), BaseContext.getCurrentUser());
        } catch (Exception e) {
            log.error("修改密码失败：{}", e.getMessage());
            return Result.error("修改密码失败");
        }

        return Result.success();
    }

}
