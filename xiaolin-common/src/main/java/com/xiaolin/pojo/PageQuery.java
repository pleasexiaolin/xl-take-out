package com.xiaolin.pojo;

import lombok.Setter;

import java.util.Objects;

/**
 * 分页查询类
 */
@Setter
public class PageQuery {

    /**
     * 每页条数
     */
    private Integer limit;
    /**
     * 当前页面
     */
    private Integer page;

    public Integer getLimit() {
        if (Objects.isNull(limit)) {
            return 10;
        }
        return limit;
    }

    public Integer getPage() {
        if (Objects.isNull(page)) {
            return 1;
        }
        return page;
    }

}
