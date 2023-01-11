package com.cmit.kapok.system.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.utils.TokenUtil;
import com.cmit.kapok.system.api.user.SysUserService;
import com.cmit.kapok.system.service.user.mapper.SysUserMapper;
import com.cmit.kapok.system.entity.user.SysUser;
import com.cmit.kapok.system.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by lizhitao on 2019/01/02.
 */
@Service
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {
    private final Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private SysUserMapper sysUserMapper;
    @Autowired
    private TokenUtil tokenUtil;
    @Override
    public User queryById(int id){
        return sysUserMapper.queryById(id);
    }

    @Override
    public List<Map> findAllIdAndUsername(){
        return sysUserMapper.queryAllIdAndUsername();
    }

    @Override
    public String getNameById(int id){
        return sysUserMapper.getNameById(id);
    }

    @Override
    public int checkOldPassword(int id, String password) {
        return sysUserMapper.checkOldPassword(id,password);
    }

}
