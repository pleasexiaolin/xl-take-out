package com.xiaolin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    //微信用户唯一标识
    private String openid;

    //姓名
    private String name;

    //手机号
    private String phone;

    //性别 0 女 1 男
    private String sex;

    //身份证号
    private String idNumber;

    //头像
    private String avatar;

    //注册时间
    private LocalDateTime createTime;

    // 余额
    private BigDecimal balance;

    public UserDO(String openid) {
        this.openid = openid;
        // 初始余额默认1000 模拟支付
        balance = BigDecimal.valueOf(1000);
        createTime = LocalDateTime.now();
    }

    public UserDO(Long id, BigDecimal currentBalance) {
        this.id = id;
        this.balance = currentBalance;
    }
}
