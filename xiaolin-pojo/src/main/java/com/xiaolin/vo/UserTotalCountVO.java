package com.xiaolin.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserTotalCountVO {
    private LocalDate date;
    private Integer totalUserCount;
}
