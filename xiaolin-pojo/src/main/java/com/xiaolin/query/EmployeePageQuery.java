package com.xiaolin.query;

import com.xiaolin.pojo.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lzh
 * @description: 员工分页参数
 * @date 2025/11/24 20:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePageQuery extends PageQuery {
    // 员工姓名
    private String name;
}
