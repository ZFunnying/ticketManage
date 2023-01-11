package com.cmit.kapok.system.controller.system_logger;

import com.cmit.kapok.base.core.Result;
import com.cmit.kapok.base.core.ResultGenerator;
import com.cmit.kapok.base.core.ServiceException;
import com.cmit.kapok.system.api.system_logger.LoggingEventService;
import com.cmit.kapok.system.entity.system_logger.LoggingEvent;
import com.cmit.kapok.system.utils.FileUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhitao on 2019/09/18.
 */
@RestController
@RequestMapping("/loggingevent")
public class LoggingEventController {
    @Resource
    private LoggingEventService loggingEventService;
    @Resource
    private FileUtil fileUtil;
    @Value("${log.file.path}")
    private String logFilePath;
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody LoggingEvent loggingEvent) {
        loggingEventService.save(loggingEvent);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Result delete(@RequestBody LoggingEvent loggingEvent) {
//        loggingEventService.deleteById(loggingEvent.getEventId());
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result update(@RequestBody LoggingEvent loggingEvent) {
        loggingEventService.updateById(loggingEvent);
        return ResultGenerator.genSuccessResult();
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Result detail(@RequestParam Integer id) {
        LoggingEvent loggingEvent = loggingEventService.getById(id);
        return ResultGenerator.genSuccessResult(loggingEvent);
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<LoggingEvent> list = loggingEventService.list();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/queryByCond", method = RequestMethod.POST)
    public Result queryByCond(@RequestBody Map loggingEvent, @RequestParam String pageSize, @RequestParam String pageNum) {
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        if(loggingEvent.containsKey("timeRange") && null != loggingEvent.get("timeRange")){
            List timeRange = (List)loggingEvent.get("timeRange");
            DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                loggingEvent.put("startTime",fmt.parse((String)timeRange.get(0)).getTime());
                loggingEvent.put("endTime",fmt.parse((String)timeRange.get(1)).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<LoggingEvent> list = loggingEventService.findByParam(loggingEvent);
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }

    @RequestMapping(value = "/getLogFileName", method = RequestMethod.GET)
    public Result getLogFileName() {
        List array = new ArrayList();
        boolean result =fileUtil.getFileList(array);
        if(result){
            return ResultGenerator.genSuccessResult(array);
        }else {
            return ResultGenerator.genSuccessResult(new ArrayList<>());
        }

    }
    @RequestMapping(value = "/getLogFileNameOptions", method = RequestMethod.GET)
    public Result getLogFileNameOptions() {
        List array = new ArrayList();
        boolean result =fileUtil.getFileList(array);
        if(result){
            List reList = new ArrayList();
            for(Object name:array){
                Map jsonObject = new HashMap();
                jsonObject.put("label",name);
                jsonObject.put("value",name);
                reList.add(jsonObject);
            }
            return ResultGenerator.genSuccessResult(reList);
        }else {
            return ResultGenerator.genSuccessResult(new ArrayList<>());
        }

    }

    @RequestMapping(value = "/getLogFile", method = RequestMethod.GET)
    public void getLogFile(@RequestParam String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder pathSb = new StringBuilder();
        String path = pathSb.append(this.logFilePath).append(File.separator).append(fileName).toString();
        File file = new File(path);
        File upfile = new File(this.logFilePath);
        if(!file.getCanonicalPath().contains(upfile.getCanonicalPath())){
            throw new ServiceException("路径错误");
        }
//        File file = new File("/Users/lizhitao/Documents/公司项目/冷链/cmit-vue-admin/logs/" + fileName);
        try(InputStream inputStream =  new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();){
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 文件内容的总行数。
    static long getTotalLines(String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName)));
        LineNumberReader reader = new LineNumberReader(in);
        String s = reader.readLine();
        long lines = reader.lines().count();
        reader.close();
        in.close();
        return lines;
    }


    @RequestMapping(value = "/getLogFileContext", method = RequestMethod.GET)
    public Result getLogFileContext(@RequestParam String fileName
            ,@RequestParam(defaultValue = "0") Integer startLine
            ,@RequestParam(defaultValue = "1000") Integer endLine,@RequestParam Boolean ReverseView) throws IOException {
//        StringBuffer sb = new StringBuffer();
//        InputStream is = new FileInputStream("/Users/lizhitao/Documents/公司项目/冷链/cmit-vue-admin/logs/" + fileName);
        StringBuilder pathSb = new StringBuilder();
        String path = pathSb.append(this.logFilePath).append(File.separator).append(fileName).toString();
        File file = new File(path);
        File upfile = new File(this.logFilePath);
        if(!file.getCanonicalPath().contains(upfile.getCanonicalPath())){
            throw new ServiceException("路径错误");
        }
        InputStream is = new FileInputStream(file);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        List<Map> reList = new ArrayList<>();
        if(ReverseView){
            long count = getTotalLines(path);
            int tempstart = (int) (count - endLine);
            int tempend = (int) (count - startLine);
            startLine = tempstart<0?0:tempstart;
            endLine = tempend<0?0:tempend;
        }

        if(startLine.equals(0) && endLine.equals(0)){
            return ResultGenerator.genSuccessResult(reList);
        }

        int i = 0;
        while (line != null) { // 如果 line 为空说明读完了
            if(i<startLine){
                line = reader.readLine();
                i++ ;
                continue;
            }
            if(i>endLine){
                break;
            }
            if(line.contains("---")){
                String time = line.substring(0,23).trim();
                String level = line.substring(23,29).trim();
                String codeLine = line.substring(29,35).trim();
                String thread = line.substring(43,61).trim();
                String method = line.substring(63,104).trim();
                String log = line.substring(105).trim();
                Map reMap = new HashMap();
                reMap.put("time",time);
                reMap.put("level",level);
                reMap.put("codeLine",codeLine);
                reMap.put("thread",thread);
                reMap.put("method",method);
                reMap.put("log",log);
                reList.add(reMap);
            }else {
                Map reMap = new HashMap();
                reMap.put("error",line);
                reMap.put("isError",true);
                reList.add(reMap);
            }
            line = reader.readLine(); // 读取下一行
            i++ ;
        }
        reader.close();
        is.close();
        return ResultGenerator.genSuccessResult(reList);
    }


}
