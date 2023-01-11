package com.cmit.kapok.system.controller.sys_menu.vo;


import lombok.Data;

import java.util.Map;
@Data
public class SysMenuParams {

    private Integer id;

    private Integer node;

    private String path;

    private String redirect;

    private String component;

    private String name;

    private Integer metaId;

    private Boolean hasChildren;

    private Integer sort;

    private Boolean isShow;

    private Boolean alwaysShow;

    private Map<String,Object> meta;

    private Boolean isChildren;

    private String parentId;

    private Boolean noCache;

    private Boolean affix;

    private Boolean breadcrumb;

}
