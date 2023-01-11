package com.cmit.kapok.system.service.item_group;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.api.item_group.GroupItemService;
import com.cmit.kapok.system.entity.item_group.GroupItem;
import com.cmit.kapok.system.service.item_group.mapper.GroupItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by lizhitao on 2019/01/10.
 */
@Service
@Transactional
public class GroupItemServiceImpl extends ServiceImpl<GroupItemMapper,GroupItem> implements GroupItemService {
    @Resource
    private GroupItemMapper groupItemMapper;


    @Override
    public List<GroupItem> findByGroupCode(String groupCode) {
        return groupItemMapper.findByGroupCode(groupCode);
    }

    @Override
    public List<GroupItem> queryItemBySort(int sort) {
        return groupItemMapper.queryItemBySort(sort);
    }

    @Override
    public boolean updateGroupInfoCode(String oldCode, String newCode) {
        return groupItemMapper.updateGroupInfoName(oldCode,newCode);
    }

    public List<Map> queryServiceType(String condition) {
        return groupItemMapper.queryServiceType(condition);
    }


    @Override
    public String getLabelByValue(String value, String groupCode) {
        return groupItemMapper.getLabelByValue(value,groupCode);
    }

    @Override
    public GroupItem getItemByValue(String value, String groupCode) {
        return groupItemMapper.getItemByValue(value,groupCode);
    }

    @Override
    public Integer getValueByLabel(String label,String groupCode) {
        return groupItemMapper.getValueByLabel(label,groupCode);
    }
    @Override
    public List<Map> getAllDictMap() {
        return groupItemMapper.getAllDictMap();
    }

    @Override
    public void removeByGroupCode(String groupCode) {
        groupItemMapper.removeByGroupCode(groupCode);
    }
}
