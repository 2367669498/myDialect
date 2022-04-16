package com.zheng.Utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * */
public final class MD5 {

//    public static String encrypt(String strSrc) {
//        try {
//            char hexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
//                    '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//            byte[] bytes = strSrc.getBytes();
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(bytes);
//            bytes = md.digest();
//            int j = bytes.length;
//            char[] chars = new char[j * 2];
//            int k = 0;
//            for (int i = 0; i < bytes.length; i++) {
//                byte b = bytes[i];
//                chars[k++] = hexChars[b >>> 4 & 0xf];
//                chars[k++] = hexChars[b & 0xf];
//            }
//            return new String(chars);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//            throw new RuntimeException("MD5加密出错！！+" + e);
//        }
//    }

    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String encrypt(String inStr){

        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++){
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }
    public static void main(String[] args) {
        System.out.println(MD5.encrypt("123456"));
        System.out.println(MD5.encrypt("EFG@AB"));
    }

}
