package com.cmit.kapok.system.service.sys_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.sys_menu.H5Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface H5MenuMapper extends BaseMapper<H5Menu> {
    List<H5Menu> getTreeNode(@Param("node")int node, @Param("childrenIds")String childrenIds);

    List<H5Menu> getTreeNodeWithoutChild(@Param("node")int node);

    List<H5Menu> getChildrenNode(@Param("parentId")int parentId);

    @Select("select ifnull(max(hm.sort),0) from h5_menu hm where hm.node = 0")
    int getMaxSort();

    @Select("select ifnull(max(hm.sort),0) from h5_menu hm where hm.id in (select children_id from h5_menu_parent_children_rel where parent_id = #{parentId})")
    int getParentMaxSort(@Param("parentId") int parentId);
}