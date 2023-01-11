package com.cmit.kapok.system.api.sys_role;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.sys_role.SysRole;

import java.util.List;
import java.util.Map;


/**
 * Created by lizhitao on 2019/01/02.
 */
public interface SysRoleService extends IService<SysRole> {
    List<String> queryRolesByUserId(int id);

    List<Map> queryByCond(Map param);
}
