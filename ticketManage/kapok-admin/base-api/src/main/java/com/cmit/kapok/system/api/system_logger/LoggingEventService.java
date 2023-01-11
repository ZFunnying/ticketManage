package com.cmit.kapok.system.api.system_logger;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cmit.kapok.system.entity.system_logger.LoggingEvent;

import java.util.List;
import java.util.Map;


/**
 * Created by lizhitao on 2019/09/18.
 */
public interface LoggingEventService extends IService<LoggingEvent> {
    List<LoggingEvent> findByParam(Map params);
}
