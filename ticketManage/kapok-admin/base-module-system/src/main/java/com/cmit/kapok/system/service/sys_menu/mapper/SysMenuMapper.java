package com.cmit.kapok.system.service.sys_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.sys_menu.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    //    @Select("select * from sys_menu where node=#{node} and id in (${childrenIds}) order by sort")
    List<SysMenu> getTreeNode(@Param("node")int node, @Param("childrenIds")String childrenIds);

    //    @Select("select * from sys_menu where node=#{node} order by sort")
    List<SysMenu> getTreeNodeWithoutChild(@Param("node")int node);

    List<SysMenu> getChildrenNode(@Param("parentId")int parentId);

    @Select("select ifnull(max(sm.sort),0) from sys_menu sm where sm.node = 0")
    int getMaxSort();

    @Select("select ifnull(max(sm.sort),0) from sys_menu sm where sm.id in (select children_id from menu_parent_children_rel where parent_id = #{parentId})")
    int getParentMaxSort(@Param("parentId") int parentId);
}