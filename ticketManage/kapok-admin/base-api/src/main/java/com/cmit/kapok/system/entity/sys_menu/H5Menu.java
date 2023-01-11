package com.cmit.kapok.system.entity.sys_menu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class H5Menu {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer node;

    private String path;

    private String component;

    private String icon;

    private String title;

    private Boolean hasChildren;

    private String roles;

    private Integer sort;

    private Boolean isShow;
}