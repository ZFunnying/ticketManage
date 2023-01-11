package com.cmit.kapok.system.service.sys_menu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.sys_menu.mapper.H5MenuParentChildrenRelMapper;
import com.cmit.kapok.system.entity.sys_menu.H5MenuParentChildrenRel;
import com.cmit.kapok.system.api.sys_menu.H5MenuParentChildrenRelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by lizhitao on 2019/11/04.
 */
@Service
@Transactional
public class H5MenuParentChildrenRelServiceImpl extends ServiceImpl<H5MenuParentChildrenRelMapper,H5MenuParentChildrenRel> implements H5MenuParentChildrenRelService {
    @Resource
    private H5MenuParentChildrenRelMapper h5MenuParentChildrenRelMapper;

}
