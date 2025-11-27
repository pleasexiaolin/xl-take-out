package com.xiaolin.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.xiaolin.context.BaseContext;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BaseDO {
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 利用aop注解实现注入
    public BaseDO() {
        this.createUser = BaseContext.getCurrentUser();
        this.updateUser = BaseContext.getCurrentUser();
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
