package com.cmit.kapok.system.service.system_logger;

import com.cmit.kapok.system.service.system_logger.mapper.LoggingEventExceptionMapper;
import com.cmit.kapok.system.api.system_logger.LoggingEventExceptionService;
import com.cmit.kapok.system.entity.system_logger.LoggingEventException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by lizhitao on 2019/11/07.
 */
@Service
@Transactional
public class LoggingEventExceptionServiceImpl implements LoggingEventExceptionService {
    @Resource
    private LoggingEventExceptionMapper loggingEventExceptionMapper;

    @Override
    public List<LoggingEventException> getException(Integer eventId) {
        return loggingEventExceptionMapper.getException(eventId);
    }
}
