package com.xiaolin.service.impl;

import com.xiaolin.dto.GoodsSalesDTO;
import com.xiaolin.mapper.OrderMapper;
import com.xiaolin.mapper.UserMapper;
import com.xiaolin.result.Result;
import com.xiaolin.service.ReportService;
import com.xiaolin.service.WorkspaceService;
import com.xiaolin.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lzh
 * @description: 统计服务实现
 * @date 2025/11/30 19:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final WorkspaceService workspaceService;


    @Override
    public Result<OrderReportVO> ordersStatistics(LocalDate begin, LocalDate end) {
        OrderReportVO result = orderMapper.ordersStatistics(begin, end);
        if (result.getTotalOrderCount() != 0) {
            result.setOrderCompletionRate(result.getValidOrderCount().doubleValue() / result.getTotalOrderCount());
        }

        // 优化后的批量查询部分
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 一次性获取整个时间段的数据
        List<OrderDailyCountVO> dailyCounts = orderMapper.getOrderDailyStatistics(beginTime, endTime);

        // 构建日期列表和对应数据
        List<LocalDate> dateList = new ArrayList<>();
        Map<LocalDate, OrderDailyCountVO> countMap = new HashMap<>();

        for (OrderDailyCountVO count : dailyCounts) {
            LocalDate date = count.getDate();
            dateList.add(date);
            countMap.put(date, count);
        }

        // 填充缺失日期的数据
        LocalDate current = begin;
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        while (!current.isAfter(end)) {
            OrderDailyCountVO count = countMap.getOrDefault(current, new OrderDailyCountVO(current, 0, 0));
            orderCountList.add(count.getTotalCount());
            validOrderCountList.add(count.getValidCount());
            if (!dateList.contains(current)) {
                dateList.add(current);
            }
            current = current.plusDays(1);
        }

        result.setDateList(StringUtils.join(dateList, ","));
        result.setOrderCountList(StringUtils.join(orderCountList, ","));
        result.setValidOrderCountList(StringUtils.join(validOrderCountList, ","));

        return Result.success(result);
    }

    @Override
    public Result<TurnoverReportVO> turnoverStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 批量查询营业额数据
        List<DailyTurnoverVO> dailyTurnovers = orderMapper.getDailyTurnover(beginTime, endTime);

        // 构建日期映射
        Map<LocalDate, Double> turnoverMap = new HashMap<>();
        for (DailyTurnoverVO dt : dailyTurnovers) {
            turnoverMap.put(dt.getDate(), dt.getTurnover());
        }

        // 构建完整的日期序列
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> turnoverList = new ArrayList<>();

        LocalDate current = begin;
        while (!current.isAfter(end)) {
            dateList.add(current);
            turnoverList.add(turnoverMap.getOrDefault(current, 0.0));
            current = current.plusDays(1);
        }

        return Result.success(
                TurnoverReportVO.builder()
                        .dateList(StringUtils.join(dateList, ","))
                        .turnoverList(StringUtils.join(turnoverList, ","))
                        .build()
        );
    }

    @Override
    public Result<UserReportVO> userStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 批量查询每日新增用户数
        List<UserDailyCountVO> dailyCounts = userMapper.getUserDailyStatistics(beginTime, endTime);

        // 构建日期映射
        Map<LocalDate, Integer> newUserMap = new HashMap<>();
        for (UserDailyCountVO count : dailyCounts) {
            newUserMap.put(count.getDate(), count.getNewUserCount());
        }

        // 构建完整的日期序列
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        LocalDate current = begin;
        while (!current.isAfter(end)) {
            dateList.add(current);
            newUserList.add(newUserMap.getOrDefault(current, 0));
            current = current.plusDays(1);
        }

        // 优化：只查询最后一天的总用户数，然后反推之前每天的总用户数
        Integer finalTotalUser = userMapper.countByMap(null, endTime);
        List<Integer> totalUserList = calculateCumulativeUsers(newUserList, finalTotalUser);

        return Result.success(
                UserReportVO.builder()
                        .dateList(StringUtils.join(dateList, ","))
                        .newUserList(StringUtils.join(newUserList, ","))
                        .totalUserList(StringUtils.join(totalUserList, ","))
                        .build()
        );
    }

    @Override
    public Result<SalesTop10ReportVO> top10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime);

        String nameList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()), ",");
        String numberList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()), ",");

        return Result.success(
                SalesTop10ReportVO.builder()
                        .nameList(nameList)
                        .numberList(numberList)
                        .build()
        );
    }

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now().minusDays(1);
        //查询概览运营数据，提供给Excel模板文件
        BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/OperationalTemplate.xlsx");

        if (inputStream == null) {
            throw new RuntimeException("无法找到Excel模板文件: template/OperationalTemplate.xlsx");
        }
        try {
            //基于提供好的模板文件创建一个新的Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(inputStream);
            //获得Excel文件中的一个Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            sheet.getRow(1).getCell(1).setCellValue(begin + "至" + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            //获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());
            for (int i = 0; i < 7; i++) {
                LocalDate date = begin.plusDays(i);
                //准备明细数据
                businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }
            //通过输出流将文件下载到客户端浏览器中
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            //关闭资源
            out.flush();
            out.close();
            excel.close();

        } catch (IOException e) {
            log.error("导出运营数据时发生IO异常", e);
        }
    }

    /**
     * 根据每日新增用户数和最终总用户数，计算每天的累计用户数
     */
    private List<Integer> calculateCumulativeUsers(List<Integer> newUserList, Integer finalTotalUser) {
        List<Integer> totalUserList = new ArrayList<>();
        if (newUserList.isEmpty()) {
            return totalUserList;
        }

        // 从最后一天倒推计算每天的累计用户数
        int cumulative = finalTotalUser;
        for (int i = newUserList.size() - 1; i >= 0; i--) {
            totalUserList.add(0, cumulative);
            cumulative -= newUserList.get(i);
        }

        return totalUserList;
    }
}
