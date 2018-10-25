package com.maozhen.sso.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.common.utils.UserUtil;
import com.maozhen.sso.model.User;
import com.maozhen.sso.service.RoleService;
import com.maozhen.sso.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@Api(value = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;//用户服务接口

    @Autowired
    private RoleService roleService;//角色服务接口

    @ApiOperation(value = "用户列表", notes = "用户列表接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", dataType = "String", required = true, paramType = "query")
    })
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseModel list(User user, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            //装载查询条件
            Wrapper<User> entity = new EntityWrapper<User>();
            entity.eq("is_deleted", 0);
            if (null != user.getUsername() && !"".equals(user.getUsername())) {
                entity.like("username", user.getUsername());
            }
            if (null != user.getChineseName() && !"".equals(user.getChineseName())) {
                entity.like("chinese_name", user.getChineseName());
            }
            if (null != user.getMobilePhone() && !"".equals(user.getMobilePhone())) {
                entity.like("mobile_phone", user.getMobilePhone());
            }
            //数据范围过滤
            Object object = UserUtil.getLoginUser(request.getHeader(Constant.USER_TOKEN));
            if (null != object) {
                User loginUser = (User) object;
            }
            Page<User> page = userService.selectPage(new Page<User>(user.getCurrentPage(), user.getPageSize()), entity);
            List<User> users = Lists.newArrayList();
            //查询用户对应的角色
            for (int i = 0; i < page.getRecords().size(); i++) {
                User item = page.getRecords().get(i);
                item.setRoles(roleService.getRolesByUser(item.getId()));
                users.add(item);
            }
            page.setRecords(users);
            ret.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "用户新增", notes = "用户新增接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "chineseName", value = "姓名", dataType = "String", required = true, paramType = "query")
    })
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseModel create(User user, Long[] roleIds, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("新增成功");
        try {
            //设置初始密码
            user.setPassword("888888");
            //设置登录用户token
            user.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行新增初始化操作
            user.preInsert();
            userService.insert(user);
            user.setId(user.getId());
            boolean result = true;
            //保存用户对应的角色
            if (roleIds.length > 0) {
                result = userService.saveUserRole(roleIds, user);
            }
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "用户修改", notes = "用户修改接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "chineseName", value = "姓名", dataType = "String", required = true, paramType = "query")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseModel update(User user, Long[] roleIds, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("修改成功");
        try {
            //设置登录用户token
            user.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行修改初始化操作
            user.preUpdate();
            boolean result = userService.updateById(user);
            //保存用户对应的角色
            if (roleIds.length > 0) {
                result = userService.saveUserRole(roleIds, user);
            }
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "用户删除", notes = "用户删除接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户编号", dataType = "String", required = true, paramType = "query")
    })
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseModel delete(User user) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("删除成功");
        try {
            //删除用户对应的角色
            userService.deleteUserRole(user.getId());
            //删除用户本身
            user.setIsDeleted(User.STATUS_DELETE);
            boolean result = userService.updateById(user);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    /**
     * 重置密码，初始888888
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public ResponseModel resetPassword(HttpServletRequest request, User user) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("重置成功");
        try {
            user.setToken(request.getHeader(Constant.USER_TOKEN));
            user.setPassword("888888");//重置密码
            //执行修改初始化操作
            user.preUpdate();
            boolean result = userService.updateById(user);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("重置失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }


    /**
     * 修改个人信息
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "/updatePersonalUser", method = RequestMethod.POST)
    public ResponseModel updatePersonalUser(HttpServletRequest request, User user) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("修改成功");
        try {
            //设置登录用户token
            user.setToken(request.getHeader(Constant.USER_TOKEN));
            User loginUser = userService.getInfo(request.getHeader(Constant.USER_TOKEN));
            if (null != loginUser) {
                user.setId(loginUser.getId());
                //执行修改初始化操作
                user.preUpdate();
                boolean result = userService.updateById(user);
                if (!result) {
                    ret = ResponseModel.getFailedResponseModel().setMessage("修改失败");
                } else {
                    //更新缓存数据
                    userService.updateCacheUser(request.getHeader(Constant.LOGIN_CHANNEL), user.getToken());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    /**
     * 修改个人密码
     *
     * @param request
     * @param password
     * @param oldPassword
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
    public ResponseModel updatePassword(HttpServletRequest request, String password, String oldPassword) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("修改成功");
        try {
            User loginUser = userService.getInfo(request.getHeader(Constant.USER_TOKEN));
            loginUser = userService.selectById(loginUser.getId());
            if (!oldPassword.equals(loginUser.getPassword())) {
                ret = ResponseModel.getFailedResponseModel().setMessage("旧密码错误");
                return ret;
            }
            User user = new User();
            user.setId(loginUser.getId());
            user.setPassword(password);
            //设置登录用户token
            user.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行修改初始化操作
            user.preUpdate();
            boolean result = userService.updateById(user);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }


}
