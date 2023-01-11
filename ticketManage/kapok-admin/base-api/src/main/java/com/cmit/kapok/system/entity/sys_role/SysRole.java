package com.cmit.kapok.system.entity.sys_role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class SysRole {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String role;

    private String description;
}