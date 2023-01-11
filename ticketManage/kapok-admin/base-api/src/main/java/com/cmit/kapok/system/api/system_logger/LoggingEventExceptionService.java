package com.cmit.kapok.system.api.system_logger;
import com.cmit.kapok.system.entity.system_logger.LoggingEventException;

import java.util.List;


/**
 * Created by lizhitao on 2019/11/07.
 */
public interface LoggingEventExceptionService {
    List<LoggingEventException> getException(Integer eventId);
}
