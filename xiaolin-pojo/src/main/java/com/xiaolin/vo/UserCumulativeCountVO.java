package com.xiaolin.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCumulativeCountVO {
    private LocalDate date;
    private Integer dailyNewUsers;
    private Integer cumulativeUsers;
}
