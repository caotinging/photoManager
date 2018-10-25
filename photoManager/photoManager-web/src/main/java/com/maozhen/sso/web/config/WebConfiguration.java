package com.maozhen.sso.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.maozhen.sso.common.utils.RedisCacheUtil;
import com.maozhen.sso.common.utils.SpringUtil;
import com.maozhen.sso.web.filter.SessionFilter;

@Configuration
public class WebConfiguration {
    @Bean
    public SpringUtil buildSpringUtils() {
        SpringUtil springUtil = new SpringUtil();
        return springUtil;
    }

    /**
     * 配置token检查过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean FilterRegistration() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new SessionFilter());
        return bean;
    }
}
