package com.maozhen.sso.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.maozhen.sso.model.Menu;

public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据权限获取菜单
     *
     * @param param
     * @return
     */
    List<Menu> getMenusByRole(Map<String, Object> param);

}
