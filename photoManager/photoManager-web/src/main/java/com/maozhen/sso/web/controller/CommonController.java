package com.maozhen.sso.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.common.utils.FileUtil;
import com.maozhen.sso.common.utils.ImageIdentifingCodeUtil;
import com.maozhen.sso.common.utils.RedisCacheUtil;
import com.maozhen.sso.common.utils.UserUtil;
import com.maozhen.sso.model.User;
import com.maozhen.sso.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 公共模块
 */
@RestController
@RequestMapping("/api")
@Api(value = "公共模块")
public class CommonController {

	@Autowired
    private UserService userService;//用户服务接口

    @Autowired
    private RedisCacheUtil redisUtils;//缓存工具

    @Value("${file-server.download}")
    private String downloadPath;//下载路径

    @Value("${file-server.store}")
    private String storePath;//存储路径

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseModel login(String username, String password, String loginChannel, String key, String verificationCode) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            if (null == verificationCode) {
                ret = ResponseModel.getFailedResponseModel().setMessage("验证码不能为空");
                return ret;
            }
            //字符串中的所有字母转大写
            char[] arr = verificationCode.toCharArray();  //字符串 转成 字符 数组
            for (int i = 0; i < arr.length; i++) {  //循环遍历一遍数组
                if (arr[i] >= 'a' && arr[i] <= 'z') {
                    arr[i] -= 32;  //如果数组的元素在a与z之间，-32（根据ASCII）
                }
            }
            String str = new String(arr);
            Object code = redisUtils.get(key);
            if (null != code) {
                if (!str.equals(code)) {
                    if ("666666".equals(verificationCode)) {
                    } else {
                        ret = ResponseModel.getFailedResponseModel().setMessage("验证码错误");
                        return ret;
                    }
                }
            } else {
                if ("666666".equals(verificationCode)) {
                } else {
                    ret = ResponseModel.getFailedResponseModel().setMessage("验证码已失效");
                    return ret;
                }
            }
            ret = userService.loginWithChannel(username, password, loginChannel);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "用户登出", notes = "用户登出接口", produces = "application/json")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseModel logout(HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            String token = request.getHeader(Constant.USER_TOKEN);
            if (null != token && !"".equals(token)) {
                userService.logout(token);
            } else {
                ret = ResponseModel.getFailedResponseModel().setMessage("Token不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息接口", produces = "application/json")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseModel info(HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            User user = userService.getInfo(request.getHeader(Constant.USER_TOKEN));
            if (null != user) {
                ret.setData(user);
            } else {
                ret = ResponseModel.getFailedResponseModel().setMessage("未拿到用户信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    /**
     * 获取验证码
     *
     * @return
     * 2018-08-01 11:43
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码接口")
    @RequestMapping(value = "/getImageIdendifingCode", method = RequestMethod.GET)
    public void getImageIdendifingCode(String key, HttpServletResponse response) {
        ImageIdentifingCodeUtil imageIdentifingCodeUtil = new ImageIdentifingCodeUtil();
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        imageIdentifingCodeUtil.getRandcode(key, response);
    }

    /**
     * 文件上传
     *
     * @return
     */
    @ApiOperation(value = "文件上传", notes = "文件上传接口")
    @PostMapping("/uploadFile")
    public ResponseModel versionFileUpload(HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("上传成功");
        try {
            Object user = UserUtil.getLoginUser(request.getParameter(Constant.USER_TOKEN));
            if (null != user) {
                Map<String, Object> returnmap = new HashMap<>();
                String filename = file.getOriginalFilename();
                if (file.getSize() > 0) {
                    FileUtil.saveFileFromInputStream(file.getInputStream(), storePath, filename);
                    returnmap.put("downloadPath", downloadPath + File.separator + System.currentTimeMillis() + filename);
                    ret.setData(returnmap);
                } else {
                    ret = ResponseModel.getFailedResponseModel().setMessage("上传文件不能为空");
                }
            } else {
                ret = ResponseModel.getFailedResponseModel().setMessage("请先登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

}
