package com.cmit.kapok.system.entity.system_logger;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


@Data
public class LoggingEventException {
    @TableId(type = IdType.AUTO)
    private Long eventId;

    @TableId(type = IdType.AUTO)
    private Short i;

    private String traceLine;
}