package com.cmit.kapok.system.service.system_logger;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cmit.kapok.system.service.system_logger.mapper.LoggingEventMapper;
import com.cmit.kapok.system.api.system_logger.LoggingEventService;
import com.cmit.kapok.system.entity.system_logger.LoggingEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * Created by lizhitao on 2019/09/18.
 */
@Service
@Transactional
public class LoggingEventServiceImpl extends ServiceImpl<LoggingEventMapper,LoggingEvent> implements LoggingEventService {
    @Resource
    private LoggingEventMapper loggingEventMapper;

    @Override
    public List<LoggingEvent> findByParam(Map params) {
        return loggingEventMapper.findByParam(params);
    }
}
