package com.xiaolin.vo;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class OrderDailyCountVO implements Serializable {
    private LocalDate date;
    private Integer totalCount;
    private Integer validCount;

    public OrderDailyCountVO() {}

    public OrderDailyCountVO(LocalDate date, Integer totalCount, Integer validCount) {
        this.date = date;
        this.totalCount = totalCount;
        this.validCount = validCount;
    }
}
