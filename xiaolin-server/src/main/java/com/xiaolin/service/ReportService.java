package com.xiaolin.service;

import com.xiaolin.result.Result;
import com.xiaolin.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author lzh
 * @description: 统计服务
 * @date 2025/11/30 19:19
 */
public interface ReportService {
    // 订单统计
    Result<OrderReportVO> ordersStatistics(LocalDate begin, LocalDate end);
    // 营业额统计
    Result<TurnoverReportVO> turnoverStatistics(LocalDate begin, LocalDate end);
    // 用户统计
    Result<UserReportVO> userStatistics(LocalDate begin, LocalDate end);
    // 菜品排名
    Result<SalesTop10ReportVO> top10(LocalDate begin, LocalDate end);
    // 导出数据
    void exportBusinessData(HttpServletResponse response);
}
