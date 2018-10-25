package com.maozhen.sso.common.ex;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <p>
 * 自定义异常处理类
 * </p>
 *
 * @author caoting
 * @date 2018年7月10日
 */
public class BaseException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 异常粗略信息
     */
    private String message;

    /**
     * 返回简单报错信息
     *
     * @return
     * @author caoting
     * @date 2018年7月13日
     */
    public String getSimpleMessage() {
        return this.message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    /**
     * 消息是否为属性文件中的Key
     */
    private boolean propertiesKey = true;

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     */
    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public BaseException(String errorCode, String message) {
        this(errorCode, message, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public BaseException(String errorCode, String message, Throwable cause) {
        this(errorCode, message, cause, true);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode     错误编码
     * @param message       信息描述
     * @param propertiesKey 消息是否为属性文件中的Key
     */
    public BaseException(String errorCode, String message, boolean propertiesKey) {
        super(message);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
        this.setMessage(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param errorCode 错误编码
     * @param message   信息描述
     */
    public BaseException(String errorCode, String message, Throwable cause, boolean propertiesKey) {
        super(message, cause);
        this.setErrorCode(errorCode);
        this.setPropertiesKey(propertiesKey);
        this.setMessage(message);
    }

    /**
     * 构造一个基本异常.
     *
     * @param message 信息描述
     * @param cause   根异常类（可以存入任何异常）
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.setMessage(message);
    }

    public Integer getErrorCode() {
        return Integer.parseInt(errorCode);
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isPropertiesKey() {
        return propertiesKey;
    }

    public void setPropertiesKey(boolean propertiesKey) {
        this.propertiesKey = propertiesKey;
    }

    /**
     * 收集异常堆栈信息
     */
    public static String collectExceptionStackMsg(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw, true));
        String strs = sw.toString();
        return strs;
    }
}