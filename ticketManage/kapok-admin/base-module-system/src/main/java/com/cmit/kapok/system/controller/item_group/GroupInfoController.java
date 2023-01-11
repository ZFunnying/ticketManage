package com.cmit.kapok.system.controller.item_group;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.api.item_group.GroupInfoService;
import com.cmit.kapok.system.api.item_group.GroupItemService;
import com.cmit.kapok.system.entity.item_group.GroupInfo;
import com.cmit.kapok.system.entity.item_group.GroupItem;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by lizhitao on 2019/01/09.
*/
@RestController
@RequestMapping("/groupinfo")
public class GroupInfoController {
    @Resource
    private GroupInfoService groupInfoService;
    @Resource
    private GroupItemService groupItemService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody GroupInfo groupInfo) {
        List<GroupInfo> findGourpInfo = groupInfoService.list(
                new QueryWrapper<GroupInfo>().eq("group_code",groupInfo.getGroupCode()));
        if(null != findGourpInfo && !findGourpInfo.isEmpty()){
            return ResultGenerator.genFailResult("存在相同编号字典组");
        }
        groupInfo.setState(true);
        if(null == groupInfo.getSort()){
            Integer sort = groupInfoService.getMaxSort();
            groupInfo.setSort(sort+1);
        }
        groupInfoService.save(groupInfo);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody GroupInfo groupInfo) {
        GroupInfo findGroupInfo = groupInfoService.getById(groupInfo.getId());
        groupInfoService.removeById(groupInfo.getId());
        groupItemService.removeByGroupCode(findGroupInfo.getGroupCode());
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody GroupInfo groupInfo) {
        int groupInfoId = groupInfo.getId();
        GroupInfo info = groupInfoService.getById(groupInfoId);
        groupItemService.updateGroupInfoCode(info.getGroupCode(),groupInfo.getGroupCode());
        groupInfoService.updateById(groupInfo);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        GroupInfo groupInfo = groupInfoService.getById(id);
        return ResultGenerator.genSuccessResult(groupInfo);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<GroupInfo> list = groupInfoService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody GroupInfo groupInfo,@RequestParam String pageSize,@RequestParam String pageNum) {
        groupInfo.setState(true);
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<GroupInfo> list = groupInfoService.list(new QueryWrapper<>(groupInfo));
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
