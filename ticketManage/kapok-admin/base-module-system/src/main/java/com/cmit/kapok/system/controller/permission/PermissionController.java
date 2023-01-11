package com.cmit.kapok.system.controller.permission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.api.permission.ShiroService;
import com.cmit.kapok.system.entity.permission.Permission;
import com.cmit.kapok.system.api.permission.PermissionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by lizhitao on 2019/03/11.
*/
@RestController
@RequestMapping("/permission")
@Api(tags = "权限管理")
public class PermissionController {
    @Resource
    private PermissionService permissionService;
    @Resource
    private ShiroService shiroService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Permission permission) {
        Permission hasPermission = permissionService.getOne(new QueryWrapper<Permission>().eq("url",permission.getUrl()));
        if(null != hasPermission){
            return ResultGenerator.genFailResult("已存在该权限规则！");
        }
        permissionService.save(permission);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody Permission permission) {
        permissionService.removeById(permission.getId());
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        Permission permission = permissionService.getById(id);
        return ResultGenerator.genSuccessResult(permission);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Permission> list = permissionService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody Permission permission,@RequestParam String pageSize,@RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<Permission> list = permissionService.list(new QueryWrapper<>(permission));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/updatePermisstion", method = RequestMethod.GET)
    public Result updatePermisstion() {
        shiroService.updatePermission();
        return ResultGenerator.genSuccessResult();
    }
}
