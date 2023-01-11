package com.cmit.kapok.system.entity.item_group;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class GroupInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer sort;

    private String groupCode;

    private String groupName;

    private String remark;

    private boolean state;

}