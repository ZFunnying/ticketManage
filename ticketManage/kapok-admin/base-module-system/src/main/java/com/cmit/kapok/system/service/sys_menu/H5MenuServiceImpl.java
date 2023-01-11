package com.cmit.kapok.system.service.sys_menu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.sys_menu.mapper.H5MenuMapper;
import com.cmit.kapok.system.entity.sys_menu.H5Menu;
import com.cmit.kapok.system.api.sys_menu.H5MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by lizhitao on 2019/11/04.
 */
@Service
@Transactional
public class H5MenuServiceImpl extends ServiceImpl<H5MenuMapper,H5Menu> implements H5MenuService {
    @Resource
    private H5MenuMapper h5MenuMapper;
    @Override
    public List<H5Menu> getTreeNode(int node, List childrenIds) {
        if(childrenIds != null){
            String ids = "'"+StringUtils.join(childrenIds,"','")+"'";
            return h5MenuMapper.getTreeNode(node,ids);
        }else{
            return h5MenuMapper.getTreeNodeWithoutChild(node);
        }

    }
    @Override
    public List<H5Menu> getChildrenNode(int parentId) {
        return h5MenuMapper.getChildrenNode(parentId);
    }

    @Override
    public int getParentMaxSort(int parentId) {
        int sort = h5MenuMapper.getParentMaxSort(parentId);
        return sort;
    }

    @Override
    public int getMaxSort() {
        int sort = h5MenuMapper.getMaxSort();
        return sort;
    }
}
