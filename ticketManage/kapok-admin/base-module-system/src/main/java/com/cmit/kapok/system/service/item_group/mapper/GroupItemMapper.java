package com.cmit.kapok.system.service.item_group.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.item_group.GroupItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface GroupItemMapper extends BaseMapper<GroupItem> {


    List<GroupItem> findByGroupCode(String groupCode);

    List<GroupItem> queryItemBySort(int sort);

    @Update("update group_item set group_code=#{newCode} where group_code=#{oldCode}")
    Boolean updateGroupInfoName(@Param("oldCode") String oldCode, @Param("newCode") String newCode);

    List<Map> queryServiceType(@Param("condition") String condition);

    @Select("select item_code from group_item where item_name=#{label} and group_code=#{groupCode} limit 1")
    Integer getValueByLabel(@Param("label") String label, @Param("groupCode") String groupCode);

    @Select("select item_name from group_item where item_code=#{value} and group_code=#{groupCode} limit 1")
    String getLabelByValue(@Param("value") String value, @Param("groupCode") String groupCode);

    @Select("select item_name as itemName,remark,item_code as itemCode from group_item where item_code=#{value} and group_code=#{groupCode} limit 1")
    GroupItem getItemByValue(@Param("value") String value, @Param("groupCode") String groupCode);

    @Select("select item_name as label,item_code as value,group_code as groupCode from group_item where state=1 order by group_code,sort")
    List<Map> getAllDictMap();

    @Delete("delete from group_item where group_code = #{groupCode}")
    void removeByGroupCode(@Param("groupCode") String groupCode);
}