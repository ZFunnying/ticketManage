package com.cmit.kapok.system.api.sys_menu;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.sys_menu.SysMenu;

import java.util.List;
import java.util.Map;


/**
 * Created by lizhitao on 2019/01/03.
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> getTreeNode(int node,List childrenIds);

    List<Map> getChildrenNode(int parentId);

    List<Object> updateRouters();

    List<Object> getMenuTree();

    Map<String,Object> getMeta(Boolean isParent, String title, String icon
            , String roles, Boolean noCache, Boolean breadcrumb, Boolean affix);

    int getMaxSort();

    int getParentMaxSort(int parentId);
}
