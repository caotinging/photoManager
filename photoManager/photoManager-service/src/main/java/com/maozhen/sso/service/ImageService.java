package com.maozhen.sso.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.maozhen.sso.common.ex.BaseException;
import com.maozhen.sso.model.Image;
import com.maozhen.sso.model.Photo;

/**
 * @author caoting
 * @date 2018年10月16日
 */
public interface ImageService extends IService<Image> {

	/**
	 * 根据相册id获取相册下图片
	 * @param photo
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	Page<Image> listByPhotoId(Photo photo) throws BaseException;

	/**
	 * 获取相册下所有图片
	 * @param photo
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年11月13日
	 */
	Photo listImage(Photo photo);

}
