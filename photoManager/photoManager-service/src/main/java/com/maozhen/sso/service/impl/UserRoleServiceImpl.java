package com.maozhen.sso.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.maozhen.sso.dao.UserRoleMapper;
import com.maozhen.sso.model.UserRole;
import com.maozhen.sso.service.UserRoleService;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
