package com.cmit.kapok.system.controller.sys_depart;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.entity.sys_depart.SysUserDepart;
import com.cmit.kapok.system.api.sys_depart.ISysUserDepartService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import javax.validation.Valid;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by  on 2022/03/08.
*/
@RestController
@RequestMapping("/sysuserdepart")
public class SysUserDepartController {
    @Resource
    private ISysUserDepartService sysUserDepartService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody @Valid SysUserDepart sysUserDepart) {
        sysUserDepartService.save(sysUserDepart);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody SysUserDepart sysUserDepart) {
        sysUserDepartService.removeById(sysUserDepart.getId());
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody SysUserDepart sysUserDepart) {
        sysUserDepartService.updateById(sysUserDepart);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        SysUserDepart sysUserDepart = sysUserDepartService.getById(id);
        return ResultGenerator.genSuccessResult(sysUserDepart);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysUserDepart> list = sysUserDepartService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody SysUserDepart sysUserDepart,@RequestParam String pageSize,@RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<SysUserDepart> list = sysUserDepartService.list(new QueryWrapper<>(sysUserDepart));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
