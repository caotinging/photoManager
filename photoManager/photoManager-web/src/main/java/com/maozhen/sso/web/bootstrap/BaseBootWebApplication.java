package com.maozhen.sso.web.bootstrap;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.maozhen.sso.web.config.CustomMapper;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "com.maozhen.sso.web")
@ComponentScan("com.maozhen.sso")
@MapperScan("com.maozhen.sso.dao")
@EnableAutoConfiguration
public class BaseBootWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseBootWebApplication.class, args);
    }

    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("102400000KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("102400000KB");
        return factory.createMultipartConfig();
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new CustomMapper());
        return converter;
    }
}
