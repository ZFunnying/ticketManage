package com.cmit.kapok.system.service.sys_role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.sys_role.UserRoleRel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserRoleRelMapper extends BaseMapper<UserRoleRel> {
    @Delete("delete from user_role_rel where user_id = #{userId}")
    Boolean deleteByUserId(@Param("userId") int userId);

    Boolean updateUserRoleRel(@Param("userId") int userId,@Param("rolesList") List<String> rolesList);

    @Select("select role.role from user_role_rel urr left join sys_user su on urr.user_id=su.id " +
            "left join sys_role role on role.id=urr.role_id where su.user_name=#{userName}")
    List<String> queryRolesByUserName(@Param("userName")String userName);
}