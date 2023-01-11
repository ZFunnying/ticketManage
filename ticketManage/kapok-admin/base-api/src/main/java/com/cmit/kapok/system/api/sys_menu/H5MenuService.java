package com.cmit.kapok.system.api.sys_menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.sys_menu.H5Menu;

import java.util.List;


/**
 * Created by lizhitao on 2019/11/04.
 */
public interface H5MenuService extends IService<H5Menu> {
    List<H5Menu> getTreeNode(int node, List childrenIds);
    List<H5Menu> getChildrenNode(int parentId);

    int getMaxSort();

    int getParentMaxSort(int parentId);
}
