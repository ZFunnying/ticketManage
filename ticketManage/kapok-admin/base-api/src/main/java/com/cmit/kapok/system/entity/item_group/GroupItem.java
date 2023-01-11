package com.cmit.kapok.system.entity.item_group;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data

public class GroupItem {
    /**
     * 编号
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 组别
     */
    private String groupCode;

    /**
     * 组件
     */
    private String itemName;

    private String itemCode;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    private boolean state;
}