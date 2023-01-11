package com.cmit.kapok.system.utils;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PasswordUtil {
    public static String encryptionCode(String code,Object salt) {
        int hashIterations = 2;//加密的次数
        Object credentials = code;//密码
        String hashAlgorithmName = "SHA-256";//加密方式
        Object simpleHash = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
        return ((SimpleHash) simpleHash).toHex();
    }
    public static String getRandomString(int length) {
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i) {
            //产生0-61的数字
            int number = random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

}
