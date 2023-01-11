package com.cmit.kapok.system.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {
  @TableId(type = IdType.AUTO)
  private Integer id;

  private String userName;

  private String email;

  private String password;

  private String salt;

  private String token;

  private String avatar;

  private String introduction;

  private Byte state;

  private String role;

  private String roleId;

  private String realName;
}
