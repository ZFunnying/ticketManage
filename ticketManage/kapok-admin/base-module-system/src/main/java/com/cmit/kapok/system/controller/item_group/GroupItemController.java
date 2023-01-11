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
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by lizhitao on 2019/01/10.
*/
@RestController
@RequestMapping("/groupitem")
public class GroupItemController {
    @Resource
    private GroupItemService groupItemService;

    @Resource
    private GroupInfoService groupInfoService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody GroupItem groupItem) {
        groupItem.setState(true);
        List<GroupItem> findGourpItem = groupItemService.list(
                new QueryWrapper<GroupItem>().eq("group_code",groupItem.getGroupCode())
                                             .eq("item_code",groupItem.getItemCode()));
        if(null != findGourpItem && !findGourpItem.isEmpty()){
            return ResultGenerator.genFailResult("存在相同编号字典");
        }
        if(null == groupItem.getSort()){
            List<GroupItem> groupItems = groupItemService.findByGroupCode(groupItem.getGroupCode());
            if(null == groupItems || groupItems.size() == 0){
                groupItem.setSort(1);
            }else{
                groupItem.setSort(groupItems.get(groupItems.size()-1).getSort()+1);
            }
        }
        groupItemService.save(groupItem);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody GroupItem groupItem) {
        groupItem.setState(false);
        groupItemService.updateById(groupItem);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody GroupItem groupItem) {
        groupItemService.updateById(groupItem);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        GroupItem groupItem = groupItemService.getById(id);
        return ResultGenerator.genSuccessResult(groupItem);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<GroupItem> list = groupItemService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryAllByCode", method = RequestMethod.GET)
    public Result queryAllByCode(@RequestParam String groupCode) {
        List<GroupItem> list = groupItemService.findByGroupCode(groupCode);
        return ResultGenerator.genSuccessResult(list);
    }
    @RequestMapping(value = "/queryOptionByGroupCode", method = RequestMethod.GET)
    public Result queryOptionByGroupCode(@RequestParam String groupCode) {
        List<GroupItem> list = groupItemService.findByGroupCode(groupCode);
        List reList = new ArrayList();
        for(GroupItem item:list){
            Map groupItem = new HashMap();
            String itemCode = item.getItemCode();
            if(StringUtils.isNumeric(itemCode)){
                groupItem.put("value",Integer.parseInt(itemCode));
            }else {
                groupItem.put("value",itemCode);
            }
            groupItem.put("label",item.getItemName());
            reList.add(groupItem);
        }
        return ResultGenerator.genSuccessResult(reList);
    }
    @RequestMapping(value = "/queryOption", method = RequestMethod.GET)
    public Result queryOption(@RequestParam String node) {
        int groupNum = groupInfoService.getGroupCount();
        List result = getOption(Integer.parseInt(node),groupNum);
        return ResultGenerator.genSuccessResult(result);
    }

    @RequestMapping(value = "/getAllDictMap", method = RequestMethod.POST)
    public Result getAllDictMap() {
        List<Map> list = groupItemService.getAllDictMap();
        return ResultGenerator.genSuccessResult(list);
    }

    public List getOption(int node,int groupNum){
        if(groupNum <= 0 ){
            return null;
        }
        List<GroupItem> list = groupItemService.queryItemBySort(node);
        List reList = new ArrayList();
        for(GroupItem item:list){
            Map<String,Object> reMap = new HashMap<>();
            reMap.put("itemCode",item.getItemCode());
            reMap.put("itemName",item.getItemName());
            reMap.put("children",getOption(node+1,groupNum-1));
            reList.add(reMap);
        }
        return reList;
    }

    @RequestMapping(value = "/queryAllDict", method = RequestMethod.GET)
    public Result queryAllDict() {
        List<GroupItem> itemList = groupItemService.list();
        List<GroupInfo> infoList = groupInfoService.list();
        Map<String,List> reMap = new HashMap<>();
        for(GroupInfo info:infoList){
            List groupItemList = new ArrayList();
            for(GroupItem item:itemList){
                if(info.getGroupCode() == item.getGroupCode()){
                    groupItemList.add(item);
                }
            }
            reMap.put(info.getGroupCode(),groupItemList);
        }
        return ResultGenerator.genSuccessResult(reMap);
    }

    @RequestMapping(value = "/queryServiceType", method = RequestMethod.POST)
    public Result queryServiceType(@RequestBody Map condition, @RequestParam String pageSize, @RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<Map> list = new ArrayList();
        if(condition.isEmpty()){
            list = groupItemService.queryServiceType(null);
        }else {
            list = groupItemService.queryServiceType(condition.get("condition").toString());
        }

        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
