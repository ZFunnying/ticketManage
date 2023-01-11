package com.cmit.kapok.system.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @author lizhitao
 */
@Component
public class FileUtil {

    @Value("${log.file.path}")
    private String logFilePath;

    public boolean getFileList(List<String> reList){
        return this.getFileList(this.logFilePath,null,reList);
//        return this.getFileList("/Users/lizhitao/Documents/公司项目/冷链/cmit-vue-admin/logs/",null,reList);
    }

    public static List<Map> genDirTree(String path, int level, String dir) {
        level++;
        File file = new File(path);
        File[] files = file.listFiles();
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        List<Map> reList = new ArrayList<>();
        if (files.length != 0) {
            for (File f : files) {
                Map reMap = new HashMap();
                if (f.isDirectory()) {
                    dir = f.getName();
                    reMap.put("path",f.getAbsolutePath());
                    reMap.put("type","dir");
                    reMap.put("name",f.getName());
                    reMap.put("children",genDirTree(f.getAbsolutePath(), level, dir));
                    reList.add(reMap);
                } else {
                    reMap.put("path",f.getAbsolutePath());
                    reMap.put("type","file");
                    reMap.put("name",f.getName());
                    reList.add(reMap);
                }
            }
        }
        return reList;
    }

    private boolean getFileList(String path,String dirName,List<String> reList) {
        File dirFile = new File(path);
        if (dirFile.exists()) {
            File[] files = dirFile.listFiles();
            if (files != null) {
                for (File fileChildDir : files) {
                    if (fileChildDir.isDirectory()) {
                        //通过递归的方式,可以把目录中的所有文件全部遍历出来
                        getFileList(fileChildDir.getAbsolutePath(),fileChildDir.getName(),reList);
                    }
                    if (fileChildDir.isFile()) {
                        if(null != dirName){
                            reList.add(dirName + "/" + fileChildDir.getName());
                        }else {
                            reList.add(fileChildDir.getName());
                        }

                    }
                }
            }
            Collections.sort(reList,Collections.reverseOrder());
            return true;
        }else{
            return false;
        }
    }
}
