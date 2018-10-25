package com.maozhen.sso.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.maozhen.sso.common.ex.BaseException;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.common.utils.UserUtil;
import com.maozhen.sso.model.Image;
import com.maozhen.sso.model.Photo;
import com.maozhen.sso.model.User;
import com.maozhen.sso.service.ImageService;

import io.swagger.annotations.Api;

/**
 * @author caoting
 * @date 2018年10月16日
 */
@RestController
@RequestMapping("/image")
@Api(value = "相册图片模块")
public class ImageController {

	@Autowired
	private ImageService imageService;
	
	/**
	 * 相册删除图片
	 * @param request
	 * @param image
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	@PostMapping("/delete")
	public ResponseModel delete(HttpServletRequest request, Image image) {
		ResponseModel res = new ResponseModel();
		try {
			String token = request.getHeader(Constant.USER_TOKEN);
			Object userObj = UserUtil.getLoginUser(token);
			
			if ( null == userObj ) {
				throw new BaseException("500","用户登录状态异常");
			}
			User user = (User) userObj;
			
			image.setToken(token);
			image.setIsDeleted(image.STATUS_DELETE);
			image.preUpdate();
			
			boolean isSuccess = imageService.updateById(image);
			if (!isSuccess) {
				throw new BaseException("500", "删除失败");
			}
			
			res = ResponseModel.getSuccessResponseModel();
			
		} catch (BaseException ex) {
			
			res.setCode(ex.getErrorCode());
			res.setMessage(ex.getMessage());
			
		} catch (Exception ex2) {
			res = ResponseModel.getFailedResponseModel();
		}
		return res;
	}
	
	/**
	 * 相册新增图片
	 * @param request
	 * @param image
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	@PostMapping("/add")
	public ResponseModel add(HttpServletRequest request, Image image) {
		ResponseModel res = new ResponseModel();
		try {
			String token = request.getHeader(Constant.USER_TOKEN);
			Object userObj = UserUtil.getLoginUser(token);
			
			if ( null == userObj ) {
				throw new BaseException("500","用户登录状态异常");
			}
			User user = (User) userObj;
			
			image.setToken(token);
			image.preInsert();
			
			boolean isSuccess = imageService.insert(image);
			if (!isSuccess) {
				throw new BaseException("500", "上传失败");
			}
			
			res = ResponseModel.getSuccessResponseModel();
			
		} catch (BaseException ex) {
			
			res.setCode(ex.getErrorCode());
			res.setMessage(ex.getMessage());
			
		} catch (Exception ex2) {
			res = ResponseModel.getFailedResponseModel();
		}
		return res;
	}
	
	/**
	 * 相册中图片
	 * @param request
	 * @param image
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	@PostMapping("/list")
	public ResponseModel list(Photo photo) {
		ResponseModel res = new ResponseModel();
		try {
			Page<Image> page = imageService.listByPhotoId(photo);
			res = ResponseModel.getSuccessResponseModel();
			res.setData(page);
			
		} catch (BaseException ex) {
			
			res.setCode(ex.getErrorCode());
			res.setMessage(ex.getMessage());
			
		} catch (Exception ex2) {
			res = ResponseModel.getFailedResponseModel();
		}
		return res;
	}
}
