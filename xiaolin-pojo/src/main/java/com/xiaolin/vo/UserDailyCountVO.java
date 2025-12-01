package com.xiaolin.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserDailyCountVO implements Serializable {
    private LocalDate date;
    private Integer newUserCount;

    public UserDailyCountVO() {}

    public UserDailyCountVO(LocalDate date, Integer newUserCount) {
        this.date = date;
        this.newUserCount = newUserCount;
    }
}
