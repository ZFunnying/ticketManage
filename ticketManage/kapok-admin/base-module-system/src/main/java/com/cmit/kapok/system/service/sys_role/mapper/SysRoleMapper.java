package com.cmit.kapok.system.service.sys_role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.sys_role.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<String> queryRolesByUserId(@Param("id") int id);

    List<Map> queryByCond(@Param("param") Map param);
}