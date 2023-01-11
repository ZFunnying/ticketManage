package com.cmit.kapok.system.service.sys_menu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.sys_menu.mapper.MenuParentChildrenRelMapper;
import com.cmit.kapok.system.entity.sys_menu.MenuParentChildrenRel;
import com.cmit.kapok.system.api.sys_menu.MenuParentChildrenRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by lizhitao on 2019/01/04.
 */
@Service
@Transactional
public class MenuParentChildrenRelServiceImpl extends ServiceImpl<MenuParentChildrenRelMapper,MenuParentChildrenRel> implements MenuParentChildrenRelService {
    @Resource
    private MenuParentChildrenRelMapper menuParentChildrenRelMapper;

}
