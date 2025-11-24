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
    private Integer pageSize;
    /**
     * 当前页面
     */
    private Integer page;

    public Integer getLimit() {
        if (Objects.isNull(pageSize)) {
            return 10;
        }
        return pageSize;
    }

    public Integer getPage() {
        if (Objects.isNull(page)) {
            return 1;
        }
        return page;
    }

}
