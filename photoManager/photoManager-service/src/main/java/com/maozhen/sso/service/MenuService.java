package com.maozhen.sso.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.maozhen.sso.model.Menu;

/**
 * 菜单模块服务
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取角色选中的菜单编号列表
     *
     * @param roleId
     * @return
     */
    List<Long> getMenuIdsByRole(Long roleId);

    /**
     * 根据权限获取菜单列表
     *
     * @param param
     * @return
     */
    List<Menu> getMenusByRole(Map<String, Object> param);

    /**
     * 获取所有的接口列表
     *
     * @return
     */
    List<Menu> getApiList();

}
