package com.maozhen.sso.common.utils;

import java.util.List;

/**
 * 用户操作工具
 */
public class UserUtil {

    /**
     * 获取缓存对象
     */
    static RedisCacheUtil redisUtils = SpringUtil.getBean(RedisCacheUtil.class);

    /**
     * 获取登录用户
     *
     * @param token
     * @return
     */
    public static Object getLoginUser(String token) {
        return redisUtils.get(token);
    }

}
