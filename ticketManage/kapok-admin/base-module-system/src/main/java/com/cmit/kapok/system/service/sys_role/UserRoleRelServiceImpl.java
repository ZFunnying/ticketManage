package com.cmit.kapok.system.service.sys_role;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.sys_role.mapper.UserRoleRelMapper;
import com.cmit.kapok.system.api.sys_role.UserRoleRelService;
import com.cmit.kapok.system.entity.sys_role.UserRoleRel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by lizhitao on 2019/01/02.
 */
@Service
@Transactional
public class UserRoleRelServiceImpl extends ServiceImpl<UserRoleRelMapper,UserRoleRel> implements UserRoleRelService {
    @Resource
    private UserRoleRelMapper userRoleRelMapper;

    @Override
    public Boolean deleteByUserId(int userId) {
        return userRoleRelMapper.deleteByUserId(userId);
    }

    @Override
    public Boolean updateUserRoleRel(int userId, List<String> rolesList) {
        return userRoleRelMapper.updateUserRoleRel(userId,rolesList);
    }

    @Override
    @Cacheable(value = "userRoles",key = "'userRoles-'+#userName")
    public List<String> queryRolesByUserName(String userName) {
        return userRoleRelMapper.queryRolesByUserName(userName);
    }

}
