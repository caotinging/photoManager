package com.maozhen.sso.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.maozhen.sso.dao.PermissionMapper;
import com.maozhen.sso.model.Permission;
import com.maozhen.sso.service.PermissionService;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
