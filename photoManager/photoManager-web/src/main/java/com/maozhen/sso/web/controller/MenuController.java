package com.maozhen.sso.web.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.common.utils.UserUtil;
import com.maozhen.sso.model.Menu;
import com.maozhen.sso.model.User;
import com.maozhen.sso.service.MenuService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/menu")
@Api(value = "菜单模块")
public class MenuController {

	@Autowired
    private MenuService menuService;//菜单服务接口

    @ApiOperation(value = "菜单树", notes = "菜单树接口", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuType", value = "菜单类型", dataType = "int", required = true, paramType = "query")
    })
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public ResponseModel tree(HttpServletRequest request, int menuType) {
        String token = request.getHeader(Constant.USER_TOKEN);
        Object object = UserUtil.getLoginUser(token);
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            if (null != object) {
                User user = (User) object;
                //判断是否管理员
                List<Menu> menus = Lists.newArrayList();
                Map<String, Object> param = Maps.newConcurrentMap();
                param.put("menuType", menuType);
                if (!user.isAdmin()) {
                    param.put("roles", user.getRoles());
                    menus = menuService.getMenusByRole(param);
                } else {
                    //查询根菜单
                    Wrapper<Menu> entity = new EntityWrapper<Menu>();
                    if (menuType != -1) {
                        entity.eq("menu_type", menuType);
                    }
                    entity.eq("is_deleted", 0);//设置查询条件
                    menus = menuService.selectList(entity);
                }
                ret.setData(menus);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "菜单列表", notes = "菜单列表接口", produces = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseModel list(Menu menu) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            //装载查询条件
            Wrapper<Menu> entity = new EntityWrapper<Menu>();
            entity.eq("parent_id", menu.getId()).eq("is_deleted", 0);
            Page<Menu> page = menuService.selectPage(new Page<Menu>(menu.getCurrentPage(), menu.getPageSize()), entity);
            ret.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "菜单新增", notes = "菜单新增接口", produces = "application/json")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseModel create(Menu menu, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("新增成功");
        ;
        try {
            //设置登录用户token
            menu.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行新增初始化操作
            menu.preInsert();
            boolean result = menuService.insert(menu);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "菜单修改", notes = "菜单修改接口", produces = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseModel update(Menu menu, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("修改成功");
        ;
        try {
            //设置登录用户token
            menu.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行修改初始化操作
            menu.preUpdate();
            boolean result = menuService.updateById(menu);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "菜单删除", notes = "菜单删除接口", produces = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseModel delete(Menu menu) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("删除成功");
        ;
        try {
            menu.setIsDeleted(Menu.STATUS_DELETE);
            boolean result = menuService.updateById(menu);
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
     * 获取角色对应的菜单编号
     *
     * @return
     */
    @RequestMapping(path = "/findMenuByRole", method = RequestMethod.GET)
    public ResponseModel findMenuByRole(Long roleId) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            List<Long> menus = menuService.getMenuIdsByRole(roleId);
            ret.setData(menus);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

}
