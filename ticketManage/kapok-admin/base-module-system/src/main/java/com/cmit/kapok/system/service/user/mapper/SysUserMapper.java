package com.cmit.kapok.system.service.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.user.SysUser;
import com.cmit.kapok.system.entity.user.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SysUserMapper extends BaseMapper<SysUser> {
  User queryById(@Param("id") int id);

  @Select("select id as value,user_name as label from sys_user")
  List<Map> queryAllIdAndUsername();

  @Select("select user_name from sys_user where id=#{id}")
  String getNameById(@Param("id") int id);

  @Select("select count(1) from sys_user where id=#{id} and password=#{password}")
  int checkOldPassword(@Param("id")int id,@Param("password")String password);

  SysUser getUserInfoByToken(@Param("token")String token);
}