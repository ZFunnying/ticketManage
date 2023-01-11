package com.cmit.kapok.system.entity.sys_menu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author lizhitao
 * @since 2021-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer node;

    @Size(max = 255, message = "超过最大长度[255]")
    private String path;

    @Size(max = 255, message = "超过最大长度[255]")
    private String redirect;

    @Size(max = 255, message = "超过最大长度[255]")
    private String component;

    @Size(max = 255, message = "超过最大长度[255]")
    private String name;

    private Boolean hasChildren;

    private Integer sort;

    private Boolean isShow;

    @Size(max = 255, message = "超过最大长度[255]")
    private String title;

    @Size(max = 100, message = "超过最大长度[100]")
    private String icon;

    @Size(max = 255, message = "超过最大长度[255]")
    private String roles;

    private Boolean noCache;

    private Boolean affix;

    private Boolean breadcrumb;

    private Boolean alwaysShow;


}
