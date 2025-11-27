package com.xiaolin.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmpEditPasswordDTO implements Serializable {

    private Long empId;

    private String newPassword;

    private String oldPassword;
}
