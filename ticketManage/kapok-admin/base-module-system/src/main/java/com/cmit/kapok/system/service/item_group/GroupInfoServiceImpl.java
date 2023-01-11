package com.cmit.kapok.system.service.item_group;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.item_group.mapper.GroupInfoMapper;
import com.cmit.kapok.system.api.item_group.GroupInfoService;
import com.cmit.kapok.system.entity.item_group.GroupInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by lizhitao on 2019/01/09.
 */
@Service
@Transactional
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper,GroupInfo> implements GroupInfoService {
    @Resource
    private GroupInfoMapper groupInfoMapper;


    @Override
    public int getGroupCount() {
        return groupInfoMapper.getGroupCount();
    }
    @Override
    public int getMaxSort() {
        return groupInfoMapper.getMaxSort();
    }
}
