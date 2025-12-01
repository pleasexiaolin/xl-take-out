package com.xiaolin.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class DailyTurnoverVO implements Serializable {
    private LocalDate date;
    private Double turnover;

    public DailyTurnoverVO() {}
}
