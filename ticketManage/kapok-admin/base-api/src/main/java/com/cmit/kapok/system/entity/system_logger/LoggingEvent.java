package com.cmit.kapok.system.entity.system_logger;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class LoggingEvent {
    @TableId(type = IdType.AUTO)
    private Long eventId;

    private Long timestmp;

    private String loggerName;

    private String levelString;

    private String threadName;

    private Short referenceFlag;

    private String arg0;

    private String arg1;

    private String arg2;

    private String arg3;

    private String callerFilename;

    private String callerClass;

    private String callerMethod;

    private String callerLine;

    private String formattedMessage;
}