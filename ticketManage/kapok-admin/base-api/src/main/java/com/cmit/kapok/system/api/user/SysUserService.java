package com.cmit.kapok.system.api.user;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.user.SysUser;
import com.cmit.kapok.system.entity.user.User;

import java.util.List;
import java.util.Map;


/**
 * Created by lizhitao on 2019/01/02.
 */
public interface SysUserService extends IService<SysUser> {
  User queryById(int id);
  List<Map> findAllIdAndUsername();
  String getNameById(int id);
  int checkOldPassword(int id,String password);

}
