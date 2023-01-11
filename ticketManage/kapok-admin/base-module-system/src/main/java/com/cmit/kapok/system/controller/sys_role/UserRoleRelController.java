package com.cmit.kapok.system.controller.sys_role;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.controller.sys_role.vo.UserRoleParams;
import com.cmit.kapok.system.entity.sys_role.UserRoleRel;
import com.cmit.kapok.system.api.sys_role.UserRoleRelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by lizhitao on 2019/01/02.
*/
@RestController
@RequestMapping("/userrolerel")
@Api(tags = "角色相关")
public class UserRoleRelController {
    @Resource
    private UserRoleRelService userRoleRelService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody UserRoleRel userRoleRel) {
        userRoleRelService.save(userRoleRel);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody UserRoleRel userRoleRel) {
        userRoleRelService.removeById(userRoleRel.getId());
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody UserRoleRel userRoleRel) {
        userRoleRelService.updateById(userRoleRel);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        UserRoleRel userRoleRel = userRoleRelService.getById(id);
        return ResultGenerator.genSuccessResult(userRoleRel);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<UserRoleRel> list = userRoleRelService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody UserRoleRel userRoleRel) {
        int page = 1;
        int size = 10;
        PageHelper.startPage(page, size);
        List<UserRoleRel> list = userRoleRelService.list(new QueryWrapper<>(userRoleRel));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
    @ApiOperation(value="更新用户角色关联关系", notes="")
    @RequestMapping(value = "/updateUserRoleRel", method = RequestMethod.POST)
    public Result updateUserRoleRel(@RequestBody UserRoleParams userRoleParams){
        String userId = userRoleParams.getId();
        List<String> rolesList = userRoleParams.getCheckedPart();
        userRoleRelService.deleteByUserId(Integer.parseInt(userId));
        userRoleRelService.updateUserRoleRel(Integer.parseInt(userId),rolesList);
        return ResultGenerator.genSuccessResult();
    }
}
