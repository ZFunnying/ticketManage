package com.cmit.kapok.system.service.system_logger.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cmit.kapok.system.entity.system_logger.LoggingEvent;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;

public interface LoggingEventMapper extends BaseMapper<LoggingEvent> {
    List<LoggingEvent> findByParam(@Param("params") Map params);
}