package com.cmit.kapok.system.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.api.sys_role.UserRoleRelService;
import com.cmit.kapok.system.api.user.SysUserService;
import com.cmit.kapok.system.entity.sys_role.UserRoleRel;
import com.cmit.kapok.system.entity.user.SysUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by lizhitao on 2019/01/02.
*/
@RestController
@RequestMapping("/sysuser")
@Api(tags = "用户管理")
public class SysUserController {
    private final Logger logger = LoggerFactory.getLogger(SysUserController.class);
    @Autowired
    private ObjectMapper objectMapper;
    @Resource
    private SysUserService sysUserService;
    @Autowired
    private UserRoleRelService userRoleRelService;
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody SysUser sysUser) {
        sysUserService.save(sysUser);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody SysUser sysUser) throws JsonProcessingException {
        SysUser user = sysUserService.getById(sysUser.getId());
        sysUserService.removeById(sysUser.getId());
        List<UserRoleRel> userRoleRels = userRoleRelService.list(new QueryWrapper<UserRoleRel>().eq("user_id", sysUser.getId()));
        userRoleRelService.remove(new QueryWrapper<UserRoleRel>().eq("user_id", sysUser.getId()));
        logger.info("删除用户（{}）及用户角色关系：{}",user.getUserName(), objectMapper.writeValueAsString(userRoleRels));
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        SysUser sysUser = sysUserService.getById(id);
        return ResultGenerator.genSuccessResult(sysUser);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysUser> list = sysUserService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation(value="获取用户列表", notes="")
    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody SysUser sysUser,@RequestParam String pageSize,@RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<SysUser> list = sysUserService.list(new QueryWrapper<>(sysUser));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation(value="获取用户的下拉列表", notes="")
    @RequestMapping(value = "/queryIdAndUsername", method = RequestMethod.POST)
    public Result queryIdAndUsername() {
        List<Map> list = sysUserService.findAllIdAndUsername();
        return ResultGenerator.genSuccessResult(list);
    }
    @ApiOperation(value="获取所有用户的用户名", notes="")
    @RequestMapping(value = "/getName", method = RequestMethod.GET)
    public Result getName() {
        List<SysUser> relist = sysUserService.list();
        Map reMap = new HashMap();
        if(relist != null){
            for(SysUser temp:relist){
                reMap.put(temp.getId().toString(),temp.getRealName());
            }
        }
        return ResultGenerator.genSuccessResult(reMap);
    }
}
