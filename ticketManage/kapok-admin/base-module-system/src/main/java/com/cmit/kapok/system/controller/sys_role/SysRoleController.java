package com.cmit.kapok.system.controller.sys_role;

import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.api.sys_role.SysRoleService;
import com.cmit.kapok.system.entity.sys_role.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* Created by lizhitao on 2019/01/02.
*/
@RestController
@RequestMapping("/sysrole")
@Api(tags = "角色相关")
public class SysRoleController {
    @Resource
    private SysRoleService sysRoleService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody SysRole sysRole) {
        try{
            sysRoleService.save(sysRole);
        }catch (Exception e){
            return ResultGenerator.genFailResult(e.getCause().getMessage());
        }
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody SysRole sysRole) {
        sysRoleService.removeById(sysRole.getId());
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody SysRole sysRole) {
        sysRoleService.updateById(sysRole);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        SysRole sysRole = sysRoleService.getById(id);
        return ResultGenerator.genSuccessResult(sysRole);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysRole> list = sysRoleService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation(value="根据条件查询角色列表", notes="")
    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody Map param, @RequestParam String pageSize, @RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<Map> list = sysRoleService.queryByCond(param);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation(value="根据id查询角色", notes="")
    @RequestMapping(value = "/queryRoleById", method = RequestMethod.GET)
    public Result queryRoleById(@RequestParam String id) {
        List<String> reList = sysRoleService.queryRolesByUserId(Integer.parseInt(id));
        return ResultGenerator.genSuccessResult(reList);
    }
}
