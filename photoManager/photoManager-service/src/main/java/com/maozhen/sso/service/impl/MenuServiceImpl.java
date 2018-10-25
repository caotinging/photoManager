package com.maozhen.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.maozhen.sso.dao.MenuMapper;
import com.maozhen.sso.model.Menu;
import com.maozhen.sso.model.Permission;
import com.maozhen.sso.service.MenuService;
import com.maozhen.sso.service.PermissionService;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    PermissionService permissionService;

    @Override
    public List<Long> getMenuIdsByRole(Long roleId) {
        List<Long> menuIds = Lists.newArrayList();
        Wrapper<Permission> entity = new EntityWrapper<Permission>();
        entity.eq("is_deleted", 0).eq("role_id", roleId);
        List<Permission> list = permissionService.selectList(entity);
        for (int i = 0; i < list.size(); i++) {
            menuIds.add(list.get(i).getMenuId());
        }
        return menuIds;
    }

    @Override
    public List<Menu> getMenusByRole(Map<String, Object> param) {
        return baseMapper.getMenusByRole(param);
    }

    @Override
    public List<Menu> getApiList() {
        Wrapper<Menu> entity = new EntityWrapper<Menu>();
        entity.eq("is_deleted", 0).eq("menu_type", 2);
        return baseMapper.selectList(entity);
    }
}
