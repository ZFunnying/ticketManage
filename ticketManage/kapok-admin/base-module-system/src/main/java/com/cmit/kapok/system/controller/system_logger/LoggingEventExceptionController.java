package com.cmit.kapok.system.controller.system_logger;

import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.system.api.system_logger.LoggingEventExceptionService;
import com.cmit.kapok.system.entity.system_logger.LoggingEventException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by lizhitao on 2019/11/07.
*/
@RestController
@RequestMapping("/loggingeventexception")
public class LoggingEventExceptionController {
    @Resource
    private LoggingEventExceptionService loggingEventExceptionService;

    @RequestMapping(value = "/getException", method = RequestMethod.GET)
    public Result getException(@RequestParam Integer eventId) {
        List<LoggingEventException> list = loggingEventExceptionService.getException(eventId);
        return ResultGenerator.genSuccessResult(list);
    }

}
