package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xiaolin.constant.StatusConstant;
import com.xiaolin.dto.EmployeeDTO;
import com.xiaolin.pojo.BaseDO;
import lombok.*;
import org.springframework.util.DigestUtils;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("employee")
public class EmployeeDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    public EmployeeDO(EmployeeDTO form) {
        this.username = form.getUsername();
        this.name = form.getName();
        // 密码加密存储 初始为用户名
        this.password = DigestUtils.md5DigestAsHex(form.getUsername().getBytes());
        this.phone = form.getPhone();
        this.sex = form.getSex();
        this.idNumber = form.getIdNumber();
        this.status = StatusConstant.ENABLE;
    }
}
