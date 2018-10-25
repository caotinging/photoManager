package com.maozhen.sso.web.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.common.utils.DateUtil;
import com.maozhen.sso.common.utils.RedisCacheUtil;
import com.maozhen.sso.common.utils.SpringUtil;
import com.maozhen.sso.common.utils.StringUtil;
import com.maozhen.sso.model.OperateLog;
import com.maozhen.sso.model.User;

/**
 * 接口过滤器
 */
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String contextPath = httpServletRequest.getContextPath();
        String uri = httpServletRequest.getRequestURI();
        String method = httpServletRequest.getMethod();
        String token = httpServletRequest.getHeader(Constant.USER_TOKEN);//登录令牌
        String loginChannel = httpServletRequest.getHeader(Constant.LOGIN_CHANNEL);//渠道来源
        if (loginChannel == null) {
            loginChannel = httpServletRequest.getParameter("loginChannel");
        }
        User user = null;//当前登录用户

        if (((method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("GET")) &&
                (uri.equalsIgnoreCase(contextPath + "/pushMessage") ||
                        uri.equalsIgnoreCase(contextPath + "/swagger-ui.html") ||
                        uri.equalsIgnoreCase(contextPath + "/swagger-resources") ||
                        uri.indexOf("api") > -1) ||
                uri.indexOf("webjars") > -1)) {

        } else {
            RedisCacheUtil redisUtils = SpringUtil.getBean(RedisCacheUtil.class);
            Object obj = redisUtils.get(token);
            if (null == obj) {
                returnResponse(servletResponse, "403", "请重新登录");
                return;
            } else {
                user = (User) obj;//获取登录用户对象
                //延时
                redisUtils.set(token, user);
                //判断非管理员显示功能权限的使用
                if (!user.isAdmin()) {
                    //限制功能使用权限
                    boolean isOk = false;
                    if (null != user.getMenus()) {
                        for (int i = 0; i < user.getMenus().size(); i++) {
                            if (uri.endsWith(user.getMenus().get(i).getUrl())) {
                                isOk = true;
                                break;
                            }
                        }
                    }
                    if (!isOk) {
                        //没有访问权限
                        returnResponse(servletResponse, "401", "接口访问受限");
                        return;
                    }
                }
            }
        }
        //记录访问日志
        try {
            if (null != user) {
                OperateLog operateLog = new OperateLog();
                operateLog.setCreatorUserId(user.getId());
                operateLog.setCreator(user.getChineseName());
                operateLog.setLogType(1);
                operateLog.setIp(StringUtil.getRemoteAddr(httpServletRequest));
                operateLog.setIsDeleted(0);
                operateLog.setGmtCreate(DateUtil.format(new Date(), DateUtil.DATE_TIME_PATTERN));
                operateLog.setGmtModified(DateUtil.format(new Date(), DateUtil.DATE_TIME_PATTERN));
                operateLog.setUri(uri);
                LogUtil logUtil = SpringUtil.getBean(LogUtil.class);
                logUtil.saveLog(operateLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    HttpServletResponse returnResponse(ServletResponse servletResponse, String code, String message) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        Map<String, Object> returnmap = new HashMap<String, Object>();
        returnmap.put("code", Integer.valueOf(code));
        returnmap.put("message", message);
        httpResponse.getWriter().write(JSON.toJSONString(returnmap));
        return httpResponse;
    }

    @Override
    public void destroy() {

    }
}
