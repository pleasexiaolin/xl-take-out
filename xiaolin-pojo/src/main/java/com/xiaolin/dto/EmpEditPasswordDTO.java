package com.xiaolin.dto;

import lombok.Data;

@Data
public class EmpEditPasswordDTO {

    private Long empId;

    private String newPassword;

    private String oldPassword;
}
