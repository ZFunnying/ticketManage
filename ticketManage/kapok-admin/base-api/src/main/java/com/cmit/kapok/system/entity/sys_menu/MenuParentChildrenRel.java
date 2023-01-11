package com.cmit.kapok.system.entity.sys_menu;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class MenuParentChildrenRel {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer parentId;

    private Integer childrenId;
}