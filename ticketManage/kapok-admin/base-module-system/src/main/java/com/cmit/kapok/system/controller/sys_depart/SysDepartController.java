package com.cmit.kapok.system.controller.sys_depart;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.entity.sys_depart.SysDepart;
import com.cmit.kapok.system.api.sys_depart.ISysDepartService;
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
@RequestMapping("/sysdepart")
public class SysDepartController {
    @Resource
    private ISysDepartService sysDepartService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody @Valid SysDepart sysDepart) {
        sysDepartService.save(sysDepart);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody SysDepart sysDepart) {
        sysDepartService.removeById(sysDepart.getId());
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody SysDepart sysDepart) {
        sysDepartService.updateById(sysDepart);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        SysDepart sysDepart = sysDepartService.getById(id);
        return ResultGenerator.genSuccessResult(sysDepart);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<SysDepart> list = sysDepartService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody SysDepart sysDepart,@RequestParam String pageSize,@RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<SysDepart> list = sysDepartService.list(new QueryWrapper<>(sysDepart));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
