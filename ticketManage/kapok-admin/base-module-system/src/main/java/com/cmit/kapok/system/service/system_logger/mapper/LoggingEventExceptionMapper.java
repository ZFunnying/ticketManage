package com.cmit.kapok.system.service.system_logger.mapper;

import com.cmit.kapok.system.entity.system_logger.LoggingEventException;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LoggingEventExceptionMapper {
    @Select("select event_id as eventId,i,trace_line as traceLine from logging_event_exception where event_id=#{eventId} order by i")
    List<LoggingEventException> getException(@Param("eventId") Integer eventId);
}