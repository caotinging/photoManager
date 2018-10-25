package com.maozhen.sso.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author caoting
 * @date 2018年7月20日
 */
// 继承 ObjectMapper 类
public class CustomMapper extends ObjectMapper {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CustomMapper() {
        this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        // 设置 SerializationFeature.FAIL_ON_EMPTY_BEANS 为 false
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}