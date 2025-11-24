package com.xiaolin.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xiaolin.context.BaseContext;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class BaseDO {
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String creater;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    public BaseDO() {
        this.creater = BaseContext.getCurrentUser();
        this.updater = BaseContext.getCurrentUser();
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
