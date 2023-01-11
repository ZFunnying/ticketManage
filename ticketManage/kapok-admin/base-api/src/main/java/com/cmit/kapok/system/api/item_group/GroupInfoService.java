package com.cmit.kapok.system.api.item_group;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.item_group.GroupInfo;


/**
 * Created by lizhitao on 2019/01/09.
 */
public interface GroupInfoService extends IService<GroupInfo> {
    int getGroupCount();
    int getMaxSort();
}
