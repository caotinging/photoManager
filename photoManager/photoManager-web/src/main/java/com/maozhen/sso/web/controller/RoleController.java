package com.maozhen.sso.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.model.Role;
import com.maozhen.sso.service.RoleService;
import com.maozhen.sso.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/role")
@Api(value = "角色模块")
public class RoleController {

	@Autowired
    private RoleService roleService;//角色服务接口

	@Autowired
    private UserService userService;//用户服务接口

    @ApiOperation(value = "角色列表", notes = "角色列表接口", produces = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseModel list(Role role) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            //装载查询条件
            Wrapper<Role> entity = new EntityWrapper<Role>();
            entity.eq("is_deleted", 0).where("id!={0}", 1);//过滤掉超级管理员
            Page<Role> page = roleService.selectPage(new Page<Role>(role.getCurrentPage(), role.getPageSize()), entity);
            ret.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "角色新增", notes = "角色新增接口", produces = "application/json")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseModel create(Role role, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("新增成功");
        try {
            //设置登录用户token
            role.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行新增初始化操作
            role.preInsert();
            boolean result = roleService.insert(role);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "角色修改", notes = "角色修改接口", produces = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseModel update(Role role, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("修改成功");
        try {
            //设置登录用户token
            role.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行修改初始化操作
            role.preUpdate();
            boolean result = roleService.updateById(role);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "角色删除", notes = "角色删除接口", produces = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseModel delete(Role role) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("删除成功");
        try {
            //判断角色是否被使用
            boolean flag = userService.hasUserByRoleId(role.getId());
            if (flag) {
                ret = ResponseModel.getFailedResponseModel().setMessage("删除失败：【该角色已被某用户关联】");
                return ret;
            }
            role.setIsDeleted(Role.STATUS_DELETE);
            boolean result = roleService.updateById(role);//逻辑删除
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "分配菜单权限", notes = "分配菜单权限接口", produces = "application/json")
    @RequestMapping(value = "/allot", method = RequestMethod.POST)
    public ResponseModel allot(Long[] menuIds, Long roleId, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("分配成功");
        try {
            boolean result = roleService.allotMenus(menuIds, roleId, request.getHeader(Constant.USER_TOKEN));
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("分配失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

}
