package com.cmit.kapok.system.controller.sys_menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.api.sys_menu.MenuParentChildrenRelService;
import com.cmit.kapok.system.api.sys_menu.SysMenuService;
import com.cmit.kapok.system.controller.sys_menu.vo.SysMenuParams;
import com.cmit.kapok.system.entity.sys_menu.MenuParentChildrenRel;
import com.cmit.kapok.system.entity.sys_menu.SysMenu;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cmit.kapok.system.utils.FileUtil.genDirTree;

/**
* Created by lizhitao on 2019/01/03.
*/
@RestController
@RequestMapping("/sysmenu")
public class SysMenuController {
    private final Logger logger = LoggerFactory.getLogger(SysMenuController.class);
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private MenuParentChildrenRelService menuParentChildrenRelService;
    @Value("${spring.profiles.active}")
    private String env;//当前激活的配置文件
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody SysMenuParams sysMenuParams) {
        // 添加菜单
        SysMenu sysMenu = new SysMenu();
        sysMenu.setNode(sysMenuParams.getNode());
        sysMenu.setAlwaysShow(sysMenuParams.getAlwaysShow());
        sysMenu.setComponent(sysMenuParams.getComponent());
        sysMenu.setIsShow(sysMenuParams.getIsShow());
        sysMenu.setName(sysMenuParams.getName());
        sysMenu.setPath(sysMenuParams.getPath());
        sysMenu.setSort(sysMenuParams.getSort());
        sysMenu.setHasChildren(sysMenuParams.getHasChildren());
        sysMenu.setRedirect(sysMenuParams.getRedirect());
        sysMenu.setTitle((String)sysMenuParams.getMeta().get("title"));
        sysMenu.setIcon((String)sysMenuParams.getMeta().get("icon"));
        sysMenu.setRoles(ListToString((List)sysMenuParams.getMeta().get("roles")));
        sysMenu.setAffix(sysMenuParams.getAffix())
                .setBreadcrumb(sysMenuParams.getBreadcrumb())
                .setNoCache(sysMenuParams.getNoCache());
        if(sysMenuParams.getIsChildren()){
            int parentId = Integer.parseInt(sysMenuParams.getParentId());
            SysMenu sysMenuServiceById = sysMenuService.getById(parentId);
            sysMenu.setNode(sysMenuServiceById.getNode()+1);
            // 保存子菜单
            if(sysMenu.getSort() == null){
                int sort = sysMenuService.getParentMaxSort(parentId);
                sysMenu.setSort(sort + 1);
            }
            sysMenuService.save(sysMenu);
            int childrenId = sysMenu.getId();

            // 子母菜单建立联系
            MenuParentChildrenRel menuParentChildrenRel = new MenuParentChildrenRel();
            menuParentChildrenRel.setParentId(parentId);
            menuParentChildrenRel.setChildrenId(childrenId);
            menuParentChildrenRelService.save(menuParentChildrenRel);

            // 母菜单回传hasChildren
            SysMenu parentMenu = new SysMenu();
            parentMenu.setHasChildren(true);
            parentMenu.setId(parentId);
            sysMenuService.updateById(parentMenu);
        }else {
            if(null == sysMenu.getSort()){
                int maxSort = sysMenuService.getMaxSort();
                sysMenu.setSort(maxSort+1);
            }
            sysMenuService.save(sysMenu);
        }
        Map<String,Object> reMap = new HashMap<String,Object>();
        reMap.put("id",sysMenu.getId());
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
    public Result delete(@RequestBody SysMenu sysMenu) {
        sysMenuService.removeById(sysMenu.getId());
        List<MenuParentChildrenRel> ids = menuParentChildrenRelService.list(
                new QueryWrapper<MenuParentChildrenRel>().eq("parent_id",sysMenu.getId()));
        for(MenuParentChildrenRel childrenId:ids){
            menuParentChildrenRelService.removeById(childrenId.getId());
            sysMenuService.removeById(childrenId.getChildrenId());
        }
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody SysMenuParams sysMenuParams) {
        // 添加菜单
        SysMenu sysMenu = new SysMenu();
        sysMenu.setId(sysMenuParams.getId());
        sysMenu.setNode(sysMenuParams.getNode());
        sysMenu.setAlwaysShow(sysMenuParams.getAlwaysShow());
        sysMenu.setComponent(sysMenuParams.getComponent());
        sysMenu.setIsShow(sysMenuParams.getIsShow());
        sysMenu.setName(sysMenuParams.getName());
        sysMenu.setPath(sysMenuParams.getPath());
        sysMenu.setSort(sysMenuParams.getSort());
        sysMenu.setHasChildren(sysMenuParams.getHasChildren());
        sysMenu.setRedirect(sysMenuParams.getRedirect());
        sysMenu.setTitle((String)sysMenuParams.getMeta().get("title"));
        sysMenu.setIcon((String)sysMenuParams.getMeta().get("icon"));
        sysMenu.setRoles(ListToString((List)sysMenuParams.getMeta().get("roles")));
        sysMenu.setAffix(sysMenuParams.getAffix())
                .setBreadcrumb(sysMenuParams.getBreadcrumb())
                .setNoCache(sysMenuParams.getNoCache());
        sysMenuService.updateById(sysMenu);
        sysMenuService.updateRouters();
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        return ResultGenerator.genSuccessResult(sysMenu);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysMenu> list = sysMenuService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody SysMenu sysMenu) {
        int page = 1;
        int size = 10;
        PageHelper.startPage(page, size);
        List<SysMenu> list = sysMenuService.list(new QueryWrapper<>(sysMenu));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @RequestMapping(value = "/getVueModuleDirTree", method = RequestMethod.GET)
    public Result getVueModuleDirTree(){
        if(env.equals("dev")){
            return ResultGenerator.genSuccessResult(genDirTree(System.getProperty("user.dir") + "/webapp/src/modules",0,null));
        }else {
            return ResultGenerator.genSuccessResult();
        }    }

    @RequestMapping(value = "/getRouters", method = RequestMethod.GET)
    public Result getRouters(){
        return ResultGenerator.genSuccessResult(sysMenuService.getMenuTree());
    }

    @RequestMapping(value = "/getParentNode", method = RequestMethod.GET)
    public Result getParentNode(){
        List<SysMenu> result = sysMenuService.getTreeNode(0,null);
        List<Object> reList = new ArrayList<>();
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
            reMap.put("isShowFlag",menu.getIsShow().toString());
            reMap.put("hasChildren",menu.getHasChildren());
            reMap.put("icon",menu.getIcon());
            reMap.put("meta",sysMenuService.getMeta(menu.getHasChildren(),menu.getTitle()
                    ,menu.getIcon(),menu.getRoles(),menu.getNoCache(),menu.getBreadcrumb(),menu.getAffix()));
            if(menu.getHasChildren() != null && menu.getHasChildren()){
                reMap.put("children",sysMenuService.getChildrenNode(menu.getId()));
            }
            reList.add(reMap);
        }
        return ResultGenerator.genSuccessResult(reList);
    }

    @RequestMapping(value = "/getChildrenNode", method = RequestMethod.GET)
    public Result getChildrenNode(@RequestParam Integer parentId){
        List<Map> result = sysMenuService.getChildrenNode(parentId);
        return ResultGenerator.genSuccessResult(result);
    }
}
