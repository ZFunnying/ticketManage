package com.cmit.kapok.system.entity.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 接口
     */
    private String url;

    /**
     * 权限
     */
    private String roles;

    private String remark;

}