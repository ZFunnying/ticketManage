package com.cmit.kapok.system.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

public class SHA1Util {
    private static Logger logger =  LoggerFactory.getLogger(SHA1Util.class);

    public static String getSHA1HexString(String str) throws Exception {
        // SHA1签名生成
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes());
        byte[] digest = md.digest();

        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();
    }
    public static String getSignature(String appSecret,String nonce,String timespan){
        String[] tempArr = {appSecret,nonce,timespan};
        Arrays.sort(tempArr);
        String strArr = String.join("",tempArr);
        String reStr = "";
        try {
            reStr = SHA1Util.getSHA1HexString(strArr);
        } catch (Exception e) {
            logger.error("trans 2 SHA1 error:"+e.getMessage());
        }
        return reStr.toLowerCase();
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
