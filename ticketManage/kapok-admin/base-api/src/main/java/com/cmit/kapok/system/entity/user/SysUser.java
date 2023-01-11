package com.cmit.kapok.system.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
@Data
public class SysUser {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    private Date addTime;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 介绍
     */
    private String introduction;

    /**
     * 状态
     */
    private Boolean state;
    private String realName;

}