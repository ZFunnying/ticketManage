package com.cmit.kapok.system.service.item_group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.item_group.GroupInfo;
import org.apache.ibatis.annotations.Select;


public interface GroupInfoMapper extends BaseMapper<GroupInfo> {
    @Select("select count(*) from group_info")
    int getGroupCount();
    @Select("select ifnull(max(sort),0) from group_info")
    int getMaxSort();
}