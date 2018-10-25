package com.maozhen.sso.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.maozhen.sso.dao.RoleMapper;
import com.maozhen.sso.model.Permission;
import com.maozhen.sso.model.Role;
import com.maozhen.sso.service.PermissionService;
import com.maozhen.sso.service.RoleService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    PermissionService permissionService;

    @Override
    public List<Role> getRolesByUser(Long userId) {
        return baseMapper.getRolesByUser(userId);
    }

    @Override
    public boolean allotMenus(Long[] menuIds, Long roleId, String token) {
        //先删除原来的菜单权限
        Wrapper<Permission> entityWrapper = new EntityWrapper<Permission>();
        entityWrapper.eq("role_id", roleId);
        permissionService.delete(entityWrapper);
        //再重新赋值
        List<Permission> permissions = Lists.newArrayList();
        for (int i = 0; i < menuIds.length; i++) {
            Permission permission = new Permission();
            permission.setToken(token);
            permission.setRoleId(roleId);
            permission.setMenuId(menuIds[i]);
            permission.preInsert();
            permissions.add(permission);
        }
        return permissionService.insertBatch(permissions);
    }
}
