package com.cmit.kapok.system.api.sys_role;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.sys_role.UserRoleRel;

import java.util.List;


/**
 * Created by lizhitao on 2019/01/02.
 */
public interface UserRoleRelService extends IService<UserRoleRel> {
    Boolean deleteByUserId(int userId);
    Boolean updateUserRoleRel(int userId,List<String> rolesList);
    List<String> queryRolesByUserName(String userName);
}
