package com.maozhen.sso.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.maozhen.sso.model.Role;

/**
 * 角色模块服务
 * Created by HuangChao on 2018/8/1.
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据用户获取角色列表
     *
     * @param userId
     * @return
     */
    List<Role> getRolesByUser(Long userId);

    /**
     * 分配菜单权限
     *
     * @param menuIds 菜单集合
     * @param roleId  角色编号
     * @param token   登录令牌
     * @return
     */
    boolean allotMenus(Long[] menuIds, Long roleId, String token);

}
