package com.cmit.kapok.system.service.sys_menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.api.sys_menu.MenuParentChildrenRelService;
import com.cmit.kapok.system.api.sys_menu.SysMenuService;
import com.cmit.kapok.system.entity.sys_menu.MenuParentChildrenRel;
import com.cmit.kapok.system.entity.sys_menu.SysMenu;
import com.cmit.kapok.system.service.sys_menu.mapper.SysMenuMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by lizhitao on 2019/01/03.
 */
@Service
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper,SysMenu> implements SysMenuService {
    private static final Logger logger = LoggerFactory.getLogger(SysMenuService.class);

    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private MenuParentChildrenRelService menuParentChildrenRelService;
    @Override
    public List<SysMenu> getTreeNode(int node,List childrenIds) {
        if(childrenIds != null){
            String ids = "'"+StringUtils.join(childrenIds,"','")+"'";
            return sysMenuMapper.getTreeNode(node,ids);
        }else{
            return sysMenuMapper.getTreeNodeWithoutChild(node);
        }

    }
    @Override
    @CachePut(value = "getRouters")
    public List<Object> updateRouters(){
        return getMenuTree(0,null);
    }

    @Override
    @Cacheable(value = "getRouters")
    public List<Object> getMenuTree() {
        return getMenuTree(0,null);
    }

    public List<Object> getMenuTree(int node, List childrenIds){
        List<SysMenu> result = this.getTreeNode(node,childrenIds);
        List<Object> reList = new ArrayList<>();
        for(SysMenu menu:result){
            Map<String,Object> reMap = new HashMap<String,Object>();
            reMap.put("menuId",menu.getId());
            reMap.put("path",menu.getPath());
            reMap.put("component",menu.getComponent());
            if(node==0){
                reMap.put("redirect",menu.getRedirect());
            }
            reMap.put("name",menu.getName());
            reMap.put("meta",getMeta(menu.getHasChildren(),menu.getTitle()
                    ,menu.getIcon(),menu.getRoles(),menu.getNoCache(),menu.getBreadcrumb(),menu.getAffix()));
            if(menu.getIsShow() == false){
                reMap.put("hidden",true);
            }
            if(null != menu.getHasChildren() && menu.getHasChildren()){
                MenuParentChildrenRel menuParentChildrenRel = new MenuParentChildrenRel();
                menuParentChildrenRel.setParentId(menu.getId());

                List<MenuParentChildrenRel> childrenRels = menuParentChildrenRelService.list(new QueryWrapper<>(menuParentChildrenRel));

                List Ids = new ArrayList();
                for(MenuParentChildrenRel childrenId:childrenRels){
                    Ids.add(childrenId.getChildrenId());
                }
                reMap.put("children",getMenuTree(node+1,Ids));
            }
            reList.add(reMap);
        }
        return reList;
    }

    @Override
    public List<Map> getChildrenNode(int parentId) {
        List<SysMenu> result = sysMenuMapper.getChildrenNode(parentId);
        List<Map> reList = new ArrayList<>();
        for(SysMenu menu:result){
            Map<String,Object> reMap = new HashMap<String,Object>();
            reMap.put("id",menu.getId());
            reMap.put("path",menu.getPath());
            reMap.put("component",menu.getComponent());
            reMap.put("redirect",menu.getRedirect());
            reMap.put("name",menu.getName());
            reMap.put("title",menu.getTitle());
            reMap.put("sort",menu.getSort());
            reMap.put("alwaysShow",menu.getAlwaysShow());
            reMap.put("isShow",menu.getIsShow());
            reMap.put("parentId",parentId);
            reMap.put("isShowFlag",menu.getIsShow().toString());
            reMap.put("hasChildren",menu.getHasChildren());
            reMap.put("icon",menu.getIcon());
            reMap.put("meta",getMeta(menu.getHasChildren(),menu.getTitle()
                    ,menu.getIcon(),menu.getRoles(),menu.getNoCache(),menu.getBreadcrumb(),menu.getAffix()));
            if(menu.getHasChildren() != null && menu.getHasChildren()){
                reMap.put("children",getChildrenNode(menu.getId()));
            }
            reList.add(reMap);
        }
        return reList;
    }

    @Override
    public Map<String,Object> getMeta(Boolean isParent,String title,String icon
            ,String roles,Boolean noCache,Boolean breadcrumb,Boolean affix){
        Map<String,Object> reMap = new HashMap<String,Object>();
        if(null == isParent || !isParent){
            reMap.put("noCache",noCache);
            reMap.put("breadcrumb",breadcrumb);
            reMap.put("affix",affix);
        }
        reMap.put("title",title);
        reMap.put("icon",icon);
        List<String> rolesList = new ArrayList<String>();
        if(null == roles || StringUtils.isBlank(roles)){
            logger.warn("getMeta() ----> 无角色信息");
        } else if(roles.contains(",")){
            rolesList = Arrays.asList(roles.split(","));
        }else{
            rolesList.add(roles);
        }
        if(!rolesList.isEmpty()){
            reMap.put("roles",rolesList);
        }
        return reMap;
    }
    @Override
    public int getParentMaxSort(int parentId) {
        int sort = sysMenuMapper.getParentMaxSort(parentId);
        return sort;
    }

    @Override
    public int getMaxSort() {
        int sort = sysMenuMapper.getMaxSort();
        return sort;
    }
}
