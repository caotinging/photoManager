package com.maozhen.sso.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.common.utils.RedisCacheUtil;
import com.maozhen.sso.common.utils.UserUtil;
import com.maozhen.sso.common.utils.UuidUtil;
import com.maozhen.sso.dao.UserMapper;
import com.maozhen.sso.model.Menu;
import com.maozhen.sso.model.Role;
import com.maozhen.sso.model.User;
import com.maozhen.sso.model.UserRole;
import com.maozhen.sso.service.MenuService;
import com.maozhen.sso.service.RoleService;
import com.maozhen.sso.service.UserRoleService;
import com.maozhen.sso.service.UserService;

/**
 * 用户模块服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    private RedisCacheUtil redisUtils;//缓存工具

    @Autowired
    RoleService roleService;

    @Autowired
    MenuService menuService;

    @Override
    public User login(String username, String password) {
        Wrapper<User> entityWrapper = new EntityWrapper<User>();
        entityWrapper.eq("username", username).eq("password", password).eq("status", 0).eq("is_deleted", 0);
        return selectOne(entityWrapper);
    }

    @Override
    public boolean saveUserRole(Long[] roleIds, User user) {
        //先删除原来的菜单权限
        Wrapper<UserRole> entityWrapper = new EntityWrapper<UserRole>();
        entityWrapper.eq("user_id", user.getId());
        userRoleService.delete(entityWrapper);
        //再重新赋值
        List<UserRole> userRoles = Lists.newArrayList();
        for (int i = 0; i < roleIds.length; i++) {
            UserRole userRole = new UserRole();
            userRole.setToken(user.getToken());
            userRole.setRoleId(roleIds[i]);
            userRole.setUserId(user.getId());
            userRole.preInsert();
            userRoles.add(userRole);
        }
        return userRoleService.insertBatch(userRoles);
    }

    @Override
    public boolean hasUserByRoleId(Long roleId) {
        Wrapper<UserRole> entityWrapper = new EntityWrapper<UserRole>();
        entityWrapper.eq("role_id", roleId);
        List<UserRole> list = userRoleService.selectList(entityWrapper);
        if (list.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUserRole(Long userId) {
        Wrapper<UserRole> entityWrapper = new EntityWrapper<UserRole>();
        entityWrapper.eq("user_id", userId);
        UserRole userRole = new UserRole();
        userRole.setIsDeleted(UserRole.STATUS_DELETE);
        return userRoleService.update(userRole, entityWrapper);
    }

	@Override
	public ResponseModel loginWithChannel(String username, String password, String loginChannel) {
		Map<String, Object> map = Maps.newConcurrentMap();
		ResponseModel ret = ResponseModel.getSuccessResponseModel();
		User user = this.login(username, password);
		
		if (null != user) {
			// 查询用户所对应的角色
			List<Role> roles = roleService.getRolesByUser(user.getId());
			user.setRoles(roles);
			// 查询用户所对应的菜单
			if (user.isAdmin()) {
				// 管理员绕开权限限制
				Wrapper<Menu> menu = new EntityWrapper<Menu>();
				menu.eq("is_deleted", 0);// 设置查询条件
				List<Menu> menus = menuService.selectList(menu);// 查询根节点数据
				user.setMenus(menus);
			} else {
				// 按照用户的权限查询所属的菜单
				Map<String, Object> paramRole = Maps.newConcurrentMap();
				paramRole.put("roles", roles);
				paramRole.put("id", 0);// 从根目录获取数据
				List<Menu> menus = menuService.getMenusByRole(paramRole);
				user.setMenus(menus);
			}
			String token = UuidUtil.get32UUID();
			user.setToken(token);
			user.setPassword("");// 清空密码
			redisUtils.set(token, user);// 存储token永久
			redisUtils.set(username, token);
			
			map.put("token", user.getToken());
			ret.setData(map);// 返回登录数据
		} else {
			ret = ResponseModel.getFailedResponseModel().setMessage("账号或密码错误");
		}
		return ret;
	}
	
	@Override
	public ResponseModel logout(String token) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        User user = getUserByToken(token);
        if (null != user) {
            redisUtils.delete(user.getUsername());
        }
        redisUtils.delete(token);
        return ret;
    }

    public User getUserByToken(String token) {
        Object user = redisUtils.get(token);
        return user == null ? null : (User) user;
    }

    public String getTokenByUserName(String username) {
        Object token = redisUtils.get(username);
        return token == null ? null : (String) token;
    }

    @Override
    public User getInfo(String token) {
        Object object = UserUtil.getLoginUser(token);
        if (null != object) {
            return (User) object;
        }
        return null;
    }
    
    public boolean updateCacheUser(String loginChannel, String token) {
        User user = getUserByToken(token);
        if (null != user) {
            //查询用户所对应的角色
            List<Role> roles = roleService.getRolesByUser(user.getId());
            user.setRoles(roles);
            //查询用户所对应的菜单
            if (user.isAdmin()) {
                //管理员绕开权限限制
                Wrapper<Menu> menu = new EntityWrapper<Menu>();
                menu.eq("is_deleted", 0);//设置查询条件
                List<Menu> menus = menuService.selectList(menu);//查询根节点数据
                user.setMenus(menus);
            } else {
                //按照用户的权限查询所属的菜单
                Map<String, Object> paramRole = Maps.newConcurrentMap();
                paramRole.put("roles", roles);
                paramRole.put("id", 0);//从根目录获取数据
                List<Menu> menus = menuService.getMenusByRole(paramRole);
                user.setMenus(menus);
            }
            user.setToken(token);
            user.setPassword("");//清空密码
            
            redisUtils.set(token, user);//token存储永久
            redisUtils.set(user, token);
        }
        return false;
    }

}
