package com.cmit.kapok.system.controller.sys_menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.api.sys_menu.H5MenuParentChildrenRelService;
import com.cmit.kapok.system.api.sys_menu.H5MenuService;
import com.cmit.kapok.system.api.sys_role.SysRoleService;
import com.cmit.kapok.system.controller.sys_menu.vo.SysMenuParams;
import com.cmit.kapok.system.entity.sys_menu.H5Menu;
import com.cmit.kapok.system.entity.sys_menu.H5MenuParentChildrenRel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* Created by lizhitao on 2019/11/04.
*/
@RestController
@RequestMapping("/h5menu")
public class H5MenuController {
    private final Logger logger = LoggerFactory.getLogger(H5MenuController.class);
    @Resource
    private H5MenuService h5MenuService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private H5MenuParentChildrenRelService h5MenuParentChildrenRelService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody SysMenuParams sysMenuParams) {
        H5Menu h5Menu = new H5Menu();
        h5Menu.setNode(sysMenuParams.getNode());
        h5Menu.setIsShow(sysMenuParams.getIsShow());
        h5Menu.setPath(sysMenuParams.getPath());
        h5Menu.setSort(sysMenuParams.getSort());
        h5Menu.setComponent(sysMenuParams.getComponent());
        h5Menu.setHasChildren(sysMenuParams.getHasChildren());
        h5Menu.setTitle((String)sysMenuParams.getMeta().get("title"));
        h5Menu.setIcon((String)sysMenuParams.getMeta().get("icon"));
        h5Menu.setRoles(ListToString((List)sysMenuParams.getMeta().get("roles")));
        if(sysMenuParams.getIsChildren()){
            int parentId = Integer.parseInt(sysMenuParams.getParentId());
            H5Menu sysMenuServiceById = h5MenuService.getById(parentId);
            h5Menu.setNode(sysMenuServiceById.getNode()+1);
            // 保存子菜单
            if(h5Menu.getSort() == null){
                int sort = h5MenuService.getParentMaxSort(parentId);
                h5Menu.setSort(sort + 1);
            }
            h5MenuService.save(h5Menu);
            int childrenId = h5Menu.getId();

            // 子母菜单建立联系
            H5MenuParentChildrenRel h5MenuParentChildrenRel = new H5MenuParentChildrenRel();
            h5MenuParentChildrenRel.setParentId(parentId);
            h5MenuParentChildrenRel.setChildrenId(childrenId);
            h5MenuParentChildrenRelService.save(h5MenuParentChildrenRel);

            // 母菜单回传hasChildren
            H5Menu parentMenu = new H5Menu();
            parentMenu.setHasChildren(true);
            parentMenu.setId(parentId);
            h5MenuService.updateById(parentMenu);
        }else {
            if(null == h5Menu.getSort()){
                int maxSort = h5MenuService.getMaxSort();
                h5Menu.setSort(maxSort+1);
            }
            h5MenuService.save(h5Menu);
        }
        Map<String,Object> reMap = new HashMap<String,Object>();
        reMap.put("id",h5Menu.getId());
        return ResultGenerator.genSuccessResult(reMap);
    }
    private String ListToString(List<String> params){
        if (params.size() == 1){
            return params.get(0);
        }
        if(params.size() == 0){
            return "";
        }
        return String.join(",", params);
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody H5Menu h5Menu) {
        h5MenuService.removeById(h5Menu.getId());
        List<H5MenuParentChildrenRel> ids = h5MenuParentChildrenRelService.list(
                new QueryWrapper<H5MenuParentChildrenRel>().eq("parent_id",h5Menu.getId()));
        for(H5MenuParentChildrenRel childrenId:ids){
            h5MenuParentChildrenRelService.removeById(childrenId.getId());
            h5MenuService.removeById(childrenId.getChildrenId());
        }
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody SysMenuParams sysMenuParams) {
        // 添加菜单
        H5Menu h5Menu = new H5Menu();
        h5Menu.setId(sysMenuParams.getId());
        h5Menu.setNode(sysMenuParams.getNode());
        h5Menu.setIsShow(sysMenuParams.getIsShow());
        h5Menu.setComponent(sysMenuParams.getComponent());
        h5Menu.setPath(sysMenuParams.getPath());
        h5Menu.setSort(sysMenuParams.getSort());
        h5Menu.setHasChildren(sysMenuParams.getHasChildren());
        h5Menu.setTitle((String)sysMenuParams.getMeta().get("title"));
        h5Menu.setIcon((String)sysMenuParams.getMeta().get("icon"));
        h5Menu.setRoles(ListToString((List)sysMenuParams.getMeta().get("roles")));
        h5MenuService.updateById(h5Menu);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        H5Menu h5Menu = h5MenuService.getById(id);
        return ResultGenerator.genSuccessResult(h5Menu);
    }
    @RequestMapping(value = "/getRouters", method = RequestMethod.GET)
    public Result getRouters(@RequestParam String userId){
        List<String> roles = sysRoleService.queryRolesByUserId(Integer.parseInt(userId));
        return ResultGenerator.genSuccessResult(getMenuTree(0,null,roles));
    }
    public List<Object> getMenuTree(int node,List childrenIds,List<String> roles){
        List<H5Menu> result = h5MenuService.getTreeNode(node,childrenIds);
        List<Object> reList = new ArrayList<>();
        for(H5Menu menu:result){
            Map<String,Object> reMap = new HashMap<String,Object>();
            reMap.put("path",menu.getPath());
            reMap.put("component",menu.getComponent());
            reMap.put("icon",menu.getIcon());
            reMap.put("title",menu.getTitle());
            reMap.put("roles",menu.getRoles());
            if(menu.getIsShow() == false){
                reMap.put("isShow",false);
            }else {
                reMap.put("isShow",true);
            }

            // 无菜单角色所有人都可以看见
            if(null != menu.getRoles() && StringUtils.isNotEmpty(menu.getRoles())){
                String[] roleList = menu.getRoles().split(",");
                // 查看有无并集
                if(roles.stream().filter(funcCode -> Arrays.asList(roleList).contains(funcCode)).collect(Collectors.toList()).size()<=0){
                    continue;
                }
            }

            if(null != menu.getHasChildren() && menu.getHasChildren()){
                H5MenuParentChildrenRel h5MenuParentChildrenRel = new H5MenuParentChildrenRel();
                h5MenuParentChildrenRel.setParentId(menu.getId());
                List<H5MenuParentChildrenRel> childrenRels = h5MenuParentChildrenRelService.list(new QueryWrapper<>(h5MenuParentChildrenRel));
                List Ids = new ArrayList();
                for(H5MenuParentChildrenRel childrenId:childrenRels){
                    Ids.add(childrenId.getChildrenId());
                }
                reMap.put("children",getMenuTree(node+1,Ids,roles));
            }
            reList.add(reMap);
        }
        return reList;
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<H5Menu> list = h5MenuService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody H5Menu h5Menu,@RequestParam String pageSize,@RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<H5Menu> list = h5MenuService.list(new QueryWrapper<>(h5Menu));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/getParentNode", method = RequestMethod.GET)
    public Result getParentNode(){
        List<H5Menu> result = h5MenuService.getTreeNode(0,null);
        List<Object> reList = new ArrayList<>();
        for(H5Menu menu:result){
            Map<String,Object> reMap = new HashMap<String,Object>();
            reMap.put("id",menu.getId());
            reMap.put("path",menu.getPath());
            reMap.put("component",menu.getComponent());
            reMap.put("title",menu.getTitle());
            reMap.put("sort",menu.getSort());
            reMap.put("isShow",menu.getIsShow());
            reMap.put("isShowFlag",menu.getIsShow().toString());
            reMap.put("hasChildren",menu.getHasChildren());
            reMap.put("icon",menu.getIcon());
            reMap.put("title",menu.getTitle());
            reMap.put("roles",getRoles(menu.getRoles()));
            reList.add(reMap);
        }
        return ResultGenerator.genSuccessResult(reList);
    }
    @RequestMapping(value = "/getChildrenNode", method = RequestMethod.GET)
    public Result getChildrenNode(@RequestParam String parentId){
        List<H5Menu> result = h5MenuService.getChildrenNode(Integer.parseInt(parentId));
        List<Object> reList = new ArrayList<>();
        for(H5Menu menu:result){
            Map<String,Object> reMap = new HashMap<String,Object>();
            reMap.put("id",menu.getId());
            reMap.put("path",menu.getPath());
            reMap.put("title",menu.getTitle());
            reMap.put("component",menu.getComponent());
            reMap.put("sort",menu.getSort());
            reMap.put("isShow",menu.getIsShow());
            reMap.put("parentId",parentId);
            reMap.put("isShowFlag",menu.getIsShow().toString());
            reMap.put("hasChildren",menu.getHasChildren());
            reMap.put("icon",menu.getIcon());
            reMap.put("title",menu.getTitle());
            reMap.put("roles",getRoles(menu.getRoles()));
            reList.add(reMap);
        }
        return ResultGenerator.genSuccessResult(reList);
    }
    private List getRoles(String roles){
        List<String> rolesList = new ArrayList<String>();
        if(null == roles){
            logger.info("getMeta() ----> 无角色信息");
        } else if(roles.contains(",")){
            rolesList = Arrays.asList(roles.split(","));
        }else{
            rolesList.add(roles);
        }
        return rolesList;
    }
}
