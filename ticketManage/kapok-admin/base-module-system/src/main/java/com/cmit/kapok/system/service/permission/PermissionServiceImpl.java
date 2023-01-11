package com.cmit.kapok.system.service.permission;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.permission.mapper.PermissionMapper;
import com.cmit.kapok.system.entity.permission.Permission;
import com.cmit.kapok.system.api.permission.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by lizhitao on 2019/03/11.
 */
@Service
@Transactional
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper,Permission> implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

}
