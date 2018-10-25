package com.maozhen.sso.service;

import com.baomidou.mybatisplus.service.IService;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.model.User;

/**
 * 用户模块服务
 * Created by HuangChao on 2018/8/1.
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @return
     */
    User login(String username, String password);

    /**
     * 保存用户对应的角色
     *
     * @param roleIds
     * @param user
     * @return
     */
    boolean saveUserRole(Long[] roleIds, User user);

    /**
     * 判断角色是否是否被用户使用
     *
     * @param roleId
     * @return
     */
    boolean hasUserByRoleId(Long roleId);

    /**
     * 删除用户对应的角色
     *
     * @param userId
     * @return
     */
    boolean deleteUserRole(Long userId);

	ResponseModel loginWithChannel(String username, String password, String loginChannel);

	ResponseModel logout(String token);

	User getInfo(String header);

	boolean updateCacheUser(String header, String token);

}
