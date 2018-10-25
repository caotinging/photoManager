package com.maozhen.sso.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.maozhen.sso.model.Image;
import com.maozhen.sso.model.Photo;
import com.maozhen.sso.model.User;

/**
 * @author caoting
 * @date 2018年10月16日
 */
public interface PhotoService extends IService<Photo> {

	/**
	 * 根据用户id获取用户下相册列表
	 * @param user
	 * @return
	 * 
	 * @author caoting
	 * @date 2018年10月16日
	 */
	Page<Photo> listByUserId(User user);

}
