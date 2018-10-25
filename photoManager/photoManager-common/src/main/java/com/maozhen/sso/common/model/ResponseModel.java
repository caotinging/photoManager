package com.maozhen.sso.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import java.io.Serializable;

public class ResponseModel implements Serializable {
    private static final long serialVersionUID = -93744850105709929L;

    private Integer code;
    private String message;
    private Object data;

    public static ResponseModel getSuccessResponseModel() {
        return new ResponseModel().setCode(200).setMessage("操作成功");
    }

    public static ResponseModel getFailedResponseModel() {
        return new ResponseModel().setCode(500).setMessage("操作失败，请联系系统管理员！");
    }

    public static ResponseModel getLoginErrorResponseModel() {
        return new ResponseModel().setCode(401).setMessage("auth_error");
    }

    public ResponseModel() {
    }

    public ResponseModel(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public ResponseModel setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public ResponseModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        if(null == this.data){
            return Maps.newConcurrentMap();
        }
        return this.data;
    }

    public ResponseModel setData(Object data) {
        this.data = data;
        return this;
    }

    public String toString() {
        try {
            return "ResponseModel{code='" + this.code + '\'' + ", message='" + this.message + '\'' + ", data=" + new ObjectMapper().writeValueAsString(this.data) + '}';
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}