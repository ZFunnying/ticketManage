package com.cmit.kapok.system.service.sys_role;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.sys_role.mapper.SysRoleMapper;
import com.cmit.kapok.system.entity.sys_role.SysRole;
import com.cmit.kapok.system.api.sys_role.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**I
 * Created by lizhitao on 2019/01/02.
 */
@Service
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<String> queryRolesByUserId(int id){
        return sysRoleMapper.queryRolesByUserId(id);
    }

    @Override
    public List<Map> queryByCond(Map param) {
        return sysRoleMapper.queryByCond(param);
    }
}
