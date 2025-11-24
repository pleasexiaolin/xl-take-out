package com.xiaolin.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yjj.ypzfyth.business.login.pojo.LoginUser;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

/**
 * @author lpf
 * @date Create in 2021/11/01 17:11
 */
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
    /**
     * 删除标识 00 未删除 01 已删除
     */
    @TableField(fill = FieldFill.INSERT)
    private String deleteFlag;

    public BaseDO() {
    }

    public BaseDO(LoginUser loginUser) {
        if (Objects.nonNull(loginUser)) {
            this.creater = loginUser.getUserId();
            this.updater = loginUser.getUserId();
        }
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
