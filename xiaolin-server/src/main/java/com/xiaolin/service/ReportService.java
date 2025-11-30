package com.xiaolin.service;

import com.xiaolin.result.Result;
import com.xiaolin.vo.*;

import java.time.LocalDate;

/**
 * @author lzh
 * @description: 统计服务
 * @date 2025/11/30 19:19
 */
public interface ReportService {
    // 订单统计
    Result<OrderReportVO> ordersStatistics(LocalDate begin, LocalDate end);
    //
    Result<TurnoverReportVO> turnoverStatistics(LocalDate begin, LocalDate end);

    Result<UserReportVO> userStatistics(LocalDate begin, LocalDate end);

    Result<SalesTop10ReportVO> top10(LocalDate begin, LocalDate end);

    Result<String> export();
}
