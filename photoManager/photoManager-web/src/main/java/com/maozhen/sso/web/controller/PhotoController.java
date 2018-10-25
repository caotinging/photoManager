package com.maozhen.sso.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.maozhen.sso.common.ex.BaseException;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.CommomUtils;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.common.utils.UserUtil;
import com.maozhen.sso.model.Image;
import com.maozhen.sso.model.Photo;
import com.maozhen.sso.model.User;
import com.maozhen.sso.service.ImageService;
import com.maozhen.sso.service.PhotoService;
import com.maozhen.sso.vo.Carouse;
import com.maozhen.sso.vo.PhotoVo;

import io.swagger.annotations.Api;

/**
 * @author caoting
 * @date 2018年10月16日
 */
@RestController
@RequestMapping("/photo")
@Api(value = "相册模块")
public class PhotoController {
	
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private ImageService imageService;
	
	/**
	 * 更新用户相册
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	@PostMapping("/update")
	public ResponseModel update(HttpServletRequest request, Photo photo) {
		ResponseModel res = new ResponseModel();
		try {
			String token = request.getHeader(Constant.USER_TOKEN);
			photo.setToken(token);
			photo.preUpdate();
			
			boolean isSuccess = photoService.insert(photo);
			if (!isSuccess) {
				throw new BaseException("500", "更新相册失败");
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
	 * 删除用户相册
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	@GetMapping("/delete")
	public ResponseModel delete(Photo photo) {
		ResponseModel res = new ResponseModel();
		try {
			boolean isSuccess = photoService.deleteById(photo.getId());
			
			Wrapper<Image> wrapper = new EntityWrapper<Image>().eq("photo_id", photo.getId());
			//TODO 删除相册下图片
			List<Image> list = imageService.selectList(wrapper);
			for (Image image : list) {
				imageService.deleteById(image.getId());
			}
			
			if (!isSuccess) {
				throw new BaseException("500", "删除相册失败");
			}
			res = ResponseModel.getSuccessResponseModel();
			
		} catch (Exception ex2) {
			res = ResponseModel.getFailedResponseModel();
		}
		return res;
	}
	
	/**
	 * 新增用户相册
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	@PostMapping("/create")
	public ResponseModel create(HttpServletRequest request, Photo photo) {
		ResponseModel res = new ResponseModel();
		try {
			String token = request.getHeader(Constant.USER_TOKEN);
			
			Object userObj = UserUtil.getLoginUser(token);
			if ( null == userObj ) {
				throw new BaseException("500","用户登录状态异常");
			}
			User user = (User) userObj;
			
			// 相册名去重
			String photoName = photo.getPhotoName();
			if (StringUtils.isNoneEmpty(photoName)) {
				List<Photo> list = photoService.selectList(new EntityWrapper<Photo>().eq("photo_name", photoName)
						.eq("is_deleted", 0));
				if (CommomUtils.listIsNotEmpty(list)) {
					throw new BaseException("500", "相册名重复");
				}
			}
			
			photo.setToken(token);
			photo.setUserId(user.getId());
			photo.preInsert();
			
			boolean isSuccess = photoService.insert(photo);
			if (!isSuccess) {
				throw new BaseException("500", "新增相册失败");
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
	 * 查询用户相册列表
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	@PostMapping("/list")
	public ResponseModel list(HttpServletRequest request) {
		ResponseModel res = new ResponseModel();
		try {
			String token = request.getHeader(Constant.USER_TOKEN);
			Object userObj = UserUtil.getLoginUser(token);
			
			if ( null == userObj ) {
				throw new BaseException("500","用户登录状态异常");
			}
			User user = (User) userObj;
			
			Page<Photo> page = photoService.listByUserId(user);
			
			// 进行封装
			List<Photo> photoList = page.getRecords();
			PhotoVo vo = new PhotoVo();
			vo.setCarouseColSize(3);
			vo.setCarouseRowSize((photoList.size()-1)/3+1);
			
			for (Photo photo : photoList) {
				// 查询当前相册的前五张图片
				Page<Image> imgPage = imageService.selectPage(new Page<Image>(1,5), new EntityWrapper<Image>()
						.eq("photo_id", photo.getId())
						.eq("is_deleted", 0)
						.orderBy("gmt_create", false));
				
				List<Image> imgList = imgPage.getRecords();
				List<Carouse> carouses = Lists.newArrayList();
				
				for (Image img : imgList) {
					Carouse carouse = new Carouse();
					carouse.setKey(img.getId()+"");
					carouse.setSrc(img.getImageUrl());
					carouse.setDesc("");
					
					carouses.add(carouse);
				}
				photo.setCarouselList(carouses);
			}
			vo.setPhotoList(photoList);
			
			res = ResponseModel.getSuccessResponseModel();
			res.setData(vo);
			
		} catch (BaseException ex) {
			
			res.setCode(ex.getErrorCode());
			res.setMessage(ex.getMessage());
			
		} catch (Exception ex2) {
			res = ResponseModel.getFailedResponseModel();
		}
		return res;
	}
	
}
