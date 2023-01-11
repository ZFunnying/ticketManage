package com.cmit.kapok.system.api.item_group;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.item_group.GroupItem;

import java.util.List;
import java.util.Map;


/**
 * Created by lizhitao on 2019/01/10.
 */
public interface GroupItemService extends IService<GroupItem> {
    List<GroupItem> findByGroupCode(String groupCode);

    List<GroupItem> queryItemBySort(int sort);

    boolean updateGroupInfoCode(String oldCode,String newCode);

    List<Map> queryServiceType(String condition);

    String getLabelByValue(String value,String groupCode);
    GroupItem getItemByValue(String value,String groupCode);

    Integer getValueByLabel(String label,String groupCode);

    List<Map> getAllDictMap();

    void removeByGroupCode(String groupCode);
}
