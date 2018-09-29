package com.wind.control.okhttp.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;


/**
 * Base64编码，解码工具
 * Created by luow on 2018/1/4.
 */

public class Base64Utils {
    private static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * 字节直接编码，返回字节，默认编码方式
     *
     * @param bytes
     * @return
     */
    public static byte[] encode(byte[] bytes) {
        return encode(bytes, Base64.DEFAULT);
    }

    /**
     * 字节直接编码，返回字节，指定编码方式
     *
     * @param bytes
     * @param flags
     * @return
     */
    public static byte[] encode(byte[] bytes, int flags) {
        return Base64.encode(bytes, flags);
    }


    /**
     * 对明文编码，返回字节，默认编码方式
     *
     * @param plaintext
     */
    public static byte[] encode(String plaintext) {
        return encode(plaintext, Base64.DEFAULT);
    }

    /**
     * 对明文编码，返回字节，指定编码方式
     *
     * @param plaintext
     */
    public static byte[] encode(String plaintext, int flags) {
        try {
            return encode(plaintext.getBytes(CHARSET_UTF_8), flags);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 字节直接编码，返回字符串，指定编码方式
     *
     * @param bytes
     * @param flags
     * @return
     */
    public static String encodeToString(byte[] bytes, int flags) {
        try {
            return new String(encode(bytes, flags), CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对明文编码，返回字符串，指定编码方式
     *
     * @param plaintext
     * @param flags
     * @return
     */
    public static String encodeToString(String plaintext, int flags) {
        try {
            return encodeToString(plaintext.getBytes(CHARSET_UTF_8), flags);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节直接解码，返回字节，默认编码方式
     *
     * @param bytes
     * @return
     */
    public static byte[] decode(byte[] bytes) {
        return decode(bytes, Base64.DEFAULT);
    }

    /**
     * 字节直接解码，返回字节，指定编码方式
     *
     * @param bytes
     * @param flags
     * @return
     */
    public static byte[] decode(byte[] bytes, int flags) {
        return Base64.decode(bytes, flags);
    }

    /**
     * 对明文（已经被Base64编码过了）解码，返回字节，默认编码方式
     *
     * @param plaintext
     */
    public static byte[] decode(String plaintext) {
        return decode(plaintext, Base64.DEFAULT);
    }

    /**
     * 对明文（已经被Base64编码过了）解码，返回字节，指定编码方式
     *
     * @param plaintext
     */
    public static byte[] decode(String plaintext, int flags) {
        try {
            return decode(plaintext.getBytes(CHARSET_UTF_8), flags);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 字节直接编码，返回字符串，指定编码方式
     *
     * @param bytes
     * @param flags
     * @return
     */
    public static String decodeToString(byte[] bytes, int flags) {
        try {
            return new String(Base64.decode(bytes, flags), CHARSET_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对明文编码，返回字符串，指定编码方式
     *
     * @param plaintext
     * @param flags
     * @return
     */
    public static String decodeToString(String plaintext, int flags) {
        try {
            return decodeToString(plaintext.getBytes(CHARSET_UTF_8), flags);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
